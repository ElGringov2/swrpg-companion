package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.content.Context;
import android.util.JsonReader;
import android.util.JsonWriter;

import com.dragonrider.swrpgcompanion.Classes.Database;
import com.dragonrider.swrpgcompanion.Classes.InitiativeAdapter;
import com.dragonrider.swrpgcompanion.Classes.NPC;
import com.dragonrider.swrpgcompanion.Classes.RollResult;
import com.dragonrider.swrpgcompanion.Classes.Skill;


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

    private int ActualShieldEnergy = 1;
    private int ActualEngineEnergy = 1;
    private int ActualWeaponsEnergy = 1;

    private Vehicle baseVehicle;

    private int selectedManeuver = -1;
    private int selectedSpeed = -1;


    private List<CrewWrapper> crew = new ArrayList<>();



    public static VehicleFighter getFighterFromJSON(JsonReader reader, Context context, List<NPC> PlayerList) throws IOException {
        Database db = new Database(context);

        VehicleFighter fighter = new VehicleFighter();

        reader.beginObject();
        while (reader.hasNext()) {

            String name = reader.nextName();
            if (name.startsWith("crew")) {
                reader.beginObject();
                final CrewWrapper wrapper = new CrewWrapper(null, false, false);
                while (reader.hasNext()) {
                    switch (reader.nextName()) {
                        case "isOnPlayerSlot":
                            wrapper.isOnPlayerSlot = reader.nextBoolean();
                            break;
                        case "PlayerName":
                            String PlayerName = reader.nextString();
                            for (NPC npc : PlayerList)
                                if (npc.OwnerName.equals(PlayerName)) {
                                    wrapper.baseNPC = npc;
                                    wrapper.isPlayer = true;
                                    PlayerList.remove(npc);

                                    break;
                                }
                            break;
                        case "NPC":
                            wrapper.baseNPC = db.GetNPCbyID(reader.nextInt());
                            RollResult rr = wrapper.baseNPC.getSkillRoll(Skill.Skills.cool);

                            wrapper.initiative = new Initiative();
                            wrapper.initiative.Advantages = rr.Advantage();
                            wrapper.initiative.Success = rr.Succcess();
                            wrapper.initiative.Triumph = rr.Triumph();
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
            writer.name("isOnPlayerSlot").value(crewWrapper.isOnPlayerSlot);
            if (crewWrapper.isPlayer)
                writer.name("PlayerName").value(crewWrapper.baseNPC.OwnerName);
            else
                writer.name("NPC").value(crewWrapper.baseNPC.DatabaseID);
            writer.endObject();
        }


        writer.endObject();

    }

    public int getActualShieldEnergy() {
        if (baseVehicle.getDefStarboard() + baseVehicle.getDefPort() + baseVehicle.getDefFore() + baseVehicle.getDefAft() == 0)
            return 0;
        return ActualShieldEnergy;
    }

    public void setActualShieldEnergy(int actualShieldEnergy) {
        if (baseVehicle.getDefStarboard() + baseVehicle.getDefPort() + baseVehicle.getDefFore() + baseVehicle.getDefAft() == 0)
            return;
        ActualShieldEnergy = actualShieldEnergy;
    }

    public int getActualEngineEnergy() {
        return ActualEngineEnergy;
    }

    public void setActualEngineEnergy(int actualEngineEnergy) {
        ActualEngineEnergy = actualEngineEnergy;
    }

    public int getActualWeaponsEnergy() {
        if (baseVehicle.getWeapons().size() == 0) return 0;
        return ActualWeaponsEnergy;
    }

    public void setActualWeaponsEnergy(int actualWeaponsEnergy) {
        if (baseVehicle.getWeapons().size() == 0) return;
        ActualWeaponsEnergy = actualWeaponsEnergy;
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


    public int getActualDefenseAft() {
        return ActualDefenseAft;

    }

    public int getBaseDefenseAft() {
        return 3 + (baseVehicle.getDefAft() * 2 );
    }

    public int getMaxDefenseAft() {
        return getBaseDefenseAft() * 2;
    }


    public int getBaseDefenseFore() {
        return 3 + (baseVehicle.getDefFore() * 2 );
    }

    public int getMaxDefenseFore() {
        return getBaseDefenseFore() * 2;
    }


    public int getBaseDefensePort() {
        return 3 + (baseVehicle.getDefPort() * 2 );
    }

    public int getMaxDefensePort() {
        return getBaseDefensePort() * 2;
    }

    public int getBaseDefenseStarboard() {
        return 3 + (baseVehicle.getDefStarboard() * 2 );
    }

    public int getMaxDefenseStarboard() {
        return getBaseDefenseStarboard() * 2;
    }




    public VehicleFighter setActualDefenseFore(int actualDefenseFore) {
        if (actualDefenseFore < 0) actualDefenseFore = 0;
        if (actualDefenseFore > getMaxDefenseFore()) actualDefenseFore = getMaxDefenseFore();
        ActualDefenseFore = actualDefenseFore;
        return this;

    }

    public int getActualDefenseFore() {
        return ActualDefenseFore;
    }

    public VehicleFighter setActualDefenseAft(int actualDefenseAft) {
        if (actualDefenseAft < 0) actualDefenseAft = 0;
        if (actualDefenseAft > getMaxDefenseAft()) actualDefenseAft= getMaxDefenseAft();
        ActualDefenseAft = actualDefenseAft;
        return this;

    }

    public int getActualDefensePort() {
        return ActualDefensePort;
    }

    public VehicleFighter setActualDefensePort(int actualDefensePort) {
        if (actualDefensePort < 0) actualDefensePort = 0;
        if (actualDefensePort > getMaxDefensePort()) actualDefensePort = getMaxDefensePort();
        ActualDefensePort = actualDefensePort;
        return this;

    }

    public int getActualDefenseStarboard() {
        return ActualDefenseStarboard;
    }

    public VehicleFighter setActualDefenseStarboard(int actualDefenseStarboard) {
        if (actualDefenseStarboard < 0) actualDefenseStarboard = 0;
        if (actualDefenseStarboard > getMaxDefenseStarboard()) actualDefenseStarboard = getMaxDefenseStarboard();
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
        this.ActualDefenseAft = baseVehicle.getDefAft() > 0 ? getBaseDefenseAft() : 0;
        this.ActualDefenseFore = baseVehicle.getDefFore() > 0 ? getBaseDefenseFore() : 0;
        this.ActualDefensePort = baseVehicle.getDefPort() > 0 ? getBaseDefensePort() : 0;
        this.ActualDefenseStarboard = baseVehicle.getDefStarboard() > 0 ? getBaseDefenseStarboard() : 0;
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

    public CrewWrapper addCrew(NPC npc, boolean isPlayer) {

        CrewWrapper crewWrapper = new CrewWrapper(npc, isPlayer, isPlayer);

        if (!isPlayer) {
            //Initiative

            RollResult rr = npc.getSkillRoll(Skill.Skills.cool);

            crewWrapper.initiative = new Initiative();
            crewWrapper.initiative.Advantages = rr.Advantage();
            crewWrapper.initiative.Success = rr.Succcess();
            crewWrapper.initiative.Triumph = rr.Triumph();
        }

        for (CrewWrapper wrapper : getCrew()) {
            if (wrapper.isPlayer)
                crewWrapper.isOnPlayerSlot = true;
        }

        crew.add(crewWrapper);

        return crewWrapper;
    }

    public List<CrewWrapper> getCrew() {
        return crew;
    }

    public void removeCrew(CrewWrapper npc) {
        crew.remove(npc);
    }





    @Override
    public String toString() {
        return String.format("%s (%s)\n[Coque %d Stress %d] [ARM %d BOU %d MOT %d]",
                this.getBaseVehicle().getName(),
                this.getBaseVehicle().getType(),
                this.getActualHullTrauma(),
                this.getActualSystemStrain(),
                this.getActualWeaponsEnergy(),
                this.getActualShieldEnergy(),
                this.getActualEngineEnergy());
    }


    public VehicleFighter Clone() {
        VehicleFighter clone = new VehicleFighter(this.baseVehicle);

        clone.ActualDefenseAft = this.ActualDefenseAft;
        clone.ActualDefenseFore = this.ActualDefenseFore;
        clone.ActualDefensePort = this.ActualDefensePort;
        clone.ActualDefenseStarboard = this.ActualDefenseStarboard;
        clone.ActualEngineEnergy = this.ActualEngineEnergy;
        clone.ActualShieldEnergy = this.ActualShieldEnergy;
        clone.ActualWeaponsEnergy = this.ActualWeaponsEnergy;

        clone.ActualSystemStrain = this.ActualSystemStrain;
        clone.ActualHullTrauma = this.ActualHullTrauma;

        for (CrewWrapper wrapper : getCrew()) {
            if (!wrapper.isPlayer)
            {
                CrewWrapper cloneWrapper = clone.addCrew(wrapper.baseNPC, false);

                RollResult rr = wrapper.baseNPC.getSkillRoll(Skill.Skills.cool);

                cloneWrapper.initiative = new Initiative();
                cloneWrapper.initiative.Advantages = rr.Advantage();
                cloneWrapper.initiative.Success = rr.Succcess();
                cloneWrapper.initiative.Triumph = rr.Triumph();


            }
        }

        return clone;
    }

    public int getSelectedManeuver() {
        return selectedManeuver;
    }

    public void setSelectedManeuver(int selectedManeveur) {
        this.selectedManeuver = selectedManeveur;
    }

    public int getSelectedSpeed() {
        return selectedSpeed;
    }

    public void setSelectedSpeed(int selectedSpeed) {
        this.selectedSpeed = selectedSpeed;
    }
}
