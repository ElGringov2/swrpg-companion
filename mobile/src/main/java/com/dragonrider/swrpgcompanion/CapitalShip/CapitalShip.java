package com.dragonrider.swrpgcompanion.CapitalShip;

import java.util.List;

/**
 * Created by mge637 on 04/11/2015.
 */
public class CapitalShip {

    private String ShipName;
    private List<Crew> Crew;


    public String getShipName() {
        return ShipName;
    }

    public CapitalShip setShipName(String shipName) {
        ShipName = shipName;
        return this;
    }

    public List<com.dragonrider.swrpgcompanion.CapitalShip.Crew> getCrew() {
        return Crew;
    }

    public CapitalShip setCrew(List<com.dragonrider.swrpgcompanion.CapitalShip.Crew> crew) {
        Crew = crew;
        return this;
    }
}
