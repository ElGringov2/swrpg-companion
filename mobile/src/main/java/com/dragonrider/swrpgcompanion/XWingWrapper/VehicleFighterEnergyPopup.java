package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.method.CharacterPickerDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.R;

/**
 * Created by mge637 on 29/07/2015. j
 */
public class VehicleFighterEnergyPopup {
    static TextView txtShield;
    static TextView txtEngines;
    static TextView txtWeapons;

    static VehicleFighter mVehicle;


    public static void Show(Context context, VehicleFighter vehicleFighter, final DefenseSwitchPopup.onValidatePopupListener onPopupValidateListener) {
        mVehicle = vehicleFighter;
        @SuppressLint("InflateParams") View baseView = LayoutInflater.from(context).inflate(R.layout.popup_vehicle_energy, null);


        AlertDialog.Builder dialog = new AlertDialog.Builder(context);

        dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ValidateEnergy();
                onPopupValidateListener.onValidatePopup();
            }
        });

        dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        txtEngines = (TextView)baseView.findViewById(R.id.textView2);
        txtWeapons = (TextView)baseView.findViewById(R.id.textView3);
        txtShield = (TextView)baseView.findViewById(R.id.textView1);




        txtEngines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iTargetEnergy == -1 && mVehicle.getActualEngineEnergy() > 0)
                    iTargetEnergy = 1;
                else if (iTargetEnergy != 1 && mVehicle.getActualEngineEnergy() < 2)
                    updateEnergy(iTargetEnergy, 1);

                updateColors();
            }
        });

        txtShield.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iTargetEnergy == -1 && mVehicle.getActualShieldEnergy() > 0)
                    iTargetEnergy = 2;
                else if (iTargetEnergy != 2 && mVehicle.getActualShieldEnergy() < 2)
                    updateEnergy(iTargetEnergy, 2);

                updateColors();
            }
        });


        txtWeapons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iTargetEnergy == -1 && mVehicle.getActualWeaponsEnergy() > 0)
                    iTargetEnergy = 3;
                else if (iTargetEnergy != 3 && mVehicle.getActualWeaponsEnergy() < 2)
                    updateEnergy(iTargetEnergy, 3);

                updateColors();
            }
        });


        iTargetEnergy = -1;
        updateColors();


        if (vehicleFighter.getBaseVehicle().getDefFore() +
                vehicleFighter.getBaseVehicle().getDefAft()+
                vehicleFighter.getBaseVehicle().getDefPort() +
                vehicleFighter.getBaseVehicle().getDefStarboard() == 0)
        txtShield.setVisibility(View.INVISIBLE);

        if (vehicleFighter.getBaseVehicle().getWeapons().size() == 0)
            txtWeapons.setVisibility(View.INVISIBLE);

        dialog.setView(baseView);
        dialog.show();
    }

    static int iTargetEnergy;

    private static void updateColors() {
        txtEngines.setBackgroundResource(R.drawable.gradient_defense_impossible);
        txtShield.setBackgroundResource(R.drawable.gradient_defense_impossible);
        txtWeapons.setBackgroundResource(R.drawable.gradient_defense_impossible);


        txtEngines.setText(String.valueOf(mVehicle.getActualEngineEnergy()));
        txtWeapons.setText(String.valueOf(mVehicle.getActualWeaponsEnergy()));
        txtShield.setText(String.valueOf(mVehicle.getActualShieldEnergy()));

        if (iTargetEnergy == -1) {
            if (mVehicle.getActualEngineEnergy() >= 1)
                txtEngines.setBackgroundResource(R.drawable.gradient_defense_select);
            if (mVehicle.getActualShieldEnergy() >= 1)
                txtShield.setBackgroundResource(R.drawable.gradient_defense_select);
            if (mVehicle.getActualWeaponsEnergy() >= 1)
                txtWeapons.setBackgroundResource(R.drawable.gradient_defense_select);
        }
        else {
            if (iTargetEnergy == 1)
                txtEngines.setBackgroundResource(R.drawable.gradient_defense_destination);
            if (iTargetEnergy == 2)
                txtShield.setBackgroundResource(R.drawable.gradient_defense_destination);
            if (iTargetEnergy == 3)
                txtWeapons.setBackgroundResource(R.drawable.gradient_defense_destination);

            if (mVehicle.getActualEngineEnergy() < 2 && iTargetEnergy != 1)
                txtEngines.setBackgroundResource(R.drawable.gradient_defense_select);
            if (mVehicle.getActualShieldEnergy() < 2 && iTargetEnergy != 2)
                txtShield.setBackgroundResource(R.drawable.gradient_defense_select);
            if (mVehicle.getActualWeaponsEnergy() < 2 && iTargetEnergy != 3)
                txtWeapons.setBackgroundResource(R.drawable.gradient_defense_select);

        }
    }

    private static void updateEnergy(int from, int to) {
        if (to == 1) mVehicle.setActualEngineEnergy(mVehicle.getActualEngineEnergy() + 1);
        if (from == 1) mVehicle.setActualEngineEnergy(mVehicle.getActualEngineEnergy() - 1);

        if (to == 2) mVehicle.setActualShieldEnergy(mVehicle.getActualShieldEnergy() + 1);
        if (from == 2) mVehicle.setActualShieldEnergy(mVehicle.getActualShieldEnergy() - 1);

        if (to == 3) mVehicle.setActualWeaponsEnergy(mVehicle.getActualWeaponsEnergy() + 1);
        if (from == 3) mVehicle.setActualWeaponsEnergy(mVehicle.getActualWeaponsEnergy() - 1);

        iTargetEnergy = -1;
    }


    private static void ValidateEnergy() {

    }
}
