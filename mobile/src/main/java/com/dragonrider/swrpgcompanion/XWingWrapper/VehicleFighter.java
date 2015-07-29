package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;

import com.dragonrider.swrpgcompanion.Classes.Database;
import com.dragonrider.swrpgcompanion.Classes.NPC;
import com.dragonrider.swrpgcompanion.Classes.XmlImport;
import com.google.android.gms.games.Player;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mge637 on 01/03/2015.
 * Gere les combats spatiaux
 */
public class VehicleFighter {
    private int ActualHullTrauma;
    private int ActualSystemStrain;
    private int ActualDefenseFore;
    private int ActualDefenseAft;
    private int ActualDefensePort;
    private int ActualDefenseStarboard;

    private Vehicle baseVehicle;


    private List<CrewWrapper> crew = new ArrayList<>();



    public static VehicleFighter getFighterFromJSON(JsonReader reader, Context context, List<NPC> PlayerList) throws IOException {
        Database db = new Database(context);

        VehicleFighter fighter = new VehicleFighter();

        reader.beginObject();
        while (reader.hasNext()) {

            String name = reader.nextName();
            if (name.startsWith("crew")) {
                reader.beginObject();
                CrewWrapper wrapper = new CrewWrapper();
                while (reader.hasNext()) {
                    switch (reader.nextName()) {
                        case "IsPlayer":
                            wrapper.IsPlayer = reader.nextBoolean();
                            break;
                        case "PlayerName":
                            String PlayerName = reader.nextString();
                            for (NPC npc : PlayerList)
                                if (npc.OwnerName.equals(PlayerName)) {
                                    wrapper.baseNPC = npc;
                                    PlayerList.remove(npc);
                                    break;
                            }
                            break;
                        case "NPC":
                            wrapper.baseNPC = db.GetNPCbyID(reader.nextInt());
                            break;
                    }
                }
                fighter.getCrew().add(wrapper);
                reader.endObject();


            }
            else {
                switch (name) {
                    case "ActualHullTrauma":
                        fighter.ActualHullTrauma = reader.nextInt();
                        break;
                    case "ActualSystemStrain":
                        fighter.ActualSystemStrain = reader.nextInt();
                        break;
                    case "ActualDefenseFore":
                        fighter.ActualDefenseFore = reader.nextInt();
                        break;
                    case "ActualDefenseAft":
                        fighter.ActualDefenseAft = reader.nextInt();
                        break;
                    case "ActualDefensePort":
                        fighter.ActualDefensePort = reader.nextInt();
                        break;
                    case "ActualDefenseStarboard":
                        fighter.ActualDefenseStarboard = reader.nextInt();
                        break;
                    case "baseVehicle":
                        fighter.setBaseVehicle(db.getVehicleByID(reader.nextInt()));
                        break;
                }
            }

        }
        reader.endObject();


        return fighter;


    }


    public void saveJSON(JsonWriter writer) throws IOException {

        writer.beginObject();
        writer.name("ActualHullTrauma").value(ActualHullTrauma);
        writer.name("ActualSystemStrain").value(ActualHullTrauma);
        writer.name("ActualDefenseFore").value(ActualHullTrauma);
        writer.name("ActualDefenseAft").value(ActualHullTrauma);
        writer.name("ActualDefensePort").value(ActualHullTrauma);
        writer.name("ActualDefenseStarboard").value(ActualHullTrauma);
        writer.name("baseVehicle").value(baseVehicle.getDatabaseID());

        for (CrewWrapper crewWrapper : crew)
        {
            writer.name("crew" + String.valueOf(crew.indexOf(crewWrapper)));
            writer.beginObject();
            writer.name("IsPlayer").value(crewWrapper.IsPlayer);
            if (crewWrapper.IsPlayer)
                writer.name("PlayerName").value(crewWrapper.baseNPC.OwnerName);
            else
                writer.name("NPC").value(crewWrapper.baseNPC.DatabaseID);
            writer.endObject();
        }


        writer.endObject();

    }

    public static class CrewWrapper {
        public NPC baseNPC;
        public boolean IsPlayer;
        public CrewWrapper(){}
        public CrewWrapper(NPC npc, boolean isPlayer) {
            baseNPC = npc;
            IsPlayer = isPlayer;
        }
    }

    private VehicleFighter () {

    }

    public VehicleFighter(Vehicle baseVehicle)
    {

        setBaseVehicle(baseVehicle);


    }



    public int getActualHullTrauma() {
        return ActualHullTrauma;
    }

    public VehicleFighter setActualHullTrauma(int actualHullTrauma) {
        ActualHullTrauma = actualHullTrauma;
        return this;

    }

    public int getActualSystemStrain() {
        return ActualSystemStrain;
    }

    public VehicleFighter setActualSystemStrain(int actualSystemStrain) {
        ActualSystemStrain = actualSystemStrain;
        return this;

    }

    public int getActualDefenseFore() {
        return ActualDefenseFore;
    }

    public VehicleFighter setActualDefenseFore(int actualDefenseFore) {
        ActualDefenseFore = actualDefenseFore;
        return this;

    }

    public int getActualDefenseAft() {
        return ActualDefenseAft;

    }

    public VehicleFighter setActualDefenseAft(int actualDefenseAft) {
        ActualDefenseAft = actualDefenseAft;
        return this;

    }

    public int getActualDefensePort() {
        return ActualDefensePort;
    }

    public VehicleFighter setActualDefensePort(int actualDefensePort) {
        ActualDefensePort = actualDefensePort;
        return this;

    }

    public int getActualDefenseStarboard() {
        return ActualDefenseStarboard;
    }

    public VehicleFighter setActualDefenseStarboard(int actualDefenseStarboard) {
        ActualDefenseStarboard = actualDefenseStarboard;
        return this;

    }

    public Vehicle getBaseVehicle() {
        return baseVehicle;

    }

    public VehicleFighter setBaseVehicle(Vehicle baseVehicle) {
        this.baseVehicle = baseVehicle;
        this.ActualHullTrauma = baseVehicle.getHullTrauma();
        this.ActualSystemStrain = baseVehicle.getSystemStrain();
        this.ActualDefenseAft = baseVehicle.getDefAft();
        this.ActualDefenseFore = baseVehicle.getDefFore();
        this.ActualDefensePort = baseVehicle.getDefPort();
        this.ActualDefenseStarboard = baseVehicle.getDefStarboard();
        return this;
    }


    public int getTotalHullTrauma() {
        if (baseVehicle == null) return 0;
        return baseVehicle.getHullTrauma();
    }

    public int getTotalSystemStrain() {
        if (baseVehicle == null) return 0;
        return baseVehicle.getSystemStrain();
    }

    public void addCrew(NPC npc, boolean isPlayer) {

        crew.add(new CrewWrapper(npc, isPlayer));
    }

    public List<CrewWrapper> getCrew() {
        return crew;
    }

    public void removeCrew(CrewWrapper npc) {
        crew.remove(npc);
    }
}
