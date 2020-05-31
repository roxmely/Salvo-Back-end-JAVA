package com.codeoftheweb.salvo;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.*;


@Entity
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private Long id;

    //***Descripción***//

    private String email;
    private String password;

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<GamePlayer> gamePlayer = new HashSet<>();//inicializar

    @OneToMany(mappedBy = "player", fetch = FetchType.EAGER)
    Set<Score> scores = new HashSet<>();//inicializar

    //***Constructor***//
    public Player() {
    }

    //***Personalizados***//

    public Player(String email, String password) {

        this.email = email;
        this.password = password;
    }

    //***Getters-Setters***//

    public Long getId() {

        return id;
    }

    public Set<Score> getScores() {

        return this.scores;
    }



    public String getEmail() {

        return email;
    }

    public String getPassword() {

        return password;
    }

    public void setEmail(String email) {

        this.email = email;
    }

    public void setPassword(String password) {

        this.password = password;
    }
    public void setScores(Set<Score> scores) {
        this.scores = scores;
    }

    //***Methods***//

    public void addGamePlayer(GamePlayer gamePlayer) {
        gamePlayer.setPlayer(this);
        this.gamePlayer.add(gamePlayer);
    }
    public Score scoreNull(Game game){
        return scores.stream().filter(score -> score.getGame().equals(game)).findFirst().orElse(null);
    }

    public Map<String, Object> makePlayerDTO() {
        Map<String, Object> dtoPlayer = new LinkedHashMap<String, Object>();
        dtoPlayer.put("id", this.getId());
        dtoPlayer.put("email", this.getEmail());
        dtoPlayer.put("scores",this.getScores().stream().map(Score::getScore));
        return dtoPlayer;

    }

}