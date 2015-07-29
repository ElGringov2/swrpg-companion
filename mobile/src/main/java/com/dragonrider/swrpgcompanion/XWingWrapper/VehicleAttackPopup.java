package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.Classes.DiceRoller;
import com.dragonrider.swrpgcompanion.Classes.NPC;
import com.dragonrider.swrpgcompanion.Classes.RollResult;
import com.dragonrider.swrpgcompanion.Classes.Skill;
import com.dragonrider.swrpgcompanion.R;

/**
 * Created by mge637 on 03/03/2015.
 *
 */
public class VehicleAttackPopup {


    private static RollResult rollResult;
    private static TextView txtResult;

    private static int iMinDamage = 0;
    private static int iDamage = 0;


    public interface onSuccessfulAttackListener {
        public void onSuccessfulAttack(int RealDamage) ;
    }

    public static void Show(final Context context, VehicleFighter Target, NPC Shooter, VehicleWeapon Weapon, final onSuccessfulAttackListener listener) {


        @SuppressLint("InflateParams")
        final View baseView = LayoutInflater.from(context).inflate(R.layout.vehicleattack_popup, null);


        txtResult = (TextView)baseView.findViewById(R.id.txtResult);


        final LinearLayout llDices = (LinearLayout)baseView.findViewById(R.id.llDices);
        final LinearLayout llResults = (LinearLayout)baseView.findViewById(R.id.llResults);


        iMinDamage = Weapon.getDamage();




        rollResult = Shooter.getSkillRoll(Skill.Skills.gunnery);



        final TextView txtDamage = (TextView)baseView.findViewById(R.id.txtDamage);

        final SeekBar bar = (SeekBar) baseView.findViewById(R.id.skDamage);

        bar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                iDamage = progress + iMinDamage;
                txtDamage.setText(String.valueOf(iDamage));

                UpdateTextView();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Vide
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Vide
            }
        });
        bar.setMax(Target.getBaseVehicle().getArmor() + (Target.getTotalHullTrauma() - Target.getActualHullTrauma()));





        baseView.findViewById(R.id.btnChangeRollResult).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DiceRoller roller = new DiceRoller(context);
                roller.setRollResult(rollResult);
                roller.setValidate(new DiceRoller.IValidateRoll() {
                    @Override
                    public void validate(RollResult rr) {
                        rollResult = rr;
                        populateLinearLayouts(llDices, llResults, bar);

                    }
                });
                roller.ShowAlertDialog();
            }
        });




        populateLinearLayouts(llDices, llResults, bar);





        new AlertDialog.Builder(context)
                .setView(baseView)
                .setTitle("Attaque")
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onSuccessfulAttack(iDamage);
                        dialog.dismiss();
                    }
                })
                .create().show();

    }

    private static void populateLinearLayouts(LinearLayout llDices, LinearLayout llResults, SeekBar DamageSeekBar) {
        rollResult.PopulateLightView(llDices);

        rollResult.PopulateResult(llResults);

        int progress = rollResult.Succcess() - 1;
        if (progress < 0) progress = 0;
        DamageSeekBar.setProgress(progress);

    }

    private static void UpdateTextView() {
        String text = "Résultat:";

        int success = rollResult.Succcess();
        if (success > 0) {
            text += " Touché: la cible subit " + String.valueOf(iDamage) + " dégâts.\n";

        }
        else
            text += " Raté.";




        txtResult.setText(text);
    }
}
