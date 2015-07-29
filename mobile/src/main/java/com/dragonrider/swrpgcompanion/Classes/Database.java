package com.dragonrider.swrpgcompanion.Classes;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.Toast;

import com.dragonrider.swrpgcompanion.Classes.NPC.NPCTypes;
import com.dragonrider.swrpgcompanion.R;
import com.dragonrider.swrpgcompanion.XWingWrapper.Vehicle;
import com.dragonrider.swrpgcompanion.XWingWrapper.VehicleWeapon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Database extends SQLiteOpenHelper {

	public static final int DATABASE_VERSION = 6;
	
	public Database(Context context) {
		super(context, "sweote.db", null, DATABASE_VERSION) ;

	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		android.util.Log.d("database", "OnCreate");
		db.execSQL("create table NPC (id integer primary key autoincrement, Name text, Key text, Description text, Race text, Experience integer(5), Category text, TotalWounds integer(2), TotalStrain integer(2), Type integer(1), Soak integer(1), MeleeDefense integer(1), RangedDefense integer(1), ForceRating integer(2), SkillText text, CharacteristicText text, TalentText text, AbilitiesText text, WeaponText text, ForcePowers text, Image blob )");
		db.execSQL("create table PLAYERS (id integer primary key autoincrement, PlayerName text, CharacterName text, Experience integer(5), IsPresent integer, Image blob)");
        db.execSQL("create table VEHICLES (id integer primary key autoincrement, Key text, Name text, Type text, Silhouette integer(2), Speed integer(2), Handling integer(2), DefFore integer(2), DefAft integer(2), DefPort integer(2), DefStarboard integer(2), Armor integer(2), HullTrauma integer(3), SystemStrain integer(3), Weapons text, Categories text, ImageIcon blob, ImageSilhouette blob)");
	}
	
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		android.util.Log.d("database", "onUpgrade");

		if (oldVersion == 2 && newVersion == 3)
			db.execSQL("ALTER TABLE NPC ADD ForceRating integer(5)");
        if (oldVersion == 3 && newVersion == 4)
            db.execSQL("ALTER TABLE PLAYERS ADD Image blob");
        if (oldVersion == 4 && newVersion == 5)
            db.execSQL("ALTER TABLE NPC ADD ForcePowers text");
        if (oldVersion == 5 && newVersion == 6)
            db.execSQL("create table VEHICLES (id integer primary key autoincrement, Key text, Name text, Type text, Silhouette integer(2), Speed integer(2), Handling integer(2), DefFore integer(2), DefAft integer(2), DefPort integer(2), DefStarboard integer(2), Armor integer(2), HullTrauma integer(3), SystemStrain integer(3), Weapons text, Categories text, ImageIcon blob, ImageSilhouette blob)");



	}
	
	
	public void SaveDB() {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			if (sd.canWrite()) {
				String currentDBPath = "//data//com.dragonrider.swrpgcompanion//databases//sweote.db";
				String backupDBPath = "sweote.db";
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				if (currentDB.exists()) {
					FileInputStream is = new FileInputStream(currentDB);
					FileOutputStream os = new FileOutputStream(backupDB);
					FileChannel src = is.getChannel();
					FileChannel dst = os.getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
					is.close();
					os.close();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	
		
	}
	public void LoadDB() {
		try {

			android.util.Log.d("hopla", "fild sd = env...");
			File sd = Environment.getExternalStorageDirectory();
			android.util.Log.d("hopla", "fild data = env...");
			File data = Environment.getDataDirectory();

			//if (data.canWrite()) {
				if (sd.canRead()) {
					String currentDBPath = "//data//com.dragonrider.swrpgcompanion//databases//sweote.db";
					String backupDBPath = "sweote.db";
					
					File currentDB = new File(data, currentDBPath);
					
					File backupDB = new File(sd, backupDBPath);

					
					String[] children = new File(data, "//data//com.dragonrider.swrpgcompanion//databases").list();


                    for (int i = 0; i < children.length; i++)
                        new File(data, "//data//com.dragonrider.swrpgcompanion//databases//" + children[i]).delete();


//					if (currentDB.exists()) {
						FileInputStream is = new FileInputStream(backupDB);
						FileOutputStream os = new FileOutputStream(currentDB);
						FileChannel src = is.getChannel();
						FileChannel dst = os.getChannel();
						dst.transferFrom(src, 0, src.size());
						Toast.makeText(App.getContext(), String.format("Copié %d octets", src.size()), Toast.LENGTH_LONG).show();
						src.close();
						dst.close();
						is.close();
						os.close();
						
					//}
				} else Toast.makeText(App.getContext(), "sd cantread", Toast.LENGTH_LONG).show();

			//}
			//else Toast.makeText(App.getContext(), "data cantwrite", Toast.LENGTH_LONG).show();
		} catch (Exception e) {
			e.printStackTrace();
			   Toast.makeText(App.getContext(), e.toString(), Toast.LENGTH_LONG).show();


               
		}

	}

	
	
	private NPC CursorToNPC(Cursor c) {
		NPC npc = new NPC();
		npc.DatabaseID = c.getInt(c.getColumnIndex("id")); 
		npc.Name = c.getString(c.getColumnIndex("Name"));
		npc.Description = c.getString(c.getColumnIndex("Description"));
		npc.Race = c.getString(c.getColumnIndex("Race"));
		npc.Category = c.getString(c.getColumnIndex("Category"));
		npc.Key = c.getString(c.getColumnIndex("Key"));
		npc.Soak = c.getInt(c.getColumnIndex("Soak"));
		npc.MeleeDefense = c.getInt(c.getColumnIndex("MeleeDefense"));
		npc.RangedDefense = c.getInt(c.getColumnIndex("RangedDefense"));
		npc.TotalStrain = c.getInt(c.getColumnIndex("TotalStrain"));
		npc.TotalWounds = c.getInt(c.getColumnIndex("TotalWounds"));
		npc.Experience = c.getInt(c.getColumnIndex("Experience"));
		npc.ForceRating = c.getInt(c.getColumnIndex("ForceRating"));

		byte[] data = c.getBlob(c.getColumnIndex("Image"));
		if (data != null)
		npc.setImage(data);
		
		int iType = c.getInt(c.getColumnIndex("Type"));
		npc.Type = NPCTypes.Minion;
		if (iType == 1) npc.Type = NPCTypes.Rival;
		if (iType == 2) npc.Type = NPCTypes.Nemesis;

		
		
		String SkillText = c.getString(c.getColumnIndex("SkillText"));
		if (!SkillText.contains("*")) {
			npc.Skills = Skill.fromDatabaseString(SkillText);
		}
		
		if (!c.getString(c.getColumnIndex("CharacteristicText")).contains("*"))
			npc.Characteristics = Characteristic.fromDatabaseString(c.getString(c.getColumnIndex("CharacteristicText")));

		String talentText = c.getString(c.getColumnIndex("TalentText"));
		if (!talentText.contains("*"))
			npc.Talents = Talent.fromDatabaseString(talentText);
			

		String[] split = c.getString(c.getColumnIndex("AbilitiesText")).split("[*]");
		if (split[0] != "")
			for (String s : split) {
				if (s.isEmpty() == false)
					npc.Abilities.add(s);
			}

        npc.ForcePowers = ForcePower.getPowersFromDB(c.getString(c.getColumnIndex("ForcePowers")));




        String items = c.getString(c.getColumnIndex("WeaponText"));
		if (items.startsWith("#")) //nouvelle génération
		{
			npc.setItemsFromDatabaseString(items);
		}
		else {
		
			split = items.split("[*]");
			for (String s : split) {
				String[] split2 = s.split("[|]");
				if (split2[0].equals("0")) {
					Item itm = new Item();
					itm.Description = split2[1];
					itm.Encumbrance = Integer.valueOf(split2[2]);
					itm.id = split2[3];
					itm.Name = split2[4];
					itm.Price = Integer.valueOf(split2[5]);
					itm.Rarity = Integer.valueOf(split2[6]);
					itm.Restricted = Boolean.valueOf(split2[6]);
					npc.Items.add(itm);
	
	
				}
				else if (split2[0].equals("1")) {
	
					
					Weapon wp = new Weapon();
					wp.Category = split2[1];
					wp.CriticalValue = Integer.valueOf(split2[2]);
					wp.Damage = Integer.valueOf(split2[3]);
					wp.Description = split2[4];
					wp.Encumbrance = Integer.valueOf(split2[5]);
					wp.HardPoints = Integer.valueOf(split2[6]);
					wp.id = "";
					wp.Name = split2[8];
					wp.Price = Integer.valueOf(split2[9]);
					wp.Range = split2[10];
					wp.Rarity = Integer.valueOf(split2[11]);
					wp.Restricted = Boolean.valueOf(split2[12]);
					wp.Traits = split2[13];
					wp.UsedSkill = split2[14];
					npc.Items.add(wp);
				}
			}
		}
		return npc;
	}


    public List<NPC> getAllNPC() {





        List<NPC> npcs = new ArrayList<>();



        SQLiteDatabase sb;

        sb = getReadableDatabase();


        Cursor c = sb.rawQuery("SELECT * FROM NPC", null);


        c.moveToFirst();

        while (!c.isAfterLast()) {
            NPC npc = CursorToNPC(c);

            npcs.add(npc);
            c.moveToNext();
        }
        sb.close();
        return npcs;








    }

	
	public NPC GetNPCbyKey(String Key) {
		SQLiteDatabase sb = getReadableDatabase();
		Cursor c = sb.rawQuery("SELECT * FROM NPC WHERE key=?", new String[] {Key});
		
		if (c.getCount() > 0)
		{
			c.moveToFirst();
			sb.close();
			return CursorToNPC(c);
		}
		sb.close();
		return null;
	}
	
	public NPC GetNPCbyID(int id) {
		SQLiteDatabase sb = getReadableDatabase();
		Cursor c = sb.rawQuery("SELECT * FROM NPC WHERE id=?", new String[] {String.valueOf(id)});
		
		if (c.getCount() > 0)
		{
			c.moveToFirst();
			sb.close();
			return CursorToNPC(c);
		}
		sb.close();
		return null;
		
	}
	
	public NPC GetNPCbyName(String name) {
		SQLiteDatabase sb = getReadableDatabase();
		Cursor c = sb.rawQuery("SELECT * FROM NPC WHERE Name=?", new String[] {name});
		
		if (c.getCount() > 0)
		{
			c.moveToFirst();
			sb.close();
			return CursorToNPC(c);
		}
		sb.close();
		return null;
		
	}
	
	public List<SWListBoxItem> GetNPCShortList() {
		List<SWListBoxItem> npcs = new ArrayList<SWListBoxItem>();
		
		
		SQLiteDatabase sb = getReadableDatabase();
		
		Cursor c = sb.rawQuery("SELECT * FROM NPC ORDER BY Category ASC, Name ASC", null);	
		
		
		c.moveToFirst();

		while (!c.isAfterLast()) {

			String Name=  c.getString(c.getColumnIndex("Name"));
			int iType = c.getInt(c.getColumnIndex("Type"));
			if (iType == 0) Name += String.format(" [%s]", App.getContext().getString(R.string.minion));
			if (iType == 1) Name += String.format(" [%s]", App.getContext().getString(R.string.rival));
			if (iType == 2) Name += String.format(" [%s]", App.getContext().getString(R.string.nemesis));
			
			String Description = String.valueOf(c.getInt(c.getColumnIndex("Experience"))) + "xp: " + c.getString(c.getColumnIndex("Description"));
			String Category = c.getString(c.getColumnIndex("Category"));
			SWListBoxItem item = new SWListBoxItem(Name, Description);
			item.setCategory(Category);
			byte[] data = c.getBlob(c.getColumnIndex("Image"));
			Bitmap bmp;
			if (data != null) {
                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
                if (bmp != null)
                    item.setImage(bmp);
            }
			item.setTag(iType == 2);

			
			npcs.add(item);

			c.moveToNext();
		}
		sb.close();
		
		
		
		return npcs;
	}


	public void SaveNPC(NPC npc) {
		
		
		if (npc.DatabaseID != -1) {
			SQLiteDatabase sb = getWritableDatabase();
			
			SQLiteStatement statement = sb.compileStatement("UPDATE NPC SET Description=?, Experience=?, Race=?, Category=?, TotalWounds=?, TotalStrain=?, Type=?, Soak=?, MeleeDefense=?, RangedDefense=?, SkillText=?, CharacteristicText=?, TalentText=?, AbilitiesText=?, WeaponText=?, Image=?, Key=?, ForceRating=?, ForcePowers=? WHERE id=?");
			statement.clearBindings();
			statement.bindString(1, npc.Description);
			statement.bindLong(2, npc.Experience);
			statement.bindString(3, npc.Race);
			statement.bindString(4, npc.Category);
			statement.bindLong(5, npc.TotalWounds);
			statement.bindLong(6, npc.TotalStrain);
			int iType = 0;
			if (npc.Type == NPCTypes.Rival) iType = 1;
			if (npc.Type == NPCTypes.Nemesis) iType = 2;
			statement.bindLong(7, iType);
			statement.bindLong(8, npc.Soak);
			statement.bindLong(9, npc.MeleeDefense);
			statement.bindLong(10, npc.RangedDefense);
			
			String SkillText = Skill.toDatabaseString((ArrayList<Skill>) npc.Skills);
			
			statement.bindString(11, SkillText);
			String CharacteristicText = Characteristic.toDatabaseString((ArrayList<Integer>) npc.Characteristics);

			statement.bindString(12, CharacteristicText);
			String TalentText = Talent.toDatabaseString(npc.Talents);
			
			statement.bindString(13, TalentText);
			String AbilitiesText = "";
			for (String s : npc.Abilities) {
				AbilitiesText += String.format("*%s", s);
			}
			statement.bindString(14, AbilitiesText);
			
			
			statement.bindString(15, npc.getItemsForDatabaseString());
			
			statement.bindBlob(16, npc.getImageData());
			
			statement.bindString(17, npc.Key);

			statement.bindLong(18, npc.ForceRating);

            statement.bindString(19, ForcePower.toDatabaseString(npc.ForcePowers));


			statement.bindLong(20, npc.DatabaseID);
			
			
			statement.executeUpdateDelete();

            sb.close();
		}
		else { 
			AddNPC(npc);
		}
		
	}
	
	public void AddNPC(NPC npc) {
		
		SQLiteDatabase sb = getWritableDatabase();
		
		String sql = "INSERT INTO NPC (";
		
		sql += "'Name', 'Key', 'Description', 'Experience', 'Race', 'Category', 'TotalWounds', 'TotalStrain', 'Type', 'Soak', 'MeleeDefense', 'RangedDefense', 'SkillText', 'CharacteristicText', 'TalentText', 'AbilitiesText', 'WeaponText', 'Image', 'ForceRating', 'ForcePowers')";
		
		int iType = 0;
		if (npc.Type == NPCTypes.Rival) iType = 1;
		if (npc.Type == NPCTypes.Nemesis) iType = 2;
		String SkillText = Skill.toDatabaseString((ArrayList<Skill>) npc.Skills);

		String TalentText = Talent.toDatabaseString(npc.Talents);
		
		String AbilitiesText = "";
		for (String s : npc.Abilities) {
			AbilitiesText += String.format("*%s", s);
		}
		String CharacteristicText = Characteristic.toDatabaseString((ArrayList<Integer>) npc.Characteristics);
		
		sql += " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		SQLiteStatement statement = sb.compileStatement(sql);

		
		statement.clearBindings();
		statement.bindString(1, npc.Name);
		statement.bindString(2, npc.Key);
		statement.bindString(3, npc.Description);
		statement.bindLong(4, npc.Experience);
		statement.bindString(5, npc.Race);
		statement.bindString(6, npc.Category);
		statement.bindLong(7, npc.TotalWounds);
		statement.bindLong(8, npc.TotalStrain);
		statement.bindLong(9, iType);
		statement.bindLong(10, npc.Soak);
		statement.bindLong(11, npc.MeleeDefense);
		statement.bindLong(12, npc.RangedDefense);
		statement.bindString(13, SkillText);
		statement.bindString(14, CharacteristicText);
		statement.bindString(15, TalentText);
		statement.bindString(16, AbilitiesText);
		statement.bindString(17, npc.getItemsForDatabaseString());

		statement.bindBlob(18, npc.getImageData());
        statement.bindLong(19, npc.ForceRating);
        statement.bindString(20, ForcePower.toDatabaseString(npc.ForcePowers));

		statement.executeUpdateDelete();
		
		
		
		sb.close();
		
	}

	public void EmptyNPC() {
		SQLiteDatabase db = getReadableDatabase();
		db.execSQL("DELETE FROM NPC");
		db.close();
		
	}

    public void EmptyVehicles() {
        SQLiteDatabase db = getReadableDatabase();
        db.execSQL("DELETE FROM VEHICLES");
        db.close();

    }


	
	public List<PlayerCharacter> getAllPC() {

		
		

		
		List<PlayerCharacter> pcs = new ArrayList<>();
		


		SQLiteDatabase sb;

		sb = getReadableDatabase();


		Cursor c = sb.rawQuery("SELECT * FROM PLAYERS", null);	
		

		c.moveToFirst();

		while (!c.isAfterLast()) {
			PlayerCharacter pc = new PlayerCharacter();
			
			pc.CharacterName = c.getString(c.getColumnIndex("CharacterName"));
			pc.PlayerName = c.getString(c.getColumnIndex("PlayerName"));
			int i = c.getInt(c.getColumnIndex("IsPresent"));
			if (i == 0) 
				pc.setIsPresent(false);
			else 
				pc.setIsPresent(true);

            byte[] data = c.getBlob(c.getColumnIndex("Image"));
            if (data != null)
                pc.setImage(data);

			
			pcs.add(pc);
			c.moveToNext();
		}
		sb.close();
		return pcs;

		


		

		
		
	}
	
	public void AddPC(PlayerCharacter pc){






		
		String sql = "INSERT INTO PLAYERS ('CharacterName', 'PlayerName', 'IsPresent', 'Image') VALUES (?, ?, ?, ?)";
		int i = 0;
		if (pc.Present) i = 1;
        SQLiteDatabase sb = getWritableDatabase();
        SQLiteStatement statement = sb.compileStatement(sql);
        //"'Name', 'Description', 'Race', 'Category', 'TotalWounds', 'TotalStrain', 'Type', 'Soak',
        //'MeleeDefense', 'RangedDefense', 'SkillText', 'CharacteristicText', 'TalentText',
        //'AbilitiesText', 'WeaponText', 'Image')";

        statement.clearBindings();
        statement.bindString(1, pc.CharacterName);
        statement.bindString(2, pc.PlayerName);
        statement.bindLong(3, i);
        statement.bindBlob(4, pc.getImageData());


        statement.executeUpdateDelete();



        sb.close();
	}
	
	

	public void EmptyPC() {
		SQLiteDatabase db = getReadableDatabase();
		db.execSQL("DELETE FROM PLAYERS");
		db.close();
		
	}


    public void SaveVehicle(Vehicle vehicle) {
        if (vehicle.getDatabaseID() != -1)
        {
            SQLiteDatabase sb = getWritableDatabase();

            String sql = "UPDATE VEHICLES SET ";

            sql += "Key=?, Name=?, Type=?, Silhouette=?, Speed=?, Handling=?, DefFore=?, DefAft=?, DefPort=?, DefStarboard=?, Armor=?, HullTrauma=?, SystemStrain=?, Weapons=?, Categories=?, ImageIcon=?, ImageSilhouette=? WHERE id=?";

            SQLiteStatement statement = sb.compileStatement(sql);


            String weaponText = "";
            for (VehicleWeapon w : vehicle.getWeapons())
                weaponText += w.toDatabaseString();


            String categories = "";


            for (String s : vehicle.getCategories()) {
                categories += String.format("#%s", s);
            }


            statement.clearBindings();
            statement.bindString(1, vehicle.getKey());
            statement.bindString(2, vehicle.getName());
            statement.bindString(3, vehicle.getType());
            statement.bindLong(4, vehicle.getSilhouette());
            statement.bindLong(5, vehicle.getSpeed());
            statement.bindLong(6, vehicle.getHandling());
            statement.bindLong(7, vehicle.getDefFore());
            statement.bindLong(8, vehicle.getDefAft());
            statement.bindLong(9, vehicle.getDefPort());
            statement.bindLong(10, vehicle.getDefStarboard());
            statement.bindLong(11, vehicle.getArmor());
            statement.bindLong(12, vehicle.getHullTrauma());
            statement.bindLong(13, vehicle.getSystemStrain());
            statement.bindString(14, weaponText);
            statement.bindString(15, categories);
            statement.bindBlob(16, vehicle.getImageIconData());
            statement.bindBlob(17, vehicle.getImageSilhouetteData());
            statement.bindLong(18, vehicle.getDatabaseID());

            statement.executeUpdateDelete();


            sb.close();
        }
        else
            AddVehicle(vehicle);
    }




    public void AddVehicle(Vehicle vehicle) {
        SQLiteDatabase sb = getWritableDatabase();

        String sql = "INSERT INTO VEHICLES (";

        sql += "'Key', 'Name', 'Type', 'Silhouette', 'Speed', 'Handling', 'DefFore', 'DefAft', 'DefPort', 'DefStarboard', 'Armor', 'HullTrauma', 'SystemStrain', 'Weapons', 'Categories', 'ImageIcon', 'ImageSilhouette') ";

        String weaponText = "";
        for (VehicleWeapon w : vehicle.getWeapons())
            weaponText += w.toDatabaseString();


        String categories = "";


        for (String s : vehicle.getCategories()) {
            categories += String.format("#%s", s);
        }


        sql += "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        SQLiteStatement statement = sb.compileStatement(sql);


        statement.clearBindings();
        statement.bindString(1, vehicle.getKey());
        statement.bindString(2, vehicle.getName());
        statement.bindString(3, vehicle.getType());
        statement.bindLong(4, vehicle.getSilhouette());
        statement.bindLong(5, vehicle.getSpeed());
        statement.bindLong(6, vehicle.getHandling());
        statement.bindLong(7, vehicle.getDefFore());
        statement.bindLong(8, vehicle.getDefAft());
        statement.bindLong(9, vehicle.getDefPort());
        statement.bindLong(10, vehicle.getDefStarboard());
        statement.bindLong(11, vehicle.getArmor());
        statement.bindLong(12, vehicle.getHullTrauma());
        statement.bindLong(13, vehicle.getSystemStrain());
        statement.bindString(14, weaponText);
        statement.bindString(15, categories);
        statement.bindBlob(16, vehicle.getImageIconData());
        statement.bindBlob(17, vehicle.getImageSilhouetteData());


        statement.executeUpdateDelete();



        sb.close();

    }

    private Vehicle CursorToVehicle(Cursor c) {

        ArrayList<VehicleWeapon> weapons = new ArrayList<>();
        String weaponText = c.getString(c.getColumnIndex("Weapons"));
        String[] split = weaponText.split("#");
        for (String s : split) {
            if (s.isEmpty()) continue;
            weapons.add(VehicleWeapon.fromDatabaseString(s));
        }
        ArrayList<String> cat = new ArrayList<>();
        split = c.getString(c.getColumnIndex("Categories")).split("#");
        for (String s : split)
        {
            if (s.isEmpty()) continue;
            cat.add(s);
        }

        byte[] data1 = c.getBlob(c.getColumnIndex("ImageIcon"));
        byte[] data2 = c.getBlob(c.getColumnIndex("ImageSilhouette"));

        return new Vehicle()
                .setDatabaseID(c.getInt(c.getColumnIndex("id")))
                .setKey(c.getString(c.getColumnIndex("Key")))
                .setName(c.getString(c.getColumnIndex("Name")))
                .setType(c.getString(c.getColumnIndex("Type")))
                .setSilhouette(c.getInt(c.getColumnIndex("Silhouette")))
                .setSpeed(c.getInt(c.getColumnIndex("Speed")))
                .setHandling(c.getInt(c.getColumnIndex("Handling")))
                .setDefFore(c.getInt(c.getColumnIndex("DefFore")))
                .setDefAft(c.getInt(c.getColumnIndex("DefAft")))
                .setDefPort(c.getInt(c.getColumnIndex("DefPort")))
                .setDefStarboard(c.getInt(c.getColumnIndex("DefStarboard")))
                .setArmor(c.getInt(c.getColumnIndex("Armor")))
                .setHullTrauma(c.getInt(c.getColumnIndex("HullTrauma")))
                .setSystemStrain(c.getInt(c.getColumnIndex("SystemStrain")))
                .setWeapons(weapons)
                .setCategories(cat)
                .setImageIcon(data1)
                .setImageSilhouette(data2)
        ;
    }


    public Vehicle getVehicleByKey(String Key) {
        SQLiteDatabase sb = getReadableDatabase();
        Cursor c = sb.rawQuery("SELECT * FROM VEHICLES WHERE key=?", new String[] {Key});

        if (c.getCount() > 0)
        {
            c.moveToFirst();
            sb.close();
            return CursorToVehicle(c);
        }
        sb.close();
        return null;
    }

    public Vehicle getVehicleByName(String name) {
        SQLiteDatabase sb = getReadableDatabase();
        Cursor c = sb.rawQuery("SELECT * FROM VEHICLES WHERE Name=?", new String[] {name});

        if (c.getCount() > 0)
        {
            c.moveToFirst();
            sb.close();
            return CursorToVehicle(c);
        }
        sb.close();
        return null;

    }

    public Vehicle getVehicleByID(int id) {
        SQLiteDatabase sb = getReadableDatabase();
        Cursor c = sb.rawQuery("SELECT * FROM VEHICLES WHERE id=?", new String[] {String.valueOf(id)});

        if (c.getCount() > 0)
        {
            c.moveToFirst();
            sb.close();
            return CursorToVehicle(c);
        }
        sb.close();
        return null;

    }

    public List<Vehicle> getAllVehicles() {
        SQLiteDatabase sb = getReadableDatabase();
        List<Vehicle> vehicles = new ArrayList<>();
        Cursor c = sb.rawQuery("SELECT * FROM VEHICLES", null);


        c.moveToFirst();

        while (!c.isAfterLast()) {

            vehicles.add(CursorToVehicle(c));
            c.moveToNext();
        }

        sb.close();
        return vehicles;
    }

    public List<Vehicle> getXWingVehicles() {
        List<Vehicle> vehicles = new ArrayList<>();


        SQLiteDatabase sb = getReadableDatabase();


        Cursor c = sb.rawQuery("SELECT * FROM VEHICLES WHERE Categories LIKE \"%xwing%\"", null);

        c.moveToFirst();

        while (!c.isAfterLast()) {

            vehicles.add(CursorToVehicle(c));
            c.moveToNext();
        }

        sb.close();
        return vehicles;

    }
}
