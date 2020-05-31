package com.codeoftheweb.salvo;
    import com.fasterxml.jackson.annotation.JsonIgnore;
    import org.hibernate.annotations.GenericGenerator;
    import org.springframework.web.bind.annotation.RequestMapping;

    import javax.persistence.*;
    import java.util.*;
    import java.util.stream.Collectors;

    import static java.util.stream.Collectors.toList;

        @Entity

public class Game {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
        @GenericGenerator(name = "native", strategy = "native")
        private Long id;

        @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
        Set<GamePlayer> gamePlayer = new HashSet<>();//inicializar

            @OneToMany(mappedBy = "game", fetch = FetchType.EAGER)
            Set<Score> scores = new HashSet<>();//inicializar


        //***Descripción***//

        private Date created;

        //***Constructor***//

        public Game() {
            this.created = new Date();
        }

        //***Personalizados***//

        public Game(Date created) {
            this.created = created;

        }

        //***Getters-Setters***//
        public void addGamePlayer(GamePlayer gamePlayer) {
                this.gamePlayer.add(gamePlayer);
            }

            public Set<Score> getScores() {
                return this.scores;
            }

        public Date getCreated() {

            return created;
        }

        public void setCreated(Date created) {

            this.created = created;
        }

        public Long getId() {

            return this.id;
        }
            public long countGp() {
                return this.gamePlayer.size();
            }

        public void setId(Long id) {

            this.id = id;
        }

            public Game(Set<GamePlayer> gamePlayer) {
                this.gamePlayer = gamePlayer;
            }
            public void setScores(Set<Score> scores) {
                this.scores = scores;
            }
            public Set<GamePlayer> getGamePlayer() {
                return gamePlayer;
            }
            //***Methods***//
            // controller

            public Map<String, Object> gameDTO() {
                Map<String, Object> gameDto = new LinkedHashMap<String, Object>();
                gameDto.put("id", this.id);
                gameDto.put("created", this.created);
                gameDto.put("gamePlayers", gamePlayer.stream()
                        .sorted((gp1,gp2)->gp1.getId().compareTo(gp2.getId()))
                        .map(GamePlayer::makeGamePlayerDTO).collect(Collectors.toList()));
               // gameDto.put("scores", scores.stream().map(Score::scoreDTO).collect(Collectors.toList()));
                return gameDto;
            }

        //pongo el @JsonIgnore para que no entre en un bucle de datos.

        @JsonIgnore
        public List<Player> getPlayer() {
            return this.gamePlayer.stream().sorted((gp1,gp2)->(gp1.getId()).compareTo(gp2.getId())).map(GamePlayer::getPlayer).collect(toList());
        }

        @JsonIgnore
        public Object gamePlayerDto() {
            return this.gamePlayer.stream().sorted((gp1,gp2)->(gp1.getId()).compareTo(gp2.getId())).map(GamePlayer::makeGamePlayerDTO).collect(Collectors.toList());
        }

    }