package com.dragonrider.swrpgcompanion.CapitalShip;

import com.dragonrider.swrpgcompanion.Classes.NPC;

/**
 * Created by mge637 on 04/11/2015.
 */
public class Crew {
    private NPC baseNPC;

    private String ActualOccupation;

    public NPC getBaseNPC() {
        return baseNPC;
    }

    public Crew setBaseNPC(NPC baseNPC) {
        this.baseNPC = baseNPC;
        return this;
    }

    public String getActualOccupation() {
        return ActualOccupation;
    }

    public Crew setActualOccupation(String actualOccupation) {
        ActualOccupation = actualOccupation;
        return this;
    }
}
