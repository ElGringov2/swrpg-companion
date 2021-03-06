package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.dragonrider.swrpgcompanion.R;

/**
 * Created by mge637 on 02/03/2015.
 * Gere les dégats d'un véhicule
 */
public class VehiculeFighterDamagePopup {

    static final RadioGroup.OnCheckedChangeListener ToggleListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(final RadioGroup radioGroup, final int i) {
            for (int j = 0; j < radioGroup.getChildCount(); j++) {
                final ToggleButton view = (ToggleButton) radioGroup.getChildAt(j);
                if (view.getId() == i) {
                    view.setChecked(true);
                    mIgnoreArmor = j;
                }
                else {
                    view.setChecked(false);
                }
            }

        }
    };






    private static int mIgnoreArmor = 0;
    private static int iDamage = 0;

    public interface onValidatePopupListener {
        void onValidatePopup() ;
    }



    public static void Show(Context context, VehicleFighter fighter, onValidatePopupListener onvalidatePopupListener) {
        Show(context, fighter, 0, onvalidatePopupListener);
    }

    private static TextView txtDamage;
    private static TextView txtFinal;

    static RadioButton rdoFore;
    static RadioButton rdoAft;
    static RadioButton rdoPort;
    static RadioButton rdoStarboard;



    static int mFighterArmor;

    public static void Show(final Context context, final VehicleFighter fighter, int Damage, final onValidatePopupListener onvalidatePopupListener) {

        View baseView = LayoutInflater.from(context).inflate(R.layout.vehicle_damage_popup, null);


        rdoFore = (RadioButton) baseView.findViewById(R.id.rdoFore);
        rdoAft = (RadioButton) baseView.findViewById(R.id.rdoAft);
        rdoPort = (RadioButton) baseView.findViewById(R.id.rdoPort);
        rdoStarboard = (RadioButton) baseView.findViewById(R.id.rdoStarboard);

        if (fighter.getBaseVehicle().getSilhouette() <= 4) {
            rdoStarboard.setVisibility(View.INVISIBLE);
            rdoPort.setVisibility(View.INVISIBLE);
        }

        rdoFore.setText(String.format("Avant %d", fighter.getActualDefenseFore()));
        rdoAft.setText(String.format("Arrière %d", fighter.getActualDefenseAft()));
        rdoPort.setText(String.format("Bâbord %d", fighter.getActualDefensePort()));
        rdoStarboard.setText(String.format("Tribord %d", fighter.getActualDefenseStarboard()));



        iDamage = Damage ;
        mFighterArmor = fighter.getBaseVehicle().getArmor();


        txtDamage = (TextView) baseView.findViewById(R.id.txtDamage);
        txtFinal = (TextView) baseView.findViewById(R.id.txtFinalDamage);

        final SeekBar bar = (SeekBar)baseView.findViewById(R.id.skDamage);

        bar.setMax(iDamage + 10);
        bar.setProgress(Damage);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((RadioGroup)v.getParent()).check(v.getId());
                mIgnoreArmor = Integer.valueOf((String)((ToggleButton)v).getText());
                UpdateText();
            }
        };

        ((RadioGroup) baseView.findViewById(R.id.toggleGroup)).setOnCheckedChangeListener(ToggleListener);

        baseView.findViewById(R.id.btn_ignorearmor8).setOnClickListener(listener);
        baseView.findViewById(R.id.btn_ignorearmor7).setOnClickListener(listener);
        baseView.findViewById(R.id.btn_ignorearmor6).setOnClickListener(listener);
        baseView.findViewById(R.id.btn_ignorearmor5).setOnClickListener(listener);
        baseView.findViewById(R.id.btn_ignorearmor4).setOnClickListener(listener);
        baseView.findViewById(R.id.btn_ignorearmor3).setOnClickListener(listener);
        baseView.findViewById(R.id.btn_ignorearmor2).setOnClickListener(listener);
        baseView.findViewById(R.id.btn_ignorearmor1).setOnClickListener(listener);
        baseView.findViewById(R.id.btn_ignorearmor0).setOnClickListener(listener);



        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                iDamage = progress;

                UpdateText();
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

        UpdateText();


        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(baseView);

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (rdoFore.isChecked()) {
                    if (iDamage > fighter.getActualDefenseFore()) {
                        iDamage -= fighter.getActualDefenseFore();
                        fighter.setActualDefenseFore(0);
                    }
                    else {
                        fighter.setActualDefenseFore(fighter.getActualDefenseFore() - iDamage);
                        iDamage = 0;
                    }
                }
                if (rdoAft.isChecked()) {
                    if (iDamage > fighter.getActualDefenseAft()) {
                        iDamage -= fighter.getActualDefenseAft();
                        fighter.setActualDefenseAft(0);
                    }
                    else {
                        fighter.setActualDefenseAft(fighter.getActualDefenseAft() - iDamage);
                        iDamage = 0;
                    }
                }
                if (rdoPort.isChecked()) {
                    if (iDamage > fighter.getActualDefensePort()) {
                        iDamage -= fighter.getActualDefensePort();
                        fighter.setActualDefensePort(0);
                    }
                    else {
                        fighter.setActualDefensePort(fighter.getActualDefensePort() - iDamage);
                        iDamage = 0;
                    }
                }
                if (rdoStarboard.isChecked()) {
                    if (iDamage > fighter.getActualDefenseStarboard()) {
                        iDamage -= fighter.getActualDefenseStarboard();
                        fighter.setActualDefenseStarboard(0);
                    }
                    else {
                        fighter.setActualDefenseStarboard(fighter.getActualDefenseStarboard() - iDamage);
                        iDamage = 0;
                    }
                }
                int iArmor = fighter.getBaseVehicle().getArmor() - mIgnoreArmor;
                if (iArmor < 0) iArmor = 0;
                iDamage -= iArmor;
                if (iDamage >0)
                    fighter.setActualHullTrauma(fighter.getActualHullTrauma() - iDamage);

                if (fighter.getActualHullTrauma() <= 0)
                {
                    new AlertDialog.Builder(context)
                            .setMessage(R.string.target_has_no_hull_remove_question)
                            .setTitle(R.string.remove)
                            .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    for (CrewWrapper wrapper : fighter.getCrew())
                                        if (wrapper.isPlayer)
                                            VehicleFightActivity.adapter.Players.add(wrapper.baseNPC);

                                    VehicleFightActivity.adapter.removeVehicle(fighter);
                                }
                            })
                            .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .show();
                }

                onvalidatePopupListener.onValidatePopup();
                dialog.dismiss();
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        builder.create().show();




    }

    private static void UpdateText() {


        int iIgnoreArmor = mFighterArmor - mIgnoreArmor;
        if (iIgnoreArmor < 0)
            iIgnoreArmor = 0;

        int realDamage = iDamage - iIgnoreArmor;
        if (realDamage < 0)
            realDamage = 0;

        txtDamage.setText(String.valueOf(iDamage));
        txtFinal.setText(String.format("%s Dégât%s", realDamage, realDamage > 1 ? "s" : ""));


    }
}
