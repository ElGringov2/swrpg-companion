package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.R;

/**
 * Created by mge637 on 02/03/2015.
 */
public class DefenseSwitchPopup {

    private static AlertDialog basePopup;

    public interface onValidatePopupListener {
        void onValidatePopup();
    }


    static TextView txtAft;
    static TextView txtFore;
    static TextView txtPort;
    static TextView txtStarboard;


    public static void Show(Context context, final VehicleFighter vehicle, final onValidatePopupListener onValidatePopupListener) {

        @SuppressLint("InflateParams") View baseView = LayoutInflater.from(context).inflate(R.layout.popup_defense_switch, null);

        iTargetDefense = -1;

        txtAft = (TextView) baseView.findViewById(R.id.txtAft);
        txtFore = (TextView) baseView.findViewById(R.id.txtFore);
        txtPort = (TextView) baseView.findViewById(R.id.txtPort);
        txtStarboard = (TextView) baseView.findViewById(R.id.txtStarboard);

        mVehicle = vehicle;


//        txtPort.setBackgroundResource(R.drawable.gradient_defense_impossible);
//        if (vehicle.getBaseVehicle().getSilhouette() > 4) {
//            txtPort.setText(String.valueOf(vehicle.getActualDefensePort()));
//            if (vehicle.getActualDefensePort() > 0)
//                txtPort.setBackgroundResource(R.drawable.gradient_defense_destination);
//        }
//
//        txtStarboard.setBackgroundResource(R.drawable.gradient_defense_impossible);
//        if (vehicle.getBaseVehicle().getSilhouette() > 4) {
//            txtStarboard.setText(String.valueOf(vehicle.getActualDefenseStarboard()));
//            if (vehicle.getActualDefenseStarboard() > 0)
//                txtPort.setBackgroundResource(R.drawable.gradient_defense_destination);
//        }
//
//        txtAft.setBackgroundResource(R.drawable.gradient_defense_impossible);
//        if (vehicle.getActualDefenseAft() > 0)
//            txtAft.setBackgroundResource(R.drawable.gradient_defense_destination);
//
//        txtFore.setBackgroundResource(R.drawable.gradient_defense_impossible);
//        if (vehicle.getActualDefenseFore() > 0)
//            txtFore.setBackgroundResource(R.drawable.gradient_defense_destination);


        txtAft.setText(String.valueOf(vehicle.getActualDefenseAft()));
        txtFore.setText(String.valueOf(vehicle.getActualDefenseFore()));
        txtPort.setText(String.valueOf(vehicle.getActualDefensePort()));
        txtStarboard.setText(String.valueOf(vehicle.getActualDefenseStarboard()));

        if (vehicle.getBaseVehicle().getSilhouette() <= 4) {
            txtStarboard.setVisibility(View.INVISIBLE);
            txtPort.setVisibility(View.INVISIBLE);
        }

        txtAft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (iTargetDefense == -1 && vehicle.getActualDefenseAft() > 0)
                    iTargetDefense = 0;
                else if (iTargetDefense != -1 && iTargetDefense != 0) {
                    UpdateDefense(iTargetDefense, 0);
                    iTargetDefense = -1;
                }
                updateColors();
            }
        });

        txtFore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iTargetDefense == -1 && vehicle.getActualDefenseFore() > 0)
                    iTargetDefense = 1;
                else if (iTargetDefense != -1 && iTargetDefense != 1) {
                    UpdateDefense(iTargetDefense, 1);
                    iTargetDefense = -1;
                }

                updateColors();
            }
        });

        txtPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iTargetDefense == -1 && vehicle.getActualDefensePort() > 0)
                    iTargetDefense = 2;
                else if (iTargetDefense != -1 && iTargetDefense != 2) {
                    UpdateDefense(iTargetDefense, 2);
                    iTargetDefense = -1;
                }

                updateColors();
            }
        });

        txtStarboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iTargetDefense == -1 && vehicle.getActualDefenseStarboard() > 0)
                    iTargetDefense = 3;
                else if (iTargetDefense != -1 && iTargetDefense != 3) {
                    UpdateDefense(iTargetDefense, 3);
                    iTargetDefense = -1;
                }

                updateColors();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(baseView);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onValidatePopupListener.onValidatePopup();
                basePopup.dismiss();
            }
        });


        updateColors();


        basePopup = builder.create();

        basePopup.show();
    }



    private static int iTargetDefense = -1;
    private static VehicleFighter mVehicle;

    private static void UpdateDefense(int OldPosition, int NewPosition) {
        if (OldPosition == 0) mVehicle.setActualDefenseAft(mVehicle.getActualDefenseAft() - 1);
        else if (OldPosition == 1) mVehicle.setActualDefenseFore(mVehicle.getActualDefenseFore() - 1);
        else if (OldPosition == 2) mVehicle.setActualDefensePort(mVehicle.getActualDefensePort() - 1);
        else if (OldPosition == 3) mVehicle.setActualDefenseStarboard(mVehicle.getActualDefenseStarboard() - 1);

        if (NewPosition == 0) mVehicle.setActualDefenseAft(mVehicle.getActualDefenseAft() + 1);
        else if (NewPosition == 1) mVehicle.setActualDefenseFore(mVehicle.getActualDefenseFore() + 1);
        else if (NewPosition == 2) mVehicle.setActualDefensePort(mVehicle.getActualDefensePort() + 1);
        else if (NewPosition == 3) mVehicle.setActualDefenseStarboard(mVehicle.getActualDefenseStarboard() + 1);

        //onValidatePopupListener.onValidatePopup();
        //basePopup.dismiss();

        updateColors();
    }



    private static void updateColors() {
        txtPort.setBackgroundResource(R.drawable.gradient_defense_impossible);
        txtStarboard.setBackgroundResource(R.drawable.gradient_defense_impossible);
        txtAft.setBackgroundResource(R.drawable.gradient_defense_impossible);
        txtFore.setBackgroundResource(R.drawable.gradient_defense_impossible);

        txtAft.setText(String.valueOf(mVehicle.getActualDefenseAft()));
        txtFore.setText(String.valueOf(mVehicle.getActualDefenseFore()));
        txtPort.setText(String.valueOf(mVehicle.getActualDefensePort()));
        txtStarboard.setText(String.valueOf(mVehicle.getActualDefenseStarboard()));

        if (iTargetDefense == -1) {

            if (mVehicle.getActualDefensePort() > 0)
                txtPort.setBackgroundResource(R.drawable.gradient_defense_select);
            if (mVehicle.getActualDefenseStarboard() > 0)
                txtStarboard.setBackgroundResource(R.drawable.gradient_defense_select);
            if (mVehicle.getActualDefenseFore() > 0)
                txtFore.setBackgroundResource(R.drawable.gradient_defense_select);
            if (mVehicle.getActualDefenseAft() > 0)
                txtAft.setBackgroundResource(R.drawable.gradient_defense_select);

        }
        else {

            if (mVehicle.getActualDefensePort() <= mVehicle.getMaxDefensePort() && iTargetDefense != 2)
                txtPort.setBackgroundResource(R.drawable.gradient_defense_select);
            if (mVehicle.getActualDefenseStarboard() <= mVehicle.getMaxDefenseStarboard() && iTargetDefense != 3)
                txtStarboard.setBackgroundResource(R.drawable.gradient_defense_select);
            if (mVehicle.getActualDefenseAft() <= mVehicle.getMaxDefenseAft() && iTargetDefense != 0)
                txtAft.setBackgroundResource(R.drawable.gradient_defense_select);
            if (mVehicle.getActualDefenseFore() <= mVehicle.getMaxDefenseFore() && iTargetDefense != 1)
                txtFore.setBackgroundResource(R.drawable.gradient_defense_select);


            if (iTargetDefense == 0)
                txtAft.setBackgroundResource(R.drawable.gradient_defense_destination);
            if (iTargetDefense == 1)
                txtFore.setBackgroundResource(R.drawable.gradient_defense_destination);
            if (iTargetDefense == 2)
                txtPort.setBackgroundResource(R.drawable.gradient_defense_destination);
            if (iTargetDefense == 3)
                txtStarboard.setBackgroundResource(R.drawable.gradient_defense_destination);

        }





    }

}
