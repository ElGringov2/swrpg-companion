package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.Classes.App;
import com.dragonrider.swrpgcompanion.Classes.NPC;
import com.dragonrider.swrpgcompanion.Classes.SWListBoxItem;
import com.dragonrider.swrpgcompanion.Classes.SWListBoxItemAdapter;
import com.dragonrider.swrpgcompanion.Classes.XmlImport;
import com.dragonrider.swrpgcompanion.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by mge637 on 01/03/2015.
 * Gere la liste des vehicules dans un combat.
 */
public class VehicleFighterAdapter extends RecyclerView.Adapter<VehicleFighterAdapter.ViewHolder>  {

    private String Filename = "";

    public String getFilename() {
        return Filename;
    }
    public void setFilename(String value) {
        this.Filename = value;
    }

    private List<VehicleFighter> Fighters;

    /**
     * Constructeur
     *
     */
    public VehicleFighterAdapter(List<VehicleFighter> fighters) {
        Fighters = fighters;
        Players = XmlImport.ImportPCs();
    }



    private List<NPC> Players;


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
        Fighters.add(new VehicleFighter(vehicle));
        notifyDataSetChanged();
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
                    Fighters.add(VehicleFighter.getFighterFromJSON(reader, App.getContext(), Players));


                reader.endArray();
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
        private Button btnDefense;
        private TextView SquadronNumber;

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
            btnDefense = (Button)itemView.findViewById(R.id.btnSwitchDefense);
            crewLayout = (LinearLayout)itemView.findViewById(R.id.lstCrew);
            SquadronNumber = (TextView)itemView.findViewById(R.id.txtSquadronNumber);




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
            SquadronNumber.setText(String.valueOf(FighterID+1));

            SilhouetteImageView.setImageBitmap(fighter.getBaseVehicle().getImageSilhouette());
            IconImageView.setImageBitmap(fighter.getBaseVehicle().getImageIcon());


            crewLayout.removeAllViews();
            DisplayMetrics metrics = crewLayout.getContext().getResources().getDisplayMetrics();
            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, metrics),
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 64, metrics));
            int MarginDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, metrics);


            param.setMargins(MarginDP,MarginDP,MarginDP,MarginDP);
            for (final VehicleFighter.CrewWrapper npc : fighter.getCrew())
            {
                ImageView view = new ImageView(crewLayout.getContext());
                view.setImageBitmap(npc.baseNPC.getImage());
                view.setLayoutParams(param);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (npc.IsPlayer)
                            Players.add(npc.baseNPC);

                        fighter.removeCrew(npc);
                        VehicleFighterAdapter.this.notifyDataSetChanged();

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
                    if (fromUser) Fighters.get(FighterID).setActualHullTrauma(progress);
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





            btnDefense.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DefenseSwitchPopup.Show(btnDefense.getContext(), Fighters.get(FighterID), new DefenseSwitchPopup.onValidatePopupListener() {
                        @Override
                        public void onValidatePopup() {
                            VehicleFighterAdapter.this.notifyDataSetChanged();

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
                            VehicleFighterAdapter.this.notifyDataSetChanged();

                        }
                    });
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
                txt.setLayoutParams(params);
                txt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ArrayList<SWListBoxItem> npcArrayList = new ArrayList<>();
                        for (VehicleFighter.CrewWrapper wrapper : Fighters.get(FighterID).getCrew())
                            npcArrayList.add(NPC.createNPCItem(wrapper.baseNPC));
                        final SWListBoxItemAdapter adapter = new SWListBoxItemAdapter(weaponLayout.getContext(), npcArrayList);

                        new AlertDialog.Builder(weaponLayout.getContext())
                                .setTitle(R.string.select_gunner)
                                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //target
                                        final NPC Gunner = (NPC)((SWListBoxItem)adapter.getItem(which)).getTag();
                                        final SWListBoxItemAdapter items = CreateVehicleList(weaponLayout.getContext(), Fighters, FighterID);
                                        new AlertDialog.Builder(weaponLayout.getContext())
                                                .setTitle("Cible:")
                                                .setAdapter(items, new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, final int which) {
                                                        VehicleAttackPopup.Show(weaponLayout.getContext(), (VehicleFighter)((SWListBoxItem)items.getItem(which)).getTag(), Gunner, wp, new VehicleAttackPopup.onSuccessfulAttackListener() {
                                                            @Override
                                                            public void onSuccessfulAttack(int RealDamage) {
                                                                VehiculeFighterDamagePopup.Show(btnDamage.getContext(), (VehicleFighter)((SWListBoxItem)items.getItem(which)).getTag(), RealDamage, new VehiculeFighterDamagePopup.onValidatePopupListener() {
                                                                    @Override
                                                                    public void onValidatePopup() {
                                                                        VehicleFighterAdapter.this.notifyDataSetChanged();

                                                                    }
                                                                });
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
                            VehicleFighterAdapter.this.notifyDataSetChanged();


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
                            public void onValidatePopup(Object Result) {
                                Fighters.get(FighterID).addCrew((NPC)Result, true);
                                Players.remove(Result);
                                VehicleFighterAdapter.this.notifyDataSetChanged();

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
