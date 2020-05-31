package com.codeoftheweb.salvo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import org.springframework.security.crypto.password.PasswordEncoder;


@RestController
@RequestMapping("/api")

public class SalvoController {

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private ScoreRepository scoreRepository;
    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private GamePlayerRepository gamePlayerRepository;

    @Autowired
    private ShipRepository shipRepository;
    @Autowired
    private SalvoRepository salvoRepository;

                             //******Games-Info******//

    @RequestMapping(path = "/games", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> getId(Authentication authentication) {
        Player player = playerAuth();

        if (player == null) {
            return  new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        else{
        Game newGame = new Game();
        gameRepository.save(newGame);
        GamePlayer gamePlayer = new GamePlayer(player, newGame);
        gamePlayerRepository.save(gamePlayer);
            return new ResponseEntity<>(makeMap("id", gamePlayer.getId()), HttpStatus.CREATED);
    }}

                             //******Game-view******//
    @RequestMapping(path="/game_view/{nn}", method = RequestMethod.GET)
    public Object getShipId(@PathVariable("nn") Long gpid, Authentication authentication) {
        Player player = playerAuth();
        GamePlayer gamePlayer = gamePlayerRepository.getOne(gpid);

        if(player == null){
            return this.responseEntity("error","Forbidden",HttpStatus.FORBIDDEN);
        }
        if(gamePlayer.getPlayer().getId()!= player.getId()){
            return this.responseEntity("error", "Unauthorized", HttpStatus.FORBIDDEN);
        }

        return gameViewDTO(gamePlayerRepository.getOne(gpid));

    }

    private ResponseEntity<Object> gameViewDTO(GamePlayer gamePlayer){
        Map<String, Object> gView = new LinkedHashMap<>();
        gView.put("id", gamePlayer.getGame().getId());
        gView.put("created", gamePlayer.getGame().getCreated());
        gView.put("gamePlayers", gamePlayer.getGame().gamePlayerDto());
        gView.put("ships", gamePlayer.getShips());
        gView.put("salvoes", gamePlayer.getSalvoes().stream().map(Salvo::salvoDTO).collect((toList())));
        gView.put("status", getGameStates (gamePlayer));
        gView.put("history", gamePlayer.getSalvoes().stream().sorted((gp1,gp2)->((Integer)gp1.getTurnNumber()).compareTo(gp2.getTurnNumber()))
                .map(salvo -> shipSalvoDto(gamePlayer,salvo)).collect(Collectors.toList()));
        return new ResponseEntity<>(gView, HttpStatus.OK);
    }

                    //******Create_Games******//

   @RequestMapping(path= "/games", method = RequestMethod.GET)
   public Map<String, Object> createGame(Authentication authentication) {
       Map<String, Object> makeGameDto = new HashMap<>();
       List<Game> games = gameRepository.findAll();

       if(authentication != null) {

           makeGameDto.put("player" ,playerRepository.findByEmail(authentication.getName()).makePlayerDTO());
       }
              makeGameDto.put("games", gameRepository.findAll()
                      .stream()
                      .sorted((gp1,gp2)->gp1.getId().compareTo(gp2.getId()))
               .map(Game::gameDTO)
               .collect(Collectors.toList()));
       return makeGameDto;
   }

                                             //*****Add placing ships*****//

   @RequestMapping(path="/games/players/{gpId}/ships", method=RequestMethod.POST)
   public Object addShip(@PathVariable long gpId,@RequestBody Set<Ship> ships) {
       GamePlayer gamePlayer = gamePlayerRepository.getOne(gpId);
       Player playerAuth = playerAuth();

       if (playerAuth == null) {
           return this.responseEntity("Message", "There is no player with the given id", HttpStatus.UNAUTHORIZED);
       }
       if (gamePlayer == null) {
           return this.responseEntity("Message", "User not connected", HttpStatus.UNAUTHORIZED);
       }


       if (playerAuth.getId() != gamePlayer.getPlayer().getId()) {
           return this.responseEntity("KEY_LOGIN_ERROR", "Player other than logged in", HttpStatus.FORBIDDEN);

       }

           if (gamePlayer.getShips().size() != 0) {

               return this.responseEntity("kEY_ERROR", "Already placed!", HttpStatus.FORBIDDEN);
           }
           if (ships.size() != 5) {

               return this.responseEntity("kEY_ERROR", "Opponent's turn", HttpStatus.FORBIDDEN);
           }else {

               ships.forEach((ship) -> {
                   ship.setGamePlayer(gamePlayer);
                   shipRepository.save(ship);

               });
               return this.responseEntity("KEY_CREATED", "Ships successfully placed!", HttpStatus.CREATED);
           }
       }
                          //******add Salvoes******//

    @RequestMapping(path="/games/players/{gpId}/salvoes", method = RequestMethod.POST)
    public Object setSalvoes(@PathVariable("gpId") long gpId, @RequestBody Salvo salvo) {
        Player authenticatedPlayer = playerAuth();

        if (authenticatedPlayer == null) {
            return  this.responseEntity("KEY_LOGIN_ERROR","there is no player with the given id", HttpStatus.UNAUTHORIZED);
        }

        GamePlayer gamePlayer = gamePlayerRepository.getOne(gpId);
        if (gamePlayer == null) {
            return  this.responseEntity("KEY_NOT_FOUND","user not connected", HttpStatus.UNAUTHORIZED);
        }

        if (authenticatedPlayer.getId() != gamePlayer.getPlayer().getId()) {
            return  this.responseEntity("KEY_LOGIN_ERROR","Player other than logged in", HttpStatus.FORBIDDEN);

        }  if (salvo.getSalvoLocations().size() !=5) {
            return this.responseEntity("KEY_ERROR","Opponent's turn",HttpStatus.CONFLICT);

        } else if(gamePlayer.getSalvoes().size() > opponent(gamePlayer).getSalvoes().size()) {
            return  this.responseEntity("key_forbidden","Waiting for opponent's salvoes!", HttpStatus.FORBIDDEN);
        }
        else {
            salvo.setTurnNumber(gamePlayer.getSalvoes().size() +1);
            gamePlayer.addSalvo(salvo);
            gamePlayerRepository.save(gamePlayer);
            salvo.setGamePlayer(gamePlayer);//esto
            salvoRepository.save(salvo);

            return this.responseEntity("KEY_CREATED","Salvoes successfully placed!", HttpStatus.CREATED);
        }

    }
    //******Sending data in URL path variables******//


                   //**Game-player Opponent**//
    private GamePlayer opponent(GamePlayer gamePlayer) {
        return gamePlayer.getGame().getGamePlayer()
                .stream()
                .filter(gp -> gp.getId() != gamePlayer.getId())
                .findFirst()
                .orElse(null);
    }
    private Set<String> getOppShips (GamePlayer gamePlayer) {
        return opponent(gamePlayer).getShip().stream().map(ship -> ship.getShipLocations()).flatMap(Collection::stream).collect(Collectors.toSet());
    }
    private Set<String> getOppSalvos (GamePlayer gamePlayer) {
        return opponent(gamePlayer).getSalvoes().stream().map(ship -> ship.getSalvoLocations()).flatMap(Collection::stream).collect(Collectors.toSet());
    }

  private Map<String, List<String>> getHits (Set<Ship> ships, Salvo salvo){
      Map<String, List<String>> dto1 = new LinkedHashMap<>();
      //GamePlayer gp2 = opponent(ship.getGamePlayer());
      List<String> hits = new ArrayList<>();
      List<String> miss = new ArrayList<>();
      List<String> type = new ArrayList<>();
      List<String> totalSalvoLocations = salvo.getGamePlayer().getSalvoes().stream().flatMap(gpSalvo -> gpSalvo.getSalvoLocations().stream()).collect(toList());
      List<String> shipLocations = ships.stream().flatMap(ship -> ship.getShipLocations().stream()).collect(toList());
      List<String> salvoLocations = salvo.getSalvoLocations();

          for (  String loc : salvoLocations) {
              if (shipLocations.contains(loc)) {
                  hits.add(loc);

              } else {
                  miss.add(loc);
              }
          }
           for(Ship ship: ships) {

          if(totalSalvoLocations.containsAll(ship.getShipLocations())){
              type.add(ship.getShipType());
          }
       }

      dto1.put("hits", hits);
      dto1.put("miss", miss);
      dto1.put("type", type);

      return dto1;
  }
    //******Sunk-Ships-Info******//

    private Map<String, Object> shipSalvoDto(GamePlayer gamePlayer,Salvo salvo) {
        Map<String, Object> dto = new LinkedHashMap<String, Object>();
        dto.put("turn", salvo.getTurnNumber());
        dto.put("hitsAndMiss", this.getHits(opponent(gamePlayer).getShip(), salvo));
        dto.put("shipOpp", this.getOppShips(gamePlayer));
        dto.put("salvosOpp", this.getOppSalvos(gamePlayer));
        return dto;

    }

    //******Create Player******//

    @Autowired
    private PasswordEncoder passwordEncoder;

    @RequestMapping(path = "/players", method = RequestMethod.POST)
    public ResponseEntity<Object> register(@RequestBody Player player) {

        if (player.getEmail().isEmpty() || player.getPassword().isEmpty()) {
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if (playerRepository.findByEmail(player.getEmail()) != null) {
            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);
        }
        player.setPassword(passwordEncoder.encode(player.getPassword()));
        playerRepository.save(player);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

        @RequestMapping(path="/game/{nn}/players", method = RequestMethod.POST)
        public Object joinGame(@PathVariable("nn") Long gameId, Authentication authentication) {
            Player player = playerAuth();

            if (player == null) {
               return this.responseEntity("KEY_ERROR","no current user", HttpStatus.UNAUTHORIZED);
            }

           Game game= gameRepository.getOne(gameId);

            if (game== null) {
                return this.responseEntity("KEY_ERROR","No such game", HttpStatus.FORBIDDEN);
            }

            if (game.gamePlayer.size() ==2) {
                return this.responseEntity("KEY_ERROR","Game is full",HttpStatus.FORBIDDEN);
            }
            GamePlayer gamePlayer = new GamePlayer(player, game);
            gamePlayerRepository.save(gamePlayer);
            return this.responseEntity("KEY_GPID",gamePlayer.getId().toString(),HttpStatus.CREATED);
        }

    private Set<String> getShots (GamePlayer gamePlayer) {
        if (gamePlayer != null) {
            return gamePlayer.getSalvoes().stream().map(oneSalvo -> oneSalvo.getSalvoLocations()).flatMap(Collection::stream).collect(Collectors.toSet());
        } else return null;
    }
    private Set<String> getShipLocations (GamePlayer gamePlayer) {
        return gamePlayer.getShip().stream().map(ship -> ship.getShipLocations()).flatMap(Collection::stream).collect(Collectors.toSet());
    }

    private Set<String> getHitsCal (GamePlayer gamePlayer, Boolean getHits) {
        Set<String> shipLocations = getShipLocations(gamePlayer);
        Set<String> opponentShots = getShots(opponent(gamePlayer));
        Set<String> dto = new LinkedHashSet<>();
        if (opponentShots != null) {

            if(getHits) {
                dto = opponentShots.stream().filter(shipLocations::contains).collect(Collectors.toSet());
            } else {
                dto = opponentShots.stream().filter(oneShot -> !shipLocations.contains(oneShot)).collect(Collectors.toSet());
            }
            return dto;
        }

        return dto;
    }
    public List<String> getAllHits(GamePlayer gamePlayer, Salvo salvo) {
        GamePlayer opponent = opponent(gamePlayer);
        long player_id = salvo.getGamePlayer().getPlayer().getId();
        if (opponent != null) {
            int turn = salvo.getTurnNumber();
            List<String> salvoLocations = new ArrayList<>();
            for (Salvo salvo1 : salvo.getGamePlayer().getSalvoes()) {
                if (salvo1.getTurnNumber() <= turn) {
                    getTurnHits(gamePlayer, salvo1).forEach(hit -> salvoLocations.add(hit));
                }}
            return salvoLocations;

        } else {
            return null;
        }
    }

    public List<String> getTurnHits(GamePlayer gamePlayer, Salvo salvo) {
        GamePlayer opponent = opponent(salvo.getGamePlayer());
        if (opponent != null) {
            List<String> salvoLocations = salvo.getSalvoLocations();
            List<String> opponentShipLocations = opponent.getShip().stream()
                    .map(ship -> ship.getShipLocations())
                    .flatMap(locations -> locations.stream())
                    .collect(Collectors.toList());
            return salvoLocations.stream()
                    .filter(location -> opponentShipLocations.contains(location))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

   public String getGameStates(GamePlayer gamePlayer) {

       if (gamePlayer.getGame().getGamePlayer().size() == 2) {
           GamePlayer opponent = opponent(gamePlayer);
           if (gamePlayer.getShips().isEmpty())
           {return "place your ships";}
           if (opponent.getShips().isEmpty()) {
               return "opponent is still placing the ships...";
           }
           else {

               GamePlayer player1 = new GamePlayer();
               GamePlayer player2 = new GamePlayer();
               if (gamePlayer.getId() < opponent.getId()) {
                   player1 = gamePlayer;
                   player2 = opponent;
               }
               else {
                   player1 = opponent;
                   player2 = gamePlayer;
               }
               if(player1.getSalvoes().size() == 0) {
                   return player1.getPlayer().getEmail() +"'s turn";}
               else if (player2.getSalvoes().size() == 0) {
                   return player2.getPlayer().getEmail() +"'s turn";
               }
               else if (player1.getSalvoes().size() == player2.getSalvoes().size()) {
                   if (getAllHits(player1, player1.getLastSalvo()).size() == 17 && getAllHits(player2, player2.getLastSalvo()).size() != 17) {
                       if (player1.getPlayer().scoreNull(player1.getGame()) == null) {
                           scoreRepository.save(new Score(1.0, player1.getGame(), player1.getPlayer()));
                           scoreRepository.save(new Score(0.0, player2.getGame(), player2.getPlayer()));
                       }
                       return player1.getPlayer().getEmail() + " wins!";
                   }
                   else if (getAllHits(player2, player2.getLastSalvo()).size() == 17 && getAllHits(player1, player1.getLastSalvo()).size() != 17) {
                       if (player2.getPlayer().scoreNull(player2.getGame()) == null) {
                           scoreRepository.save(new Score(1.0, player2.getGame(), player2.getPlayer()));
                           scoreRepository.save(new Score(0.0, player1.getGame(), player1.getPlayer()));
                       }
                       return player2.getPlayer().getEmail() + " wins!";
                   }
                   else if (getAllHits(player2, player2.getLastSalvo()).size() == 17 && getAllHits(player1, player1.getLastSalvo()).size() == 17) {
                       if (player1.getPlayer().scoreNull(player1.getGame()) == null && player2.getPlayer().scoreNull(player2.getGame()) == null) {
                           scoreRepository.save(new Score(0.5, player1.getGame(), player1.getPlayer()));
                           scoreRepository.save(new Score(0.5, player2.getGame(), player2.getPlayer()));
                       }
                       return "Tie!";
                   }
                   else {
                       return player1.getPlayer().getEmail() +"'s turn";}
               }
               else if (player1.getSalvoes().size() > player2.getSalvoes().size()) {
                   return player2.getPlayer().getEmail() +"'s turn";}
               else {
                   return player1.getPlayer().getEmail() +"'s turn";
               }
           }
       }
       else {
           return "waiting for opponent...";
       }
   }
    //******Players-Scores-Info******//


    @RequestMapping("/leaderboard")
    public Set<Map<String, Object>> scoreId() {
        Set<Map<String, Object>> leaderBoard = new HashSet<>();
        List<GamePlayer> gamePlayers = gamePlayerRepository.findAll();
        for (GamePlayer gamePlayer : gamePlayers) {
            Map<String, Object> gameDetail = new HashMap<>();
            gameDetail.put("name", gamePlayer.getPlayer().getEmail());
            gameDetail.put("totalScore", gamePlayer.getPlayer().getScores().stream().mapToDouble(score -> score.getScore()).sum());
            gameDetail.put("wins", gamePlayer.getPlayer().getScores().stream().filter(score -> score.getScore() == 1).count());
            gameDetail.put("losses", gamePlayer.getPlayer().getScores().stream().filter(score -> score.getScore() == 0).count());
            gameDetail.put("ties", gamePlayer.getPlayer().getScores().stream().filter(score -> score.getScore() == 0.5).count());
            leaderBoard.add(gameDetail);
        }
        return leaderBoard;
    }
/**
    @RequestMapping("/leaderboard")
    public Map<String, Object> scoreId() {
        List<GamePlayer>gamePlayers = gamePlayerRepository.findAll();
        Map<String, Object>leaderBoard = new LinkedHashMap<>();
        for (GamePlayer gp : gamePlayers) {
            Set<Score>scoreList = gp.getPlayer().getScores();
            if (!leaderBoard.containsKey(gp.getPlayer().getEmail())) {
                Map<String, Object>scoreInfo = new LinkedHashMap<>();
                scoreInfo.put("wins", scoreList.stream().filter(score -> score.getScore() == 1.0).count());
                scoreInfo.put("losses", scoreList.stream().filter(score -> score.getScore() == 0.0).count());
                scoreInfo.put("ties", scoreList.stream().filter(score -> score.getScore() == 0.5).count());
                scoreInfo.put("totalScore", scoreList.stream().mapToDouble(score -> score.getScore()).sum());
                leaderBoard.put(gp.getPlayer().getEmail(), scoreInfo);
            }
        }
        return leaderBoard;
    }
**/
    //****privates method****//

    //is Guest//
    private boolean isGuest(Authentication authentication) {
        return authentication == null || authentication instanceof AnonymousAuthenticationToken;
    }
 //player auth
 private Player playerAuth() {
     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
     if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
         return null;
     } else {
         return playerRepository.findByEmail(authentication.getName());
     }
 }

    //******ResponseEntity******//

    private ResponseEntity<Object> responseEntity(String key, Object value, HttpStatus httpStatus ) {
        Map<String,Object> responseMap = new LinkedHashMap<>();
        responseMap.put(key, value);
        return new ResponseEntity<>(responseMap, httpStatus);
    }

    private Map<String, Object> makeMap(String key, Object value) {
        Map<String, Object> map = new HashMap<>();
        map.put(key, value);
        return map;
    }

}
