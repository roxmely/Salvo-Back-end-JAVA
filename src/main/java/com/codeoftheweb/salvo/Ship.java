package com.codeoftheweb.salvo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity
public class Ship {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name = "native", strategy = "native")
    private long id;
//***Conectamos con Game Player****//

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gamePlayer;

    //***Descripción***//

private  String shipType;
    @ElementCollection
    @Column(name="shipLocations")
    private List<String> shipLocations;


    //***Constructor***//
        public Ship() {}

    //***Personalizados***//
    public Ship(String shipType , GamePlayer gamePlayer, List<String> shipLocations) {
        this.shipType = shipType;
        this.shipLocations = shipLocations;
        this.gamePlayer=gamePlayer;
    }

    //***Getters-Setters***//

    public String getShipType() {
        return this.shipType;
    }
    public List<String> getShipLocations() {
        return this.shipLocations;
    }
    public void setGamePlayer(GamePlayer gamePlayer) {
        this.gamePlayer = gamePlayer;
    }
    public void setShipType(String shipType) {
        this.shipType = shipType;
    }
    public void setShipLocations(List<String> shipLocations) {
        this.shipLocations = shipLocations;
    }
    public GamePlayer getGamePlayer() {
        return gamePlayer;
    }


    //***Info-Map-Ships***//

    public Map<String, Object> shipDTO() {
        Map<String, Object> shipsDto = new LinkedHashMap<String, Object>();
        shipsDto.put("shipType",this.shipType);
        shipsDto.put("shipLocations",this.shipLocations);
        return shipsDto;
    }
}
