package com.dragonrider.swrpgcompanion.Classes;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.dragonrider.swrpgcompanion.R;
import com.dragonrider.swrpgcompanion.Scenario.FightScenarioItem;
import com.dragonrider.swrpgcompanion.Scenario.ScenarioItem;
import com.dragonrider.swrpgcompanion.Scenario.TextScenarioItem;
import com.dragonrider.swrpgcompanion.XWingWrapper.FiringArcs;
import com.dragonrider.swrpgcompanion.XWingWrapper.Vehicle;
import com.dragonrider.swrpgcompanion.XWingWrapper.VehicleWeapon;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


public class XmlImport {


    private static String getContent(XmlPullParser xpp) throws IOException, XmlPullParserException {
        int eventType = xpp.next();
        if (eventType == XmlPullParser.TEXT)
            return xpp.getText();
        return "";
    }

    private static int getIntegerContent(XmlPullParser xpp) throws IOException, XmlPullParserException {
        int eventType = xpp.next();
        if (eventType == XmlPullParser.TEXT) {
            String s = xpp.getText();
            s = s.trim();
            s = s.replace("\n", "");
            return Integer.valueOf(s);
        }

        return -1;
    }

    private static List<Integer> getCharacteristics(XmlPullParser xpp) throws IOException, XmlPullParserException {
        List<Integer> returnList = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));

        Dictionary<String, Integer> dict = new Hashtable<>();
        dict.put("BR", Characteristic.Characteristics.Brawn.ordinal());
        dict.put("AG", Characteristic.Characteristics.Agility.ordinal());
        dict.put("INT", Characteristic.Characteristics.Intellect.ordinal());
        dict.put("CUN", Characteristic.Characteristics.Cunning.ordinal());
        dict.put("WIL", Characteristic.Characteristics.Willpower.ordinal());
        dict.put("PR", Characteristic.Characteristics.Presence.ordinal());


        int eventType = xpp.next();

        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("Characteristics"))) {
            if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("CharCharacteristic")) {
                Map.Entry<String, Integer> value = GetCharCharacteristic(xpp);
                returnList.set(dict.get(value.getKey()), value.getValue());
            }
            eventType = xpp.next();
        }


        return returnList;
    }

    private static Map.Entry<String, Integer> GetCharCharacteristic(XmlPullParser xpp) throws IOException, XmlPullParserException {
        int rank = 0;
        String key = "";

        int eventType = xpp.next();


        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("CharCharacteristic"))) {

            if (xpp.getName() == null) {
                eventType = xpp.next();
                continue;
            }

            if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("Key"))
                key = getContent(xpp);
            else if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("Rank"))
                rank = getRank(xpp);


            eventType = xpp.next();
        }


        return new AbstractMap.SimpleEntry<>(key, rank);
    }

    private static int getRank(XmlPullParser xpp) throws IOException, XmlPullParserException {
        return getRank(xpp, "Rank");
    }

    public static int parseWithDefault(String number) {
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    private static int getRank(XmlPullParser xpp, String defaultTag) throws IOException, XmlPullParserException {
        int iRank = 0;

        int eventType = xpp.next();

        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals(defaultTag))) {
            if (eventType == XmlPullParser.START_TAG)
                iRank += parseWithDefault(getContent(xpp));

            eventType = xpp.next();

        }

        return iRank;
    }

    private static List<Talent> getTalents(XmlPullParser xpp, Hashtable<String, Integer> talentList) throws IOException, XmlPullParserException {
        List<Talent> talents = new ArrayList<>();

        int eventType = xpp.next();

        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("Talents"))) {
            if (eventType == XmlPullParser.START_TAG) {

                Map.Entry<String, Integer> talent = getTalent(xpp);

                Talent localTalent = new Talent();
                if (talentList.containsKey(talent.getKey()))
                    localTalent.TalentID = talentList.get(talent.getKey());
                else
                    Log.i("hopla", "********************************************************** manque " + talent.getKey());
                localTalent.TalentValue = talent.getValue();

                boolean addTalent = true;
                for (Talent t : talents)
                    if (t.TalentID == localTalent.TalentID) {
                        t.setTalentID(t.TalentID + localTalent.TalentValue);
                        addTalent = false;
                    }

                if (addTalent)
                    talents.add(localTalent);

            }

            eventType = xpp.next();
        }


        return talents;
    }


    private static Map.Entry<String, Integer> getTalent(XmlPullParser xpp) throws IOException, XmlPullParserException {
        String key = "";
        int rank = 0;


        int eventType = xpp.next();

        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("CharTalent"))) {

            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Key"))
                key = getContent(xpp);
            else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Ranks"))
                rank = Integer.valueOf(getContent(xpp));


            eventType = xpp.next();
        }

        return new AbstractMap.SimpleEntry<>(key, rank);
    }


    private static List<Skill> getSkills(XmlPullParser xpp, Hashtable<String, Integer> skillList) throws IOException, XmlPullParserException {
        List<Skill> skills = new ArrayList<>();

        int eventType = xpp.next();

        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("Skills"))) {
            if (eventType == XmlPullParser.START_TAG) {

                Map.Entry<String, Integer> skill = getSkill(xpp);

                Skill localSkill = Skill.getNewSkill(skillList.get(skill.getKey()));
                localSkill.Value = skill.getValue();

                skills.add(localSkill);

            }

            eventType = xpp.next();
        }


        return skills;
    }


    private static Map.Entry<String, Integer> getSkill(XmlPullParser xpp) throws IOException, XmlPullParserException {
        String key = "";
        int rank = 0;


        int eventType = xpp.next();

        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("CharSkill"))) {

            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Key"))
                key = getContent(xpp);
            else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Rank"))
                rank = getRank(xpp);


            eventType = xpp.next();
        }

        return new AbstractMap.SimpleEntry<>(key, rank);
    }


    public static String getFileType(String Filename) {

        if (!Filename.toLowerCase().endsWith("xml"))
            return null;


        XmlPullParserFactory factory;
        Log.i("hopla", "Verification du fichier " + Filename);

        try {

            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();


            xpp.setInput(new FileReader(Filename));


            int eventType = xpp.getEventType();


            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG && xpp.getName() != null) {

                    Log.i("hopla", xpp.getName());
                    return xpp.getName();

                }


                eventType = xpp.next();
            }
        } catch (Exception e) {
            Log.i("XmlImportFileType", "Erreur dans le fichier" + Filename);
            e.printStackTrace();
        }

        return null;
    }


    public static NPC ImportAdversary(String Filename,
                                      Hashtable<String, Integer> TalentList,
                                      Hashtable<String, Integer> SkillList,
                                      Hashtable<String, Weapon> WeaponList,
                                      Hashtable<String, Integer> ItemQualityList,
                                      Hashtable<String, ForcePower> forcePowerList) {
        if (getFileType(Filename).equals("Adversary"))
            return ImportNPC(Filename, TalentList, SkillList, WeaponList, ItemQualityList, forcePowerList);
        else
            return null;


    }

    private static NPC ImportNPC(String Filename,
                                      Hashtable<String, Integer> TalentList,
                                      Hashtable<String, Integer> SkillList,
                                      Hashtable<String, Weapon> WeaponList,
                                      Hashtable<String, Integer> ItemQualityList,
                                      Hashtable<String, ForcePower> forcePowerList) {


        XmlPullParserFactory factory;


        NPC npc = new NPC();


        try {

            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();


            xpp.setInput(new FileReader(Filename));


            int eventType = xpp.getEventType();

            String sTagName;

            while (eventType != XmlPullParser.END_DOCUMENT) {




                if (eventType == XmlPullParser.START_TAG) {
                    sTagName = xpp.getName();


                    if (sTagName.equals("Key") && npc.Key.isEmpty()) {
                        npc.Key = getContent(xpp);
                        eventType = xpp.next();
                        continue;
                    }

                    if (sTagName.equals("Name") && npc.Name.isEmpty()) {
                        npc.Name = getContent(xpp);
                        eventType = xpp.next();
                        continue;
                    }
                    if (sTagName.equals("Description") && npc.Description.isEmpty()) {
                        npc.Description = getContent(xpp);
                        eventType = xpp.next();
                        continue;
                    }
                    if (sTagName.equals("Category") && npc.Category.isEmpty()) {
                        npc.Category = getContent(xpp);
                        eventType = xpp.next();
                        continue;
                    }
                    if (sTagName.equals("Characteristics")) {
                        npc.Characteristics = getCharacteristics(xpp);
                        eventType = xpp.next();
                        continue;
                    }
                    if (sTagName.equals("Talents")) {
                        npc.Talents = getTalents(xpp, TalentList);
                        eventType = xpp.next();
                        continue;
                    }
                    if (sTagName.equals("ForceAbilities")) {
                        npc.ForcePowers = getForcePowers(xpp, forcePowerList);
                        eventType = xpp.next();
                        continue;
                    }
                    if (sTagName.equals("Skills")) {
                        npc.Skills = getSkills(xpp, SkillList);
                        eventType = xpp.next();
                        continue;
                    }
                    if (sTagName.equals("UsedExperience")) {
                        npc.Experience = Integer.parseInt(getContent(xpp));
                        eventType = xpp.next();
                        continue;
                    }
                    if (sTagName.equals("SoakValue")) {
                        npc.Soak = getRank(xpp, sTagName);
                        eventType = xpp.next();
                        continue;
                    }
                    if (sTagName.equals("WoundThreshold")) {
                        npc.TotalWounds = getRank(xpp, sTagName);
                        eventType = xpp.next();
                        continue;
                    }
                    if (sTagName.equals("StrainThreshold")) {
                        npc.TotalStrain = getRank(xpp, sTagName);
                        eventType = xpp.next();
                        continue;
                    }
                    if (sTagName.equals("DefenseRanged")) {
                        npc.RangedDefense = getRank(xpp, sTagName);
                        eventType = xpp.next();
                        continue;
                    }
                    if (sTagName.equals("DefenseMelee")) {
                        npc.MeleeDefense = getRank(xpp, sTagName);
                        eventType = xpp.next();
                        continue;
                    }
                    if (sTagName.equals("ForceRating")) {
                        npc.ForceRating = getRank(xpp, sTagName);
                        eventType = xpp.next();
                        continue;
                    }
                    if (sTagName.equals("Type")) {
                        String type = getContent(xpp);
                        npc.Type = NPC.NPCTypes.valueOf(type);


                        eventType = xpp.next();
                        continue;
                    }
                    if (sTagName.equals("Portrait")) {
                        String data = getContent(xpp);
                        byte[] dataArray = Base64.decode(data, Base64.DEFAULT);
                        npc.setImage(dataArray);
                        eventType = xpp.next();
                        continue;
                    }
                    if (sTagName.equals("Weapons")) {
                        npc.Items.addAll(getCharWeapons(xpp, WeaponList));
                    }

                    if (sTagName.equals("SpecialWeapons")) {
                        npc.Items.addAll(getSpecialWeapons(xpp, SkillList, ItemQualityList));
                    }
                    //TODO Gear
                }



                eventType = xpp.next();
            }


        } catch (XmlPullParserException e) {
            Log.i("hopla2", "Erreur sur le fichier" + Filename + ".");
            e.printStackTrace();
            return null;
        } catch (Exception e) {
            Log.i("hopla2", "Fichier " + Filename + " erroné");
            e.printStackTrace();
            return null;
        }

        if (npc.Type == NPC.NPCTypes.Minion)
            for (Skill sk : npc.Skills)
                sk.setValue(1);

        return npc;
    }


    private static ArrayList<Weapon> getSpecialWeapons(XmlPullParser xpp, Hashtable<String, Integer> SkillList, Hashtable<String, Integer> ItemQualityList) throws IOException, XmlPullParserException {
        ArrayList<Weapon> weapons = new ArrayList<>();

        int eventType = xpp.next();

        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("SpecialWeapons"))) {

            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("AdvWeapon"))
                weapons.add(getSpecialWeapon(xpp, SkillList, ItemQualityList));

            eventType = xpp.next();
        }


        return weapons;
    }

    private static Weapon getSpecialWeapon(XmlPullParser xpp, Hashtable<String, Integer> SkillList, Hashtable<String, Integer> ItemQualityList) throws IOException, XmlPullParserException {
        int eventType = xpp.next();
        Weapon wp = new Weapon();

        while (!(eventType == XmlPullParser.END_TAG && xpp.getName() != null && xpp.getName().equals("AdvWeapon"))) {


            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Key"))
                wp.Key = getContent(xpp);
            else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Name"))
                wp.Name = getContent(xpp);
            else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("SkillKey"))
                wp.SkillID = SkillList.get(getContent(xpp));
            else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Damage"))
                wp.Damage = Integer.parseInt(getContent(xpp));
            else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Crit"))
                wp.CriticalValue = Integer.parseInt(getContent(xpp));
            else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Range")) {
                String sRange = getContent(xpp);
                if (sRange.equals("Engaged")) wp.RangeID = 0;
                if (sRange.equals("Short")) wp.RangeID = 1;
                if (sRange.equals("Medium")) wp.RangeID = 2;
                if (sRange.equals("Long")) wp.RangeID = 3;
                if (sRange.equals("Extreme")) wp.RangeID = 4;
            } else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Encumbrance"))
                wp.Encumbrance = Integer.parseInt(getContent(xpp));
            else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("HP"))
                wp.HardPoints = Integer.parseInt(getContent(xpp));
            else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Price"))
                wp.Price = Integer.parseInt(getContent(xpp));
            else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Type"))
                wp.Category = getContent(xpp);
            else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Descriptors")) {
                wp.Qualities = (ArrayList<ItemQuality>) getQualities(xpp, ItemQualityList, "Descriptors", "Descriptor");
            }


            eventType = xpp.next();

        }

        return wp;
    }

    private static ArrayList<Weapon> getCharWeapons(XmlPullParser xpp, Hashtable<String, Weapon> weaponList) throws IOException, XmlPullParserException {
        ArrayList<Weapon> weapons = new ArrayList<>();

        int eventType = xpp.next();

        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("Weapons"))) {

            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("CharWeapon"))
                weapons.add(weaponList.get(getCharWeaponKey(xpp)));

            eventType = xpp.next();
        }


        return weapons;
    }



    private static String getCharWeaponKey(XmlPullParser xpp) throws IOException, XmlPullParserException {
        String key = "";


        int eventType = xpp.next();

        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("CharWeapon"))) {

            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("ItemKey"))
                key = getContent(xpp);

            eventType = xpp.next();
        }

        return key;
    }

    private static List<ForcePower> getForcePowers(XmlPullParser xpp, Hashtable<String, ForcePower> forcePowerList) throws IOException, XmlPullParserException {
        int eventType = xpp.next();
        List<ForcePower> forces = new ArrayList<>();

        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("ForceAbilities"))) {

            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("CharForceAbility"))
            {
                ForcePower pow = getCharForcePower(xpp, forcePowerList);
                if (pow != null) forces.add(pow);
            }

            eventType = xpp.next();
        }

        return forces;
    }

    private static ForcePower getCharForcePower(XmlPullParser xpp, Hashtable<String, ForcePower> forcePowerList) throws IOException, XmlPullParserException {
        ForcePower power = new ForcePower();



        int eventType = xpp.next();

        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("CharForceAbility"))) {

            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Key"))
            {
                String key = getContent(xpp);
                ForcePower tempForcePower = forcePowerList.get(key);
                if (tempForcePower == null) {

                    power.Power = key;

                }
                else {
                    power.Power = tempForcePower.Power;
                    power.Type = tempForcePower.Type;
                    power.Description = tempForcePower.Description;
                    power.Name = tempForcePower.Name;
                }

            }
            else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Upgrades"))
                power.Upgrades = Integer.valueOf(getContent(xpp));


            eventType = xpp.next();
        }

        return power;
    }


    public static Hashtable<String, Integer> getDataTalents() {
        File sdCardRoot = Environment.getExternalStorageDirectory();
        File file = new File(sdCardRoot, "SWEotE/Data/Talents.xml");

        Hashtable<String, Integer> hash = new Hashtable<>();

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();


            xpp.setInput(new FileReader(file));


            int eventType = xpp.getEventType();


            while (!(eventType == XmlPullParser.END_TAG && xpp.getName() != null && xpp.getName().equals("Talents"))) {
                String key;
                String name;
                if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Talent")) {


                    key = "";
                    name = "";

                    eventType = xpp.next();
                    while (!(eventType == XmlPullParser.END_TAG && xpp.getName() != null && xpp.getName().equals("Talent"))) {

                        if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Key"))
                            key = getContent(xpp);
                        else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Name"))
                            name = getContent(xpp);

                        eventType = xpp.next();
                    }

                    String talentCompare = name.toLowerCase().replace("-", "_").replace(" ", "_").replace("'", "").replace("(", "").replace(")", "").replace("!", "");



                    int id = Talent.TalentIDs.valueOf(talentCompare).ordinal();

                    hash.put(key, id);

                }


                eventType = xpp.next();

            }


        } catch (Exception e) {
            e.printStackTrace();

        }


        return hash;
    }


    public static Hashtable<String, Integer> getDataSkills() {
        File sdCardRoot = Environment.getExternalStorageDirectory();
        File file = new File(sdCardRoot, "SWEotE/Data/Skills.xml");

        Hashtable<String, Integer> hash = new Hashtable<>();

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();


            xpp.setInput(new FileReader(file));


            int eventType = xpp.getEventType();


            while (!(eventType == XmlPullParser.END_TAG && xpp.getName() != null && xpp.getName().equals("Skills"))) {
                String key;
                String name;
                if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Skill")) {


                    key = "";
                    name = "";

                    eventType = xpp.next();
                    while (!(eventType == XmlPullParser.END_TAG && xpp.getName() != null && xpp.getName().equals("Skill"))) {

                        if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Key"))
                            key = getContent(xpp);
                        else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Name"))
                            name = getContent(xpp);

                        eventType = xpp.next();
                    }

                    String skillCompare = name.toLowerCase().replace(" - ", "_").replace(" ", "_");



                    int id = Skill.Skills.valueOf(skillCompare).ordinal();

                    hash.put(key, id);

                }


                eventType = xpp.next();

            }


        } catch (Exception e) {
            e.printStackTrace();

        }


        return hash;
    }


    public static Hashtable<String, Integer> getDataItemQualities() {
        File sdCardRoot = Environment.getExternalStorageDirectory();
        File file = new File(sdCardRoot, "SWEotE/Data/ItemDescriptors.xml");

        Hashtable<String, Integer> hash = new Hashtable<>();

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();


            xpp.setInput(new FileReader(file));


            int eventType = xpp.getEventType();


            while (!(eventType == XmlPullParser.END_TAG && xpp.getName() != null && xpp.getName().equals("ItemDescriptors"))) {
                String key;
                String name;
                if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("ItemDescriptor")) {


                    key = "";
                    name = "";
                    boolean ignore = true;

                    eventType = xpp.next();
                    while (!(eventType == XmlPullParser.END_TAG && xpp.getName() != null && xpp.getName().equals("ItemDescriptor"))) {

                        if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Key"))
                            key = getContent(xpp);
                        else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Name"))
                            name = getContent(xpp);
                        else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("IsQuality"))
                            ignore = !getContent(xpp).equals("true");

                        eventType = xpp.next();
                    }

                    if (!ignore) {
                        String ItemQualityCompare = name.toLowerCase().toLowerCase().replace(")", "").replace("(", "").replace(" ", "_").replace("-", "").replace("_quality", "");



                        int id = ItemQuality.QualityList.valueOf(ItemQualityCompare).ordinal();

                        hash.put(key, id);
                    }
                }


                eventType = xpp.next();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return hash;
    }

    public static Hashtable<String, ForcePower> getDataForcePowers() {
        File sdCardRoot = Environment.getExternalStorageDirectory();
        File file = new File(sdCardRoot, "SWEotE/Data/Force Abilities.xml");

        Hashtable<String, ForcePower> hash = new Hashtable<>();

        try {

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();


            xpp.setInput(new FileReader(file));


            int eventType = xpp.getEventType();


            while (!(eventType == XmlPullParser.END_TAG && xpp.getName() != null && xpp.getName().equals("ForceAbilities"))) {


                if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("ForceAbility")) {


                    ForcePower power = new ForcePower();
                    String key = "";

                    eventType = xpp.next();
                    while (!(eventType == XmlPullParser.END_TAG && xpp.getName() != null && xpp.getName().equals("ForceAbility"))) {

                        if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Key"))
                        {
                            key = getContent(xpp);
                            if (key.contains("DURATION"))
                                power.Type = ForcePower.ForceType.duration;
                            else if (key.contains("STRENGTH"))
                                power.Type = ForcePower.ForceType.strength;
                            else if (key.contains("MAGNITUDE"))
                                power.Type = ForcePower.ForceType.magnitude;
                            else if (key.contains("RANGE"))
                                power.Type = ForcePower.ForceType.range;
                            else if (key.contains("BASIC"))
                                power.Type = ForcePower.ForceType.basic;
                            else if (key.contains("CONTROL"))
                                power.Type = ForcePower.ForceType.control;
                        }
                        else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Name"))
                        {
                            power.Name = getContent(xpp);
                            if (power.Name.contains("-"))
                                power.Name = power.Name.split("[-]")[0];
                        }
                        else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Description"))
                            power.Description = getContent(xpp);
                        else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Power"))
                            power.Power = getContent(xpp);




                        eventType = xpp.next();
                    }

                    hash.put(key, power);

                }


                eventType = xpp.next();

            }


        } catch (Exception e) {
            e.printStackTrace();
        }


        return hash;
    }


    public static Hashtable<String, Weapon> getDataWeapons(Hashtable<String, Integer> SkillList, Hashtable<String, Integer> ItemQualityList) {
        File sdCardRoot = Environment.getExternalStorageDirectory();
        File file = new File(sdCardRoot, "SWEotE/Data/Weapons.xml");

        Hashtable<String, Weapon> weapons = new Hashtable<>();

        try {


            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();


            xpp.setInput(new FileReader(file));


            int eventType = xpp.getEventType();


            while (!(eventType == XmlPullParser.END_TAG && xpp.getName() != null && xpp.getName().equals("Weapons"))) {
                if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Weapon")) {


                    Weapon wp = new Weapon();


                    eventType = xpp.next();
                    while (!(eventType == XmlPullParser.END_TAG && xpp.getName() != null && xpp.getName().equals("Weapon"))) {

                        if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Key"))
                            wp.Key = getContent(xpp);
                        else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Name"))
                            wp.Name = getContent(xpp);
                        else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("SkillKey"))
                            wp.SkillID = SkillList.get(getContent(xpp));
                        else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Damage"))
                            wp.Damage = Integer.parseInt(getContent(xpp));
                        else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Crit"))
                            wp.CriticalValue = Integer.parseInt(getContent(xpp));
                        else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Range")) {
                            String sRange = getContent(xpp);
                            if (sRange.equals("Engaged")) wp.RangeID = 0;
                            if (sRange.equals("Short")) wp.RangeID = 1;
                            if (sRange.equals("Medium")) wp.RangeID = 2;
                            if (sRange.equals("Long")) wp.RangeID = 3;
                            if (sRange.equals("Extreme")) wp.RangeID = 4;
                        } else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Encumbrance"))
                            wp.Encumbrance = Integer.parseInt(getContent(xpp));
                        else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("HP"))
                            wp.HardPoints = Integer.parseInt(getContent(xpp));
                        else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Price"))
                            wp.Price = Integer.parseInt(getContent(xpp));
                        else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Type"))
                            wp.Category = getContent(xpp);
                        else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Qualities")) {
                            wp.Qualities = (ArrayList<ItemQuality>) getQualities(xpp, ItemQualityList);
                        }


                        eventType = xpp.next();
                    }

                    weapons.put(wp.Key, wp);
                }


                eventType = xpp.next();

            }


        } catch (Exception e) {
            e.printStackTrace();

        }


        return weapons;
    }

    private static List<ItemQuality> getQualities(XmlPullParser xpp, Hashtable<String, Integer> QualityList) throws IOException, XmlPullParserException {
        return getQualities(xpp, QualityList, "Qualities", "Quality");
    }

    private static List<ItemQuality> getQualities(XmlPullParser xpp, Hashtable<String, Integer> QualityList, String Tags, String Tag) throws IOException, XmlPullParserException {
        List<ItemQuality> qualities = new ArrayList<>();

        int eventType = xpp.next();

        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals(Tags))) {
            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals(Tag)) {

                ItemQuality quality = getQuality(xpp, Tag);
                if (QualityList.containsKey(quality.Key))
                    quality.QualityID = QualityList.get(quality.Key);


                qualities.add(quality);


            }

            eventType = xpp.next();
        }


        return qualities;
    }


    private static ItemQuality getQuality(XmlPullParser xpp, String tag) throws IOException, XmlPullParserException {

        ItemQuality quality = new ItemQuality();

        int eventType = xpp.next();

        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals(tag))) {

            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Key"))
                quality.Key = getContent(xpp);
            else if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Count"))
                quality.Value = getIntegerContent(xpp);

            eventType = xpp.next();
        }

        return quality;
    }


    static class DialogMessageObject {
        String directory;
        String filename;
        String objectType;
    }

    static ProgressDialog pd = null;

    static Handler progressDialogHandler = null;

    static class ProgressDialogHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.obj.equals("end"))
                pd.dismiss();
            else if (msg.obj.equals("insert"))
                pd.setMessage(App.getContext().getString(R.string.InsertNPCIntoDatabase));
            else {
                pd.setMessage(String.format(App.getContext().getString(R.string.ImportNPCMessage),
                        ((DialogMessageObject) msg.obj).directory,
                        ((DialogMessageObject) msg.obj).filename,
                        ((DialogMessageObject) msg.obj).objectType));
            }
        }
    }

    public static void UpdateDatabaseFromStorage(final Context context) {


        progressDialogHandler = new ProgressDialogHandler();

        pd = ProgressDialog.show(context,
                context.getString(R.string.dialog_initdatabase),
                context.getString(R.string.dialog_pleasewait), true,
                false);


        new Thread(new Runnable() {

            @Override
            public void run() {


                File sdCardRoot = Environment.getExternalStorageDirectory();
                File baseDir = new File(sdCardRoot, "SWEotE");

                Hashtable<String, Integer> talentList = XmlImport.getDataTalents();
                Hashtable<String, Integer> skillList = XmlImport.getDataSkills();
                Hashtable<String, Integer> ItemQualityList = XmlImport.getDataItemQualities();
                Hashtable<String, Weapon> weaponList = XmlImport.getDataWeapons(skillList, ItemQualityList);
                Hashtable<String, ForcePower> forcePowerList = XmlImport.getDataForcePowers();





//                List<NPC> npcs = OldImportFolder(baseDir, talentList, skillList, weaponList, ItemQualityList, forcePowerList);
//
//                List<Vehicle> vehicles = ImportVehicles(new File(sdCardRoot, "SWEotE/Vehicles"), weaponList, ItemQualityList);
//
//                Message msg = new Message();
//
//                msg.obj = "insert";
//                progressDialogHandler.sendMessage(msg);

                Database db = new Database(context);
                db.EmptyNPC();
                db.EmptyVehicles();


                ImportFolder(baseDir, db, talentList, skillList, weaponList, ItemQualityList, forcePowerList);

                db.close();

                Message endMessage = new Message();
                endMessage.obj = "end";
                progressDialogHandler.sendMessage(endMessage);


            }

        }).start();
    }


    public static void ImportFolder(File directory, Database db,
                                                 Hashtable<String, Integer> talentList,
                                                 Hashtable<String, Integer> skillList,
                                                 Hashtable<String, Weapon> weaponList,
                                                 Hashtable<String, Integer> ItemQualityList,
                                                 Hashtable<String, ForcePower> ForcePowerList) {


        File[] files = directory.listFiles();


        for (File file : files) {


            if (file.isFile()) {

                String fileType = XmlImport.getFileType(file.getPath());

                if (fileType == null)
                    continue;

                if (fileType.equals("Adversary")) {



                    Message msg = new Message();
                    DialogMessageObject obj = new DialogMessageObject();
                    obj.filename = file.getName();
                    obj.directory = directory.getName();
                    obj.objectType = "Personnage";
                    msg.obj = obj;
                    progressDialogHandler.sendMessage(msg);

                    NPC npc = XmlImport.ImportNPC(file.getPath(), talentList, skillList, weaponList, ItemQualityList, ForcePowerList);

                    if (npc != null)
                        db.AddNPC(npc);


                } else if (fileType.equals("Vehicle")) {


                    Message msg = new Message();
                    DialogMessageObject obj = new DialogMessageObject();
                    obj.filename = file.getName();
                    obj.directory = directory.getName();
                    obj.objectType = "Vehicule";
                    msg.obj = obj;
                    progressDialogHandler.sendMessage(msg);

                    Vehicle vehicle = XmlImport.ImportVehicle(file.getPath(), weaponList, ItemQualityList);

                    if (vehicle != null)
                        db.AddVehicle(vehicle);


                }

            }
            else if (file.isDirectory())
                ImportFolder(file, db, talentList, skillList, weaponList, ItemQualityList, ForcePowerList);
        }
    }

    public static ArrayList<NPC> OldImportFolder(File directory,
                                                 Hashtable<String, Integer> talentList,
                                                 Hashtable<String, Integer> skillList,
                                                 Hashtable<String, Weapon> weaponList,
                                                 Hashtable<String, Integer> ItemQualityList,
                                                 Hashtable<String, ForcePower> ForcePowerList) {
        File[] files = directory.listFiles();
        ArrayList<NPC> importFolder = new ArrayList<>();



        for (File file : files) {


            if (file.isFile()) {

                String fileType = XmlImport.getFileType(file.getPath());

                if (fileType == null)
                    continue;


                if (fileType.equals("Adversary")) {

                    NPC npc = XmlImport.ImportAdversary(file.getPath(), talentList, skillList, weaponList, ItemQualityList, ForcePowerList);


                    Message msg = new Message();
                    DialogMessageObject obj = new DialogMessageObject();
                    obj.filename = file.getName();
                    obj.directory = directory.getName();
                    obj.objectType = "Adversary";
                    msg.obj = obj;
                    progressDialogHandler.sendMessage(msg);

                    importFolder.add(npc);


                }
            } else if (file.isDirectory())
                importFolder.addAll(OldImportFolder(file, talentList, skillList, weaponList, ItemQualityList, ForcePowerList));
        }


        return importFolder;
    }


    public static EncounterFile ImportEncounterFile(String Filename) {

        EncounterFile file = new EncounterFile();
        file.Fighters = new ArrayList<>();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new FileReader(Filename));

            int eventType = xpp.getEventType();

            String sTagName;

            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {
                    sTagName = xpp.getName();

                    switch (sTagName) {
                        case "Name":
                            file.EncounterName = getContent(xpp);
                            break;
                        case "Description":
                            file.Description = getContent(xpp);
                            break;
                        case "Campaign":
                            file.Campaign = getContent(xpp);
                            break;
                        case "Adventure":
                            file.Adventure = getContent(xpp);
                            break;
                        case "Location":
                            file.Location = getContent(xpp);
                            break;
                        case "Notes":
                            file.Notes = getContent(xpp);
                            break;
                        case "EncGroups":
                            file.Fighters = getEncGroups(xpp);
                            break;
                    }

                }

                eventType = xpp.next();
            }

        }
        catch (Exception e) {
            e.fillInStackTrace();
        }

        return file;
    }

    private static List<SimpleEncounterFighter> getEncGroups (XmlPullParser xpp) throws IOException, XmlPullParserException {


        List<SimpleEncounterFighter> fighters = new ArrayList<>();

        int eventType = xpp.next();



        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("EncGroups"))) {

            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("EncGroup"))
                fighters.add(getEncGroup(xpp));

            eventType = xpp.next();
        }

        return fighters;


    }

    private static SimpleEncounterFighter getEncGroup (XmlPullParser xpp) throws IOException, XmlPullParserException {


        SimpleEncounterFighter fighter = new SimpleEncounterFighter();

        int eventType = xpp.next();



        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("EncGroup"))) {

            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("AdvKey"))
                fighter.setKey(getContent(xpp));
            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Count"))
                fighter.Count = getIntegerContent(xpp);
            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("AltName")) {
                String altName = getContent(xpp);
                if (!altName.isEmpty())
                    fighter.Name = altName;
            }
            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("StartingWounds"))
                fighter.StartingWounds = getIntegerContent(xpp);
            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("StartingStrain"))
                fighter.StartingStrain = getIntegerContent(xpp);
            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("StartingWeapon"))
                fighter.StartingWeapon = getContent(xpp);

            eventType = xpp.next();
        }

        return fighter;


    }




    public static List<NPC> ImportPCs() {

        Hashtable<String, Integer> skillList = XmlImport.getDataSkills();


        ArrayList<NPC> players = new ArrayList<>();


        File sdCardRoot = Environment.getExternalStorageDirectory();
        File yourDir = new File(sdCardRoot, "SWEotE/PC");
        if (yourDir.exists()) {


            File[] files = yourDir.listFiles();
            for (File f : files) {
                if (f.isFile()) {

                    NPC npc = ImportPC(f.getPath(), skillList);

                    players.add(npc);




                }
            }
        }

        return players;


    }


    private static NPC ImportPC(String Filename, Hashtable<String, Integer> SkillList) {


        XmlPullParserFactory factory;


        NPC npc = new NPC();


        try {

            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();


            xpp.setInput(new FileReader(Filename));


            int eventType = xpp.getEventType();


            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {
                    String sTagName = xpp.getName();

                    if (sTagName == null)
                        continue;

                    switch (sTagName) {
                        case "CharName":
                            npc.Name = getContent(xpp);
                            break;
                        case "PlayerName":
                            npc.OwnerName = getContent(xpp);
                            break;
                        case "Portrait":
                            String data = getContent(xpp);
                            byte[] dataArray = Base64.decode(data, Base64.DEFAULT);
                            npc.setImage(dataArray);
                            eventType = xpp.next();
                            continue;
                        case "Characteristics":
                            npc.Characteristics = getCharacteristics(xpp);
                            break;
                        case "Skills":
                            npc.Skills = getSkills(xpp, SkillList);
                            break;
                    }


                }

                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }

        return npc;
    }





    public static List<ScenarioItem> ImportScenarioFile(String filename) {


        XmlPullParserFactory factory;


        List<ScenarioItem> items = new ArrayList<>();


        try {

            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();


            xpp.setInput(new FileReader(filename));


            int eventType = xpp.getEventType();


            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {
                    String sTagName = xpp.getName();

                    if (sTagName == null)
                        continue;

                    if (sTagName.equals("FightScenarioItem"))
                        items.add(getFightScenarioItem(xpp));
                    if (sTagName.equals("TextScenarioItem"))
                        items.add(getTextScenarioItem(xpp));


                }

                eventType = xpp.next();
            }
        } catch (Exception e) {
            e.fillInStackTrace();
        }

        return items;
    }

    private static ScenarioItem getFightScenarioItem(XmlPullParser xpp) throws IOException, XmlPullParserException {

        FightScenarioItem fightScenarioItem = new FightScenarioItem();

        int eventType = xpp.next();



        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("FightScenarioItem"))) {

            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Name"))
                fightScenarioItem.Name = getContent(xpp);
            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Filename"))
                fightScenarioItem.encounterFilename = getContent(xpp);

            eventType = xpp.next();
        }

        return fightScenarioItem;
    }

    private static ScenarioItem getTextScenarioItem(XmlPullParser xpp) throws IOException, XmlPullParserException {

        TextScenarioItem textScenarioItem = new TextScenarioItem();

        int eventType = xpp.next();



        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("FightScenarioItem"))) {

            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Name"))
                textScenarioItem.Name = getContent(xpp);
            if (eventType == XmlPullParser.START_TAG && xpp.getName() != null && xpp.getName().equals("Filename"))
                textScenarioItem.Text = getContent(xpp);

            eventType = xpp.next();
        }

        return textScenarioItem;
    }

    public static ArrayList<Vehicle> ImportVehicles(File directory,
                                              Hashtable<String, Weapon> weaponList,
                                              Hashtable<String, Integer> ItemQualityList) {

        ArrayList<Vehicle> vehicles = new ArrayList<>();
        File[] files = directory.listFiles();

        for (File file : files) {


            if (file.isFile()) {

                String fileType = XmlImport.getFileType(file.getPath());

                if (fileType == null)
                    continue;

                if (fileType.equals("Vehicle")) {

                    vehicles.add(ImportVehicle(file.getPath(), weaponList, ItemQualityList));

                }
            }
            else if (file.isDirectory())
                vehicles.addAll(ImportVehicles(file, weaponList, ItemQualityList));
        }
        return vehicles;
    }

    public static Vehicle ImportVehicle(String Filename, Hashtable<String, Weapon> weapons, Hashtable<String, Integer> qualities) {

        Vehicle vehicle = new Vehicle();

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput(new FileReader(Filename));

            int eventType = xpp.getEventType();

            String sTagName;

            while (eventType != XmlPullParser.END_DOCUMENT) {

                if (eventType == XmlPullParser.START_TAG) {
                    sTagName = xpp.getName();

                    if (sTagName == null)
                        continue;

                    if (sTagName.equals("Key")) {
                        vehicle.setKey(getContent(xpp));
                        SetVehicleImage(vehicle);
                    }
                    if (sTagName.equals("Name"))
                        vehicle.setName(getContent(xpp));
                    if (sTagName.equals("Silhouette"))
                        vehicle.setSilhouette(getIntegerContent(xpp));
                    if (sTagName.equals("Type"))
                        vehicle.setType(getContent(xpp));
                    if (sTagName.equals("Speed"))
                        vehicle.setSpeed(getIntegerContent(xpp));
                    if (sTagName.equals("Handling")) {
                        String value = getContent(xpp).replace("+", "");
                        vehicle.setHandling(Integer.valueOf(value));
                    }
                    if (sTagName.equals("DefFore"))
                        vehicle.setDefFore(getIntegerContent(xpp));
                    if (sTagName.equals("DefAft"))
                        vehicle.setDefAft(getIntegerContent(xpp));
                    if (sTagName.equals("DefPort"))
                        vehicle.setDefPort(getIntegerContent(xpp));
                    if (sTagName.equals("DefStarboard"))
                        vehicle.setDefStarboard(getIntegerContent(xpp));
                    if (sTagName.equals("HullTrauma"))
                        vehicle.setHullTrauma(getIntegerContent(xpp));
                    if (sTagName.equals("SystemStrain"))
                        vehicle.setSystemStrain(getIntegerContent(xpp));
                    if (sTagName.equals("Armor"))
                        vehicle.setArmor(getIntegerContent(xpp));
                    if (sTagName.equals("VehicleWeapons"))
                        vehicle.getWeapons().addAll(GetVehicleWeapons(xpp, weapons, qualities));
                    if (sTagName.equals("Categories"))
                        vehicle.getCategories().addAll(GetVehicleCategories(xpp));


                }

                eventType = xpp.next();
            }

        } catch (XmlPullParserException e) {
            Log.i("hopla2", "Fichier " + Filename + " erroné");
            e.printStackTrace();
            return null;

        } catch (Exception e) {
            Log.i("hopla2", "Fichier " + Filename + " erroné");
            e.printStackTrace();
            return null;
        }


        return vehicle;
    }

    private static void SetVehicleImage(Vehicle vehicle) {



        File sdCardRoot = Environment.getExternalStorageDirectory();

        File Image = new File(sdCardRoot, "SWEotE/VehicleImages/" + vehicle.getKey() + ".png");
        if (Image.exists())
            vehicle.setImageIcon(getScaledBitmap(Image.getAbsolutePath(), 128));

        File Silhouette = new File(sdCardRoot, "SWEotE/VehicleSilhouettes/" + vehicle.getKey() + ".png");
        if (Silhouette.exists())
            vehicle.setImageSilhouette(getScaledBitmap(Silhouette.getAbsolutePath(), 128));



    }

    private static Bitmap getScaledBitmap (String path, int maxSize) {
        BitmapFactory.Options options = new BitmapFactory.Options();



        Bitmap b = BitmapFactory.decodeFile(path, options);

        int outWidth;
        int outHeight;
        int inWidth = b.getWidth();
        int inHeight = b.getHeight();
        if(inWidth > inHeight){
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }

        return Bitmap.createScaledBitmap(b, outWidth, outHeight, false);
    }

    private static List<VehicleWeapon> GetVehicleWeapons(XmlPullParser xpp, Hashtable<String, Weapon> weapons, Hashtable<String, Integer> qualities) throws IOException, XmlPullParserException {
        List<VehicleWeapon> wps = new ArrayList<>();

        int eventType = xpp.next();

        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("VehicleWeapons"))) {
            if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("VehicleWeapon")) {
                wps.add(GetVehicleWeapon(xpp, weapons, qualities));
            }
            eventType = xpp.next();
        }


        return wps;
    }


    private static VehicleWeapon GetVehicleWeapon(XmlPullParser xpp, Hashtable<String, Weapon> weapons, Hashtable<String, Integer> qualities) throws IOException, XmlPullParserException {

        VehicleWeapon weapon= new VehicleWeapon();

        int eventType = xpp.next();

        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("VehicleWeapon"))) {

            if (xpp.getName() == null) {
                eventType = xpp.next();
                continue;
            }

            if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("FiringArcs"))
                weapon.setFiringArcs(GetFiringArcs(xpp));
            else if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("Key"))
            {
                String key = getContent(xpp);
                if (weapons.containsKey(key))
                    weapon.setBaseWeapon(weapons.get(key));
                else
                    weapon.setBaseWeapon(null);

            }
            else if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("Count"))
                weapon.setCount(getIntegerContent(xpp));
            else if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("Turret"))
                weapon.setTurret(getContent(xpp).equals("true"));
            else if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("Qualities"))
                weapon.getQualities().addAll(getQualities(xpp, qualities));


            eventType = xpp.next();
        }


        return weapon;
    }



    private static FiringArcs GetFiringArcs(XmlPullParser xpp) throws IOException, XmlPullParserException {

        FiringArcs firingArcs = new FiringArcs();

        int eventType = xpp.next();






        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("FiringArcs"))) {

            if (xpp.getName() == null) {
                eventType = xpp.next();
                continue;
            }

            if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("Fore"))
                firingArcs.setFore(getContent(xpp).equals("true"));
            else if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("Aft"))
                firingArcs.setAft(getContent(xpp).equals("true"));
            else if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("Port"))
                firingArcs.setPort(getContent(xpp).equals("true"));
            else if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("Starboard"))
                firingArcs.setStarboard(getContent(xpp).equals("true"));
            else if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("Dorsal"))
                firingArcs.setDorsal(getContent(xpp).equals("true"));
            else if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("Ventral"))
                firingArcs.setVentral(getContent(xpp).equals("true"));

            eventType = xpp.next();
        }


        return firingArcs;
    }



    private static List<String> GetVehicleCategories(XmlPullParser xpp) throws IOException, XmlPullParserException {
        List<String> cat = new ArrayList<>();

        int eventType = xpp.next();

        while (!(eventType == XmlPullParser.END_TAG && xpp.getName().equals("Categories"))) {
            if (eventType == XmlPullParser.START_TAG && xpp.getName().equals("Category")) {
                cat.add(getContent(xpp));
            }
            eventType = xpp.next();
        }


        return cat;
    }


}


