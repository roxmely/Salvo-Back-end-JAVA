package com.codeoftheweb.salvo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Salvo> salvoes = new LinkedHashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "gamePlayer", fetch = FetchType.EAGER)
    Set<Ship> ship = new HashSet<>();//inicializar

    private Date created;

    //***Constructor***//

 public GamePlayer() {}

    //***Personalizados***//

    public GamePlayer(Player player, Game game) {
        this.player=player;
        this.game = game;
        this.created =new Date();}

    //***Getters-Setters***//

    public Long getId() {
        return this.id;
    }

    public Game getGame(){ return this.game;
     }

    public Player getPlayer(){ return player;
    }

    public Set<Ship> getShip() {
        return this.ship;
    }

    public void addShip(Ship ship) {
        this.ship.add(ship);
    }

    public Set<Salvo> getSalvoes() {
        return this.salvoes;
    }

    public void setGame(Game game){
     this.game= game;
    }

    public void setPlayer(Player player){
     this.player= player;
    }

    //***Methods***//


    public List<Object> getShips() {
        return this.ship.stream().map(Ship::shipDTO).collect(Collectors.toList());
    }
    public Object getSalvoesDTO() {
        return this.salvoes.stream().map(Salvo::salvoDTO).collect(Collectors.toList());
    }

    public void addSalvo(Salvo salvo) {
        this.salvoes.add(salvo);

    }
    public Score getScore(){
        return this.player.scoreNull(this.game);
    }
                //***Info-Map-Game-Players***//
    public  Map<String, Object>makeGamePlayerDTO() {
        Map<String, Object> dtoGamePlayer = new LinkedHashMap<String, Object>();
        dtoGamePlayer.put("gpId", this.getId());
        if (getScore() == null) {
            dtoGamePlayer.put("score", null);
        } else {
            dtoGamePlayer.put("score", getScore().getScore());
        }
         dtoGamePlayer.put("player",this.getPlayer().makePlayerDTO());
         return dtoGamePlayer;
    }
    /**methodos para hits**/
public GamePlayer opponent() {
        return this.game.getGamePlayer().stream().filter(gp -> gp.getId() != this.id).findFirst().orElse(null);
    }
    public Integer getLastTurn(){
        if(!this.getSalvoes().isEmpty()){
            return this.getSalvoes().stream()
                    .map(salvo1 ->salvo1.getTurnNumber() )
                    .max((x,y)->Integer.compare(x,y))
                    .get();
        }else {
            return 0;
        }
    }
    public Salvo getLastSalvo() {
        if(!this.getSalvoes().isEmpty()){
            return this.getSalvoes().stream().filter(salvo -> salvo.getTurnNumber() == this.getLastTurn()).findFirst().orElse(null);
        }
        return null;
    }

}

