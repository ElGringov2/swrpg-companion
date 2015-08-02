package com.dragonrider.swrpgcompanion.XWingWrapper;

import com.dragonrider.swrpgcompanion.Classes.NPC;

/**
 * Created by mge637 on 31/07/2015. Wrapper pour NPC
 */
public class CrewWrapper {
    /**
     * Le personnage servant de base
     */
    public NPC baseNPC;
    /**
     * D�finit si le personnage joue sur un slot PJ
     */
    public boolean isOnPlayerSlot;
    /**
     * D�finit si le personnage est un joueur
     */
    public boolean isPlayer;

    /**
     * CTOR
     * @param npc Le personnage de base
     * @param isOnPlayerSlot D�finit si le personnage agit en joueur et non en non-joueur
     * @param isPlayer D�finit si le personnage est un joueur
     */
    public CrewWrapper(NPC npc, boolean isOnPlayerSlot, boolean isPlayer) {
        baseNPC = npc;
        this.isOnPlayerSlot = isOnPlayerSlot;
        this.isPlayer = isPlayer;
    }

    /**
     * D�finit l'initiative du personnage
     */
    public Initiative initiative = new Initiative();
    /**
     * D�finit l'ordre de jeu (-1 = non jou�)
     */
    public int Played = -1;
}