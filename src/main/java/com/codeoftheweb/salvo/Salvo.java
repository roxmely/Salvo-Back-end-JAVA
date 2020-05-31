package com.codeoftheweb.salvo;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Salvo {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name ="GamePlayer_id")
    private GamePlayer gamePlayer;

    @ElementCollection
    @Column(name="salvoLocations")
    private List<String> salvoLocations;

    //***Descripción***//

    private  int turnNumber;

    //***Constructor***//

    public Salvo() {}

    //***Personalizados***//
    public Salvo(GamePlayer gamePlayer, int turnNumber, List<String> salvoLocations) {
        this.gamePlayer = gamePlayer;
        this.turnNumber = turnNumber;
        this.salvoLocations = salvoLocations;
    }


    public int getTurnNumber() {
        return this.turnNumber;
    }
    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }
    public void setShipLocations(List<String> shipLocations) {
        this.salvoLocations = shipLocations;
    }

    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }

    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }

    public List<String> getSalvoLocations() {
        return salvoLocations;
    }

    public void setSalvoLocations(List<String> salvoLocations) {
        this.salvoLocations = salvoLocations;
    }

    public Map<String, Object> salvoDTO() {
        Map<String, Object> salvoesDto = new LinkedHashMap<String, Object>();
        salvoesDto.put("turnNumber",this.turnNumber);
        salvoesDto.put("salvoLocations",this.salvoLocations);
        return salvoesDto;
    }



}
