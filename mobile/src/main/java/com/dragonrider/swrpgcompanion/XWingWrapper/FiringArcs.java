package com.dragonrider.swrpgcompanion.XWingWrapper;

/**
 * Created by mge637 on 28/02/2015.
 */
public class FiringArcs {
    private boolean Fore;
    private boolean Aft;
    private boolean Port;
    private boolean Starboard;
    private boolean Dorsal;
    private boolean Ventral;

    public boolean isFore() {
        return Fore;
    }

    public FiringArcs setFore(boolean fore) {
        Fore = fore;
        return this;
    }

    public boolean isAft() {
        return Aft;
    }

    public FiringArcs setAft(boolean aft) {
        Aft = aft;
        return this;
    }

    public boolean isPort() {
        return Port;
    }

    public FiringArcs setPort(boolean port) {
        Port = port;
        return this;
    }

    public boolean isStarboard() {
        return Starboard;
    }

    public FiringArcs setStarboard(boolean starboard) {
        Starboard = starboard;
        return this;
    }

    public boolean isDorsal() {
        return Dorsal;
    }

    public FiringArcs setDorsal(boolean dorsal) {
        Dorsal = dorsal;
        return this;
    }

    public boolean isVentral() {
        return Ventral;
    }

    public FiringArcs setVentral(boolean ventral) {
        Ventral = ventral;
        return this;
    }
}
