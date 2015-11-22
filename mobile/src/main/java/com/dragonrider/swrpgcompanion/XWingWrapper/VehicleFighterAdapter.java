package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.widget.PopupMenu ;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.Classes.App;
import com.dragonrider.swrpgcompanion.Classes.GenericEditor;
import com.dragonrider.swrpgcompanion.Classes.InitiativeAdapter;
import com.dragonrider.swrpgcompanion.Classes.NPC;
import com.dragonrider.swrpgcompanion.Classes.RollResult;
import com.dragonrider.swrpgcompanion.Classes.SWListBoxItem;
import com.dragonrider.swrpgcompanion.Classes.SWListBoxItemAdapter;
import com.dragonrider.swrpgcompanion.Classes.Skill;
import com.dragonrider.swrpgcompanion.Classes.Util;
import com.dragonrider.swrpgcompanion.Classes.XmlImport;
import com.dragonrider.swrpgcompanion.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 * Created by mge637 on 01/03/2015.
 * Gere la liste des véhicules dans un combat.
 */
public class VehicleFighterAdapter extends RecyclerView.Adapter<VehicleFighterAdapter.ViewHolder>  {



    //Nom du fichier dans le cas ou le combat est sauvegardé
    private String Filename = "";

    /**
     * Obtient le nom du fichier
     * @return le nom du fichier sur le stockage
     */
    public String getFilename() {
        return Filename;
    }

    /**
     * Définit le nom du fichier
     * @param value Le nom du fichier sur le stockage.
     */
    public void setFilename(String value) {
        this.Filename = value;
    }

    //Liste des véhicules
    private List<VehicleFighter> Fighters;

    public List<VehicleFighter> getFighters() {
        return Fighters;
    }

    private Context mContext;

    /**
     * Constructeur
     * @param fighters La liste des véhicules impliqué
     */
    public VehicleFighterAdapter(Context pContext, List<VehicleFighter> fighters) {

        Fighters = fighters;
        Players = XmlImport.ImportPCs();
        mContext = pContext;
    }


    //Liste temporaire des joueurs pour eviter doublons
    public List<NPC> Players;


    /**
     * Obtient les pilotes des vaisseaux (premiers dans la liste)
     * @return
     */
    public List<CrewWrapper> getPilots() {

        ArrayList<CrewWrapper> crewWrapperArrayList = new ArrayList<>();

        for (VehicleFighter vehicleFighter : Fighters)
            if (vehicleFighter.getCrew().size() > 0)
                crewWrapperArrayList.add(vehicleFighter.getCrew().get(0));




        Collections.sort(crewWrapperArrayList, new Comparator<CrewWrapper>() {
            @Override
            public int compare(CrewWrapper lhs, CrewWrapper rhs) {
                int compareTo = ((Integer)lhs.initiative.Triumph).compareTo(rhs.initiative.Triumph);
                if (compareTo != 0)
                    return -compareTo;
                compareTo = ((Integer)lhs.initiative.Success).compareTo(rhs.initiative.Success);
                if (compareTo != 0)
                    return -compareTo;
                compareTo = -((Integer)lhs.initiative.Advantages).compareTo(rhs.initiative.Advantages);
                if (compareTo != 0)
                    return -compareTo;

                if (lhs.isOnPlayerSlot && rhs.isOnPlayerSlot) return 0;
                if (!lhs.isOnPlayerSlot && !rhs.isOnPlayerSlot) return 0;
                if (lhs.isOnPlayerSlot) return -1;
                return 1;

            }
        });



        return crewWrapperArrayList;

    }

    public List<CrewWrapper> getCrews() {

        ArrayList<CrewWrapper> crewWrapperArrayList = new ArrayList<>();

        for (VehicleFighter vehicleFighter : Fighters) {
            for (CrewWrapper wrapper : vehicleFighter.getCrew())
                wrapper.VehicleID = Fighters.indexOf(vehicleFighter);
            crewWrapperArrayList.addAll(vehicleFighter.getCrew());
        }


        Collections.sort(crewWrapperArrayList, new Comparator<CrewWrapper>() {
            @Override
            public int compare(CrewWrapper lhs, CrewWrapper rhs) {
                int compareTo = ((Integer)lhs.initiative.Triumph).compareTo(rhs.initiative.Triumph);
                if (compareTo != 0)
                    return -compareTo;
                compareTo = ((Integer)lhs.initiative.Success).compareTo(rhs.initiative.Success);
                if (compareTo != 0)
                    return -compareTo;
                compareTo = -((Integer)lhs.initiative.Advantages).compareTo(rhs.initiative.Advantages);
                if (compareTo != 0)
                    return -compareTo;

                if (lhs.isOnPlayerSlot && rhs.isOnPlayerSlot) return 0;
                if (!lhs.isOnPlayerSlot && !rhs.isOnPlayerSlot) return 0;
                if (lhs.isOnPlayerSlot) return -1;
                return 1;

            }
        });



        return crewWrapperArrayList;

    }

    public VehicleFighter getVehicle(CrewWrapper crewWrapper) {
        for (VehicleFighter vehicleFighter : Fighters)
            for (CrewWrapper inCrew : vehicleFighter.getCrew())
                if (inCrew.equals(crewWrapper))
                    return vehicleFighter;

        return null;

    }

    private InitiativeAdapter<CrewWrapper> initiativeAdapter;

    public InitiativeAdapter<CrewWrapper> getInitiativeAdapter(Context context) {
        if (initiativeAdapter == null) {
            initiativeAdapter = InitiativeAdapter.fromCrewWrapperList(context, getCrews());
            initiativeAdapter.onInitiativeClick = onInitiativeAdapterClick;
        }
        return initiativeAdapter;
    }

    private InitiativeAdapter.IInitiativeSlotClick onInitiativeAdapterClick = new InitiativeAdapter.IInitiativeSlotClick() {
        @Override
        public void Click(boolean isPlayer, Context context) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            final ArrayList<SWListBoxItem> items = new ArrayList<>();
            if (isPlayer)
                for (CrewWrapper wrapper : getUnplayedPC())
                    items.add(new SWListBoxItem(wrapper.baseNPC.Name, String.format("(%d) %s", wrapper.VehicleID, getVehicle(wrapper).toString())).setImage(wrapper.baseNPC.getImage()).setTag(wrapper));
            else
                for (CrewWrapper wrapper : getUnplayedNPC())
                    items.add(new SWListBoxItem(wrapper.baseNPC.Name, String.format("(%d) %s", wrapper.VehicleID, getVehicle(wrapper).toString())).setImage(wrapper.baseNPC.getImage()).setTag(wrapper));

            builder.setAdapter(new SWListBoxItemAdapter(context, items), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((CrewWrapper)items.get(which).getTag()).Played  = VehicleFighterAdapter.this.initiativeAdapter.Play((CrewWrapper) items.get(which).getTag());
                }
            });
            builder.setTitle("Personnage suivant");
            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.show();

        }
    };


    /**
     * Obtient la liste des personnages joueurs n'ayant pas joué
     * @return Une liste de crewwrapper
     */
    public List<CrewWrapper> getUnplayedPC() {
        ArrayList<CrewWrapper> unplayed = new ArrayList<>();
        for (CrewWrapper crewWrapper : getCrews())
            if (crewWrapper.isOnPlayerSlot && crewWrapper.Played == -1)
                unplayed.add(crewWrapper);

        return unplayed;
    }

    /**
     * Obtient la liste des personnages non joueurs n'ayant pas joué
     * @return Une liste de crewwrapper
     */
    public List<CrewWrapper> getUnplayedNPC() {
        ArrayList<CrewWrapper> unplayed = new ArrayList<>();
        for (CrewWrapper crewWrapper : getCrews())
            if (!crewWrapper.isOnPlayerSlot && crewWrapper.Played == -1)
                unplayed.add(crewWrapper);


        return unplayed;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {



        View baseView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_vehiclefighter, viewGroup, false);



        return new ViewHolder(baseView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        viewHolder.UpdateHolder(i);

    }

    @Override
    public int getItemCount() {
        return Fighters.size();
    }

    public void addVehicle(Vehicle vehicle) {
        VehicleFighter fighter = new VehicleFighter(vehicle);
        addFighter(fighter);

    }

    private void addFighter(VehicleFighter fighter) {
        Fighters.add(fighter);
        for (CrewWrapper wrapper : fighter.getCrew())
            wrapper.Played = -1;
        FinalNotifyDataSetChanged();
    }

    public void removeVehicle(int fighter) {
        Fighters.remove(fighter);
        FinalNotifyDataSetChanged();
    }

    public void removeVehicle(VehicleFighter fighter) {
        Fighters.remove(fighter);
        FinalNotifyDataSetChanged();
    }


    public void NextRound() {
        for (VehicleFighter fighter : Fighters)
            for (CrewWrapper wrapper : fighter.getCrew())
                wrapper.Played = -1;
    }


    public String SaveFight() {

        try {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/SWEotE/VehicleFights/"+Filename);
            boolean result = file.createNewFile();
            if (result) Log.d("hopla", "le fichier " + file.getAbsolutePath() + " existe.");

            FileOutputStream stream = new FileOutputStream(file.getAbsolutePath());
            OutputStreamWriter streamWriter = new OutputStreamWriter(stream, "UTF-8");
            JsonWriter writer = new JsonWriter(streamWriter);
            writer.setIndent("    ");

            writer.beginArray();
            for (VehicleFighter fighter : Fighters)
                fighter.saveJSON(writer);
            writer.endArray();

            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

        return "OK";

    }

    public void LoadFromFile (String Filename) {
        try {

            File file = new File(Filename);

            if (file.exists()) {
                ClearFighters();

                FileInputStream stream = new FileInputStream(file.getAbsolutePath());
                InputStreamReader streamReader = new InputStreamReader(stream, "UTF-8");
                JsonReader reader = new JsonReader(streamReader);

                reader.beginArray();

                while (reader.hasNext())
                    addFighter(VehicleFighter.getFighterFromJSON(reader, App.getContext(), Players));


                reader.endArray();

                this.Filename = Filename;
            }




        }
        catch (Exception e) {
            e.printStackTrace();


        }
    }

    public void ClearFighters() {

        Players = XmlImport.ImportPCs();

        Fighters.clear();
    }

    /**
     * Effectue une phase d'énergie pour tous les vaisseaux
     * @param context Un contexte pour popups
     */
    public void EnergyStep(Context context) {
        for (VehicleFighter fighter : Fighters)
        {
            int iShield = fighter.getActualShieldEnergy() - 1;
            fighter.setActualDefenseFore(fighter.getActualDefenseFore() + (fighter.getBaseVehicle().getDefFore() * 2 * iShield));
            fighter.setActualDefenseAft(fighter.getActualDefenseAft() + (fighter.getBaseVehicle().getDefAft() * 2 * iShield));
            fighter.setActualDefensePort(fighter.getActualDefensePort() + (fighter.getBaseVehicle().getDefPort() * 2 * iShield));
            fighter.setActualDefenseStarboard(fighter.getActualDefenseStarboard() + (fighter.getBaseVehicle().getDefStarboard() * 2 * iShield));


            if (fighter.getActualWeaponsEnergy() == 2) {
                for (VehicleWeapon wp : fighter.getBaseVehicle().getWeapons())
                    wp.setUsed(false);
            }
            else if (fighter.getActualWeaponsEnergy() == 1) {

                final ArrayList<VehicleWeapon> wps = new ArrayList<>();
                for (VehicleWeapon wp : fighter.getBaseVehicle().getWeapons())
                    if (wp.isUsed())
                        wps.add(wp);

                if (wps.size() == 1)
                    wps.get(0).setUsed(false);

                if (wps.size() > 1) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);

                    builder.setTitle("Energie dans les armes");

                    ArrayAdapter<VehicleWeapon> vehicleWeaponArrayAdapter = new ArrayAdapter<>(context, android.R.layout.simple_expandable_list_item_1, wps);
                    builder.setAdapter(vehicleWeaponArrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            wps.get(which).setUsed(false);
                            FinalNotifyDataSetChanged();
                        }
                    });
                    builder.show();
                }
            }

        }

        this.FinalNotifyDataSetChanged();
    }

    public boolean InitiativeOnlyPilot = false;

    public void FinalNotifyDataSetChanged() {
        this.notifyDataSetChanged();

        if (initiativeAdapter != null) { //L'initiativeAdapter n'existe pas lors du chargement d'un fichier

            if (InitiativeOnlyPilot)
                initiativeAdapter.updateData(getPilots());
            else
                initiativeAdapter.updateData(getCrews());
        }


    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView Title;
        private TextView DefFore;
        private TextView DefAft;
        private TextView DefPort;
        private TextView DefStarboard;
        private SeekBar SystemStrain;
        private SeekBar HullTrauma;
        private TextView SystemStrainValue;
        private TextView HullTraumaValue;
        private ImageView SilhouetteImageView;
        private ImageView IconImageView;
        private Button btnDamage;

        private Button btnEnergy;
        private TextView SquadronNumber;
        private ImageView ManeuverImage;
        private TextView ManeuverSpeed;
        private TextView EnergyInfo;
        private View vOverflow;

        private LinearLayout crewLayout;
        private LinearLayout weaponLayout;




        public ViewHolder(View itemView) {
            super(itemView);


            Title = (TextView)itemView.findViewById(R.id.title_text);
            DefFore = (TextView)itemView.findViewById(R.id.DefenseFore);
            DefAft = (TextView)itemView.findViewById(R.id.DefenseAft);
            DefPort = (TextView)itemView.findViewById(R.id.DefensePort);
            DefStarboard = (TextView)itemView.findViewById(R.id.DefenseStarboard);
            SystemStrain = (SeekBar)itemView.findViewById(R.id.SystemStrainSeekBar);
            HullTrauma = (SeekBar)itemView.findViewById(R.id.HullTraumaSeekBar);
            SystemStrainValue = (TextView)itemView.findViewById(R.id.SystemStrainValueTextView);
            HullTraumaValue = (TextView)itemView.findViewById(R.id.HullTraumaValueTextView);
            SilhouetteImageView = (ImageView)itemView.findViewById(R.id.SilhouetteImageView);
            IconImageView = (ImageView)itemView.findViewById(R.id.IconImageView);
            btnDamage = (Button)itemView.findViewById(R.id.btnDamage);
            btnEnergy = (Button)itemView.findViewById(R.id.btnEnergy);
            crewLayout = (LinearLayout)itemView.findViewById(R.id.lstCrew);
            SquadronNumber = (TextView)itemView.findViewById(R.id.txtSquadronNumber);
            ManeuverImage = (ImageView)itemView.findViewById(R.id.ManeuverImage);
            ManeuverSpeed = (TextView)itemView.findViewById(R.id.ManeuverSpeed);
            EnergyInfo = (TextView)itemView.findViewById(R.id.txtEnergy);
            vOverflow = itemView.findViewById(R.id.mnuOverflow);


        }




        public void UpdateHolder(final int FighterID) {
            final VehicleFighter fighter = Fighters.get(FighterID);
            Title.setText(fighter.getBaseVehicle().getName());
            DefFore.setText(String.valueOf(fighter.getActualDefenseFore()));
            DefAft.setText(String.valueOf(fighter.getActualDefenseAft()));
            if (fighter.getBaseVehicle().getSilhouette() >= 5) {
                DefPort.setText(String.valueOf(fighter.getActualDefensePort()));
                DefStarboard.setText(String.valueOf(fighter.getActualDefenseStarboard()));
            }
            else {
                DefPort.setText("");
                DefStarboard.setText("");
            }
            SystemStrain.setMax(fighter.getTotalSystemStrain());
            SystemStrain.setProgress(fighter.getActualSystemStrain());
            SystemStrainValue.setText(String.valueOf(fighter.getActualSystemStrain()));
            HullTrauma.setMax(fighter.getTotalHullTrauma());
            HullTrauma.setProgress(fighter.getActualHullTrauma());
            HullTraumaValue.setText(String.valueOf(fighter.getActualHullTrauma()));
            SquadronNumber.setText(String.valueOf(FighterID + 1));
            EnergyInfo.setText(String.format("Armes: %d Boucliers: %d Moteurs: %d", fighter.getActualWeaponsEnergy(), fighter.getActualShieldEnergy(), fighter.getActualEngineEnergy()));
            SilhouetteImageView.setImageBitmap(fighter.getBaseVehicle().getImageSilhouette());
            IconImageView.setImageBitmap(fighter.getBaseVehicle().getImageIcon());

            int[][] man = ManeuverInfos.getManeuvers(fighter.getBaseVehicle().getManeuverMapName());
            if (fighter.getSelectedManeuver() != -1)
            {
                int diff = man[fighter.getSelectedSpeed()][fighter.getSelectedManeuver()];
                if (diff != 0)
                    ManeuverImage.setImageResource(ManeuverInfos.GetManeuverRessource(fighter.getSelectedManeuver(), diff - 1));
                ManeuverSpeed.setText(String.valueOf(fighter.getSelectedSpeed()));
            }

            crewLayout.removeAllViews();
            DisplayMetrics metrics = crewLayout.getContext().getResources().getDisplayMetrics();
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, metrics),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, metrics));
            int MarginDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, metrics);


            param.setMargins(MarginDP, MarginDP, MarginDP, MarginDP);
            for (final CrewWrapper npc : fighter.getCrew())
            {
                ImageView view = new ImageView(crewLayout.getContext());
                view.setImageBitmap(npc.baseNPC.getImage());
                view.setLayoutParams(param);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (npc.isPlayer)
                            Players.add(npc.baseNPC);

                        fighter.removeCrew(npc);
                        VehicleFighterAdapter.this.FinalNotifyDataSetChanged();

                    }
                });
                crewLayout.addView(view);
            }




            SystemStrain.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    SystemStrainValue.setText(String.valueOf(progress));
                    if (fromUser) Fighters.get(FighterID).setActualSystemStrain(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    //vide
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    //vide

                }
            });


            HullTrauma.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    HullTraumaValue.setText(String.valueOf(progress));
                    if (fromUser) {
                        Fighters.get(FighterID).setActualHullTrauma(progress);

                    }


                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    //vide

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    //vide

                }
            });





            SilhouetteImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DefenseSwitchPopup.Show(SilhouetteImageView.getContext(), Fighters.get(FighterID), new DefenseSwitchPopup.onValidatePopupListener() {
                        @Override
                        public void onValidatePopup() {
                            VehicleFighterAdapter.this.FinalNotifyDataSetChanged();

                        }
                    });
                }
            });


            btnDamage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VehiculeFighterDamagePopup.Show(btnDamage.getContext(), Fighters.get(FighterID), new VehiculeFighterDamagePopup.onValidatePopupListener() {
                        @Override
                        public void onValidatePopup() {
                            VehicleFighterAdapter.this.FinalNotifyDataSetChanged();

                        }
                    });
                }
            });

            btnEnergy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VehicleFighterEnergyPopup.Show(btnDamage.getContext(), Fighters.get(FighterID), new DefenseSwitchPopup.onValidatePopupListener() {
                        @Override
                        public void onValidatePopup() {
                            VehicleFighterAdapter.this.FinalNotifyDataSetChanged();

                        }
                    });
                }
            });

            vOverflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu menu = new PopupMenu(v.getContext(), v);
                    menu.getMenuInflater().inflate(R.menu.vehiculefighterfenu, menu.getMenu());
                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            if (menuItem.getItemId() == R.id.mnuRemove) {
                                for (CrewWrapper wrapper : Fighters.get(FighterID).getCrew()) {
                                    if (wrapper.isPlayer)
                                        VehicleFighterAdapter.this.Players.add(wrapper.baseNPC);

                                    Fighters.get(FighterID).removeCrew(wrapper);


                                }

                                VehicleFighterAdapter.this.removeVehicle(FighterID);


                            } else if (menuItem.getItemId() == R.id.mnuDuplicate) {
                                //Duplicate
                                VehicleFighter duplicate = Fighters.get(FighterID).Clone();
                                Fighters.add(duplicate);
                                NextRound();
                                FinalNotifyDataSetChanged();

                                return true;
                            }
                            else if (menuItem.getItemId() == R.id.mnuEdit)
                            {
                                GenericEditor.Show(mContext, Fighters.get(FighterID), new GenericEditor.IOnPopupClosed() {
                                    @Override
                                    public void OnClosed() {

                                        FinalNotifyDataSetChanged();
                                    }
                                });



                            }
                            else if (menuItem.getItemId() == R.id.mnuSetAsAllyOrEnnemy)
                            {
                                for  (CrewWrapper wrapper: Fighters.get(FighterID).getCrew())
                                    wrapper.isOnPlayerSlot = !wrapper.isOnPlayerSlot;

                                FinalNotifyDataSetChanged();
                            }


                            return false;
                        }
                    });
                    menu.show();
                }
            });

            weaponLayout = (LinearLayout)itemView.findViewById(R.id.WeaponList);
            weaponLayout.removeAllViews();

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(2, 2, 2, 2);

            for (final VehicleWeapon wp : Fighters.get(FighterID).getBaseVehicle().getWeapons())
            {
                TextView txt = new TextView(weaponLayout.getContext());
                txt.setText(wp.toString());
                txt.setTextSize(23);
                if (wp.isUsed()) txt.setTextSize(12);
                txt.setLayoutParams(params);
                txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<SWListBoxItem> npcArrayList = new ArrayList<>();
                        for (CrewWrapper wrapper : Fighters.get(FighterID).getCrew())
                            npcArrayList.add(new SWListBoxItem(wrapper.baseNPC.Name, wrapper.baseNPC.Description).setImage(wrapper.baseNPC.getImage()).setTag(wrapper));
                        final SWListBoxItemAdapter adapter = new SWListBoxItemAdapter(weaponLayout.getContext(), npcArrayList);

                        new AlertDialog.Builder(weaponLayout.getContext())
                                .setTitle(R.string.select_gunner)
                                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //target
                                        final CrewWrapper Gunner = (CrewWrapper)((SWListBoxItem)adapter.getItem(which)).getTag();
                                        final SWListBoxItemAdapter items = CreateVehicleList(weaponLayout.getContext(), Fighters, FighterID);
                                        new AlertDialog.Builder(weaponLayout.getContext())
                                                .setTitle("Cible:")
                                                .setAdapter(items, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, final int which) {
                                                        if (Gunner.isPlayer) {
                                                            RollResult pilot;
                                                            if (((VehicleFighter)((SWListBoxItem)items.getItem(which)).getTag()).getCrew().size() > 0)
                                                                pilot = ((VehicleFighter)((SWListBoxItem)items.getItem(which)).getTag()).getCrew().get(0).baseNPC.getSkillRoll(Skill.Skills.piloting_space, new Random().nextInt(), 0);
                                                            else
                                                                pilot = new RollResult();


                                                            pilot.InvertDices();

                                                            TextView baseTextView = new TextView(weaponLayout.getContext());
                                                            Util.setTextViewSymbols(baseTextView, "Difficulté: " + pilot.toSymbolFormattableString());
                                                            LinearLayout.LayoutParams layoutParams = new ActionMenuView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                                                            layoutParams.gravity = Gravity.CENTER;
                                                            LinearLayout linearLayout = new LinearLayout(weaponLayout.getContext());
                                                            linearLayout.setLayoutParams(layoutParams);
                                                            linearLayout.addView(baseTextView);
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(weaponLayout.getContext());

                                                            builder.setView(linearLayout);

                                                            builder.setTitle(String.format("%s %s", weaponLayout.getContext().getString(R.string.action_attack_on), ((SWListBoxItem) items.getItem(which)).getName()));
                                                                    builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialog, int which2) {
                                                                            VehiculeFighterDamagePopup.Show(btnDamage.getContext(), (VehicleFighter) ((SWListBoxItem) items.getItem(which)).getTag(), 0, new VehiculeFighterDamagePopup.onValidatePopupListener() {
                                                                                @Override
                                                                                public void onValidatePopup() {
                                                                                    wp.setUsed(true);
                                                                                    VehicleFighterAdapter.this.FinalNotifyDataSetChanged();

                                                                                }
                                                                            });
                                                                            dialog.dismiss();
                                                                        }
                                                                    });
                                                            builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {
                                                                    dialog.dismiss();
                                                                }
                                                            });
                                                            builder.show();

                                                        }
                                                        else
                                                            VehicleAttackPopup.Show(weaponLayout.getContext(), (VehicleFighter)((SWListBoxItem)items.getItem(which)).getTag(), Gunner.baseNPC, wp, new VehicleAttackPopup.onAttackListener() {
                                                                @Override
                                                                public void onSuccessfullAttack(int RealDamage) {
                                                                    VehiculeFighterDamagePopup.Show(btnDamage.getContext(), (VehicleFighter)((SWListBoxItem)items.getItem(which)).getTag(), RealDamage, new VehiculeFighterDamagePopup.onValidatePopupListener() {
                                                                        @Override
                                                                        public void onValidatePopup() {
                                                                            wp.setUsed(true);
                                                                            VehicleFighterAdapter.this.FinalNotifyDataSetChanged();

                                                                        }
                                                                    });
                                                                }

                                                                @Override
                                                                public void onUnsuccessfullAttack() {
                                                                    wp.setUsed(true);
                                                                    VehicleFighterAdapter.this.FinalNotifyDataSetChanged();
                                                                }
                                                            });
                                                    }
                                                })
                                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .create().show();

                                    }
                                })
                                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }).create().show();

                    }
                });
                weaponLayout.addView(txt);

            }

            itemView.findViewById(R.id.imgAddUser).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VehicleCrewPopup.Show(itemView.getContext(), new VehicleCrewPopup.onValidatePopupListener() {
                        @Override
                        public void onValidatePopup(Object Result) {
                            Fighters.get(FighterID).addCrew((NPC)Result, false);
                            NextRound();
                            VehicleFighterAdapter.this.FinalNotifyDataSetChanged();


                        }
                    });
                }
            });

            itemView.findViewById(R.id.imgAddPlayer).setVisibility(View.VISIBLE);
            if (Players.size() != 0)
                itemView.findViewById(R.id.imgAddPlayer).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        VehicleCrewPopup.Show(itemView.getContext(), new VehicleCrewPopup.onValidatePopupListener() {
                            @Override
                            public void onValidatePopup(final Object Result) {

                                InitiativePopup.Show(itemView.getContext(), ((NPC)Result).Name, new InitiativePopup.IOnValidateInitiative() {
                                    @Override
                                    public void OnValidate(int Triumph, int Success, int Advantage) {
                                        CrewWrapper wrapper = Fighters.get(FighterID).addCrew((NPC) Result, true);
                                        wrapper.initiative.Triumph = Triumph;
                                        wrapper.initiative.Success = Success;
                                        wrapper.initiative.Advantages = Advantage;
                                        Players.remove(Result);

                                        NextRound();
                                        VehicleFighterAdapter.this.FinalNotifyDataSetChanged();

                                    }
                                });


                            }
                        }, Players);
                    }
                });
            else
                itemView.findViewById(R.id.imgAddPlayer).setVisibility(View.GONE);

        }
    }

    public static SWListBoxItemAdapter CreateVehicleList(Context context, List<VehicleFighter> Fighters, int IgnoreIndex) {
        List<SWListBoxItem> items = new ArrayList<>();
        for(VehicleFighter fighter : Fighters)
            if (Fighters.indexOf(fighter) != IgnoreIndex)
                items.add(new SWListBoxItem(fighter.getBaseVehicle().getName(),
                    String.format("Armure %d, Coque %d, Système %d", fighter.getBaseVehicle().getArmor(), fighter.getActualHullTrauma(), fighter.getActualSystemStrain()))
                    .setImage(fighter.getBaseVehicle().getImageIcon())
                    .setTag(fighter));

        return new SWListBoxItemAdapter(context, items);

    }


}
