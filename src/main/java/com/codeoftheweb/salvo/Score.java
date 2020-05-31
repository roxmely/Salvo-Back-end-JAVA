package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Score {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id")
    private Player player;

    private Double score;
    //***Constructor***//
    public Score(){}

    //***Personalizados***//

    public Score(Double score, Game game,Player player ) {
        this.score= score;
        this.game = game;
        this.player = player;
    }

    //***Getters-Setters***//

    public void setScore(Double score) {
        this.score   = score;
    }

    public void setGame(Game game) {
        this.game = game;
    }
    public Double getScore() {

        return this.score;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }

    public Game getGame() {
        return this.game;
    }

    public Player getPlayer() {
        return this.player;
    }

    //***Info-Map-Scores***//
    public Object scoreDTO() {
        Map<String,Object> scoresDTO = new LinkedHashMap<>();
        scoresDTO.put("playerID", this.player.getId());
        scoresDTO.put("score", this.score);
        return scoresDTO;
    }
}
