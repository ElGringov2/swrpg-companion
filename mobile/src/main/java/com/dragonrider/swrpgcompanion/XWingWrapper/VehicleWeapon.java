package com.dragonrider.swrpgcompanion.XWingWrapper;

import com.dragonrider.swrpgcompanion.Classes.App;
import com.dragonrider.swrpgcompanion.Classes.ItemQuality;
import com.dragonrider.swrpgcompanion.Classes.Weapon;
import com.dragonrider.swrpgcompanion.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by mge637 on 28/02/2015.
 * Gere les armes de véhicules
 */
public class VehicleWeapon {


    private ArrayList<ItemQuality> Qualities = new ArrayList<>();
    private FiringArcs FiringArcs;
    private boolean Turret;
    private int Count;
    private String Name;
    private int Damage;
    private int Critical;
    private boolean Used = false;


    @Override
    public String toString() {
        return String.format("%dx%s (%s %d, %s %d %s)",
                this.Count,
                this.Name,
                App.getContext().getString(R.string.weapon_damage)    ,
                this.Damage,
                App.getContext().getString(R.string.weapon_critical)    ,
                this.Critical,
                ItemQuality.getLightDescription(Qualities)
        );
    }

    public List<ItemQuality> getQualities() {
        return Qualities;
    }

    public VehicleWeapon setQualities(ArrayList<ItemQuality> qualities) {
        Qualities = qualities;
        return this;
    }

    public FiringArcs getFiringArcs() {
        return FiringArcs;
    }

    public VehicleWeapon setFiringArcs(FiringArcs firingArcs) {
        FiringArcs = firingArcs;
        return this;
    }

    public boolean isTurret() {
        return Turret;
    }

    public VehicleWeapon setTurret(boolean turret) {
        Turret = turret;
        return this;
    }

    public int getCount() {
        return Count;
    }

    public VehicleWeapon setCount(int count) {
        Count = count;
        return this;
    }

    public int getDamage() {
        return Damage;
    }

    public VehicleWeapon setDamage(int damage) {
        Damage = damage;
        return this;
    }

    public int getCritical() {
        return Critical;
    }

    public VehicleWeapon setCritical(int critical) {
        Critical = critical;
        return this;
    }

    public String getName() {
        return Name;
    }

    public VehicleWeapon setName(String name) {
        Name = name;
        return this;
    }

    public String toDatabaseString() {


        return String.format("#%s|%d|%d|%d|%d|%d|%d|%d|%d|%d|%d|%s",
                this.Name,
                this.FiringArcs.isAft() ? 1 : 0,
                this.FiringArcs.isFore() ? 1 : 0,
                this.FiringArcs.isPort() ? 1 : 0,
                this.FiringArcs.isStarboard() ? 1 : 0,
                this.FiringArcs.isDorsal() ? 1 : 0,
                this.FiringArcs.isVentral() ? 1 : 0,
                this.Turret ? 1 : 0,
                this.Count,
                this.Damage,
                this.Critical,
                ItemQuality.toDatabaseString(Qualities));

    }
    
    public static VehicleWeapon fromDatabaseString(String dbString) {
        String[] hopla = dbString.split("[|]");
        VehicleWeapon weapon = new VehicleWeapon();
        
        weapon.setName(hopla[0]);
        com.dragonrider.swrpgcompanion.XWingWrapper.FiringArcs arcs = new FiringArcs();
        arcs.setAft(hopla[1].equals("1"));
        arcs.setFore(hopla[2].equals("1"));
        arcs.setPort(hopla[3].equals("1"));
        arcs.setStarboard(hopla[4].equals("1"));
        arcs.setDorsal(hopla[5].equals("1"));
        arcs.setVentral(hopla[6].equals("1"));
        weapon.setFiringArcs(arcs);
        weapon.setTurret(hopla[7].equals("1"));
        weapon.setCount(Integer.valueOf(hopla[8]));
        weapon.setDamage(Integer.valueOf(hopla[9]));
        weapon.setCritical(Integer.valueOf(hopla[10]));
        weapon.setQualities(ItemQuality.fromDatabaseString(hopla[11]));


        if (weapon.Count == 0)
            weapon.Count = 1;
        return  weapon;
    }

    public void setBaseWeapon(Weapon weapon) {
        if (weapon != null) {
            this.Damage = weapon.Damage;
            this.Critical = weapon.CriticalValue;
            this.Name = weapon.Name;
        }
        else {
            this.Damage = 1;
            this.Critical = 99;
            this.Name = "";
        }
    }

    public boolean isUsed() {
        return Used;
    }

    public void setUsed(boolean used) {
        Used = used;
    }
}
