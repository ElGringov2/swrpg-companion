package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.dragonrider.swrpgcompanion.Classes.App;
import com.dragonrider.swrpgcompanion.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mge637 on 28/02/2015.
 * Gere les véhicules
 */
public class Vehicle {

    private int DatabaseID = -1;
    private String Key ;
    private String Name ;
    private String Type ;
    private String ManeuverMapName = "";
    private int Silhouette ;
    private int Speed ;
    private int Handling ;
    private int DefFore ;
    private int DefAft ;
    private int DefPort ;
    private int DefStarboard ;
    private int Armor ;
    private int HullTrauma ;
    private int SystemStrain ;

    private Bitmap ImageIcon;
    private Bitmap ImageSilhouette;

    private List<VehicleWeapon> Weapons = new ArrayList<>();


    private List<String> Categories = new ArrayList<>();

    public String getKey() {
        return Key;
    }

    public Vehicle setKey(String key) {
        Key = key;
        return this;
    }

    public String getName() {
        return Name;
    }

    public Vehicle setName(String name) {
        Name = name;
        return this;
    }


    public String getType() {
        return Type;
    }

    public Vehicle setType(String type) {
        Type = type;
        return this;
    }


    public int getSilhouette() {
        return Silhouette;
    }

    public Vehicle setSilhouette(int silhouette) {
        Silhouette = silhouette;
        return this;
    }


    public int getSpeed() {
        return Speed;
    }

    public Vehicle setSpeed(int speed) {
        Speed = speed;
        return this;
    }


    public int getHandling() {
        return Handling;
    }

    public Vehicle setHandling(int handling) {
        Handling = handling;
        return this;
    }


    public int getDefFore() {
        return DefFore;
    }

    public Vehicle setDefFore(int defFore) {
        DefFore = defFore;
        return this;
    }


    public int getDefAft() {
        return DefAft;
    }

    public Vehicle setDefAft(int defAft) {
        DefAft = defAft;
        return this;
    }

    public int getDefPort() {
        return DefPort;
    }

    public Vehicle setDefPort(int defPort) {
        DefPort = defPort;
        return this;
    }


    public int getDefStarboard() {
        return DefStarboard;
    }

    public Vehicle setDefStarboard(int defStarboard) {
        DefStarboard = defStarboard;
        return this;
    }


    public int getArmor() {
        return Armor;
    }

    public Vehicle setArmor(int armor) {
        Armor = armor;
        return this;
    }


    public int getHullTrauma() {
        return HullTrauma;
    }

    public Vehicle setHullTrauma(int hullTrauma) {
        HullTrauma = hullTrauma;
        return this;
    }


    public int getSystemStrain() {
        return SystemStrain;
    }

    public Vehicle setSystemStrain(int systemStrain) {
        SystemStrain = systemStrain;
        return this;
    }




    public List<VehicleWeapon> getWeapons() {
        return Weapons;
    }

    public Vehicle setWeapons(List<VehicleWeapon> weapons) {
        Weapons = weapons;
        return this;
    }

    public Bitmap getImageIcon() {
        return ImageIcon;
    }

    public Vehicle setImageIcon(Bitmap imageIcon) {
        ImageIcon = imageIcon;
        return this;
    }


    public Vehicle setImageIcon(byte[] data) {
        ImageIcon = BitmapFactory.decodeByteArray(data, 0, data.length);
        return this;
    }


    public byte[] getImageIconData() {
        if (this.ImageIcon == null) return new byte[0];
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        this.ImageIcon.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public Bitmap getImageSilhouette() {
        return ImageSilhouette;
    }

    public Vehicle setImageSilhouette(Bitmap imageSilhouette) {
        ImageSilhouette = imageSilhouette;
        return this;
    }

    public Vehicle setImageSilhouette(byte[] data) {
        ImageSilhouette = BitmapFactory.decodeByteArray(data, 0, data.length);
        return this;
    }


    public byte[] getImageSilhouetteData() {
        if (this.ImageSilhouette == null) return new byte[0];
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        this.ImageSilhouette.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    public List<String> getCategories() {
        return Categories;
    }

    public Vehicle setCategories(List<String> categories) {
        Categories = categories;
        return this;
    }

    public int getDatabaseID() {
        return DatabaseID;
    }

    public Vehicle setDatabaseID(int databaseID) {
        DatabaseID = databaseID;
        return this;
    }


    public String getFullDescription() {
        String s = "";
        for (String cat : Categories)
            s += cat + ", ";
        s = (s + "!").replace(", !", "\n");
        s += String.format("%s %d, %s %d, %s %d, %s max %d, %s %d, %s %d.\n%s: ",
                App.getContext().getString(R.string.armor),
                this.Armor,
                App.getContext().getString(R.string.handling),
                this.Handling,
                App.getContext().getString(R.string.silhouette),
                this.Silhouette,
                App.getContext().getString(R.string.xwingspeed),
                this.Speed,
                App.getContext().getString(R.string.hull_trauma),
                this.HullTrauma,
                App.getContext().getString(R.string.system_strain),
                this.SystemStrain,
                App.getContext().getString(R.string.defense)
                );

        if (this.Silhouette >= 5 )
            s += String.format("%s %d, %s %d, %s %d, %s %d.\n",
                    App.getContext().getString(R.string.fore),
                    this.DefFore,
                    App.getContext().getString(R.string.port),
                    this.DefPort,
                    App.getContext().getString(R.string.starboard),
                    this.DefStarboard,
                    App.getContext().getString(R.string.aft),
                    this.DefAft
                    );
        else
            s += String.format("%s %d, %s %d.\n",
                    App.getContext().getString(R.string.fore),
                    this.DefFore,
                    App.getContext().getString(R.string.aft),
                    this.DefAft
            );


        for (VehicleWeapon weapon : this.Weapons)
            s += weapon.toString() + ", ";

        s = (s + "!!!").replace(", !!!", "").replace("!!!", "");

        return s;
    }

    public String getManeuverMapName() {
        return ManeuverMapName;
    }

    public Vehicle setManeuverMapName(String maneuverMapName) {
        ManeuverMapName = maneuverMapName;
        return this;
    }
}
