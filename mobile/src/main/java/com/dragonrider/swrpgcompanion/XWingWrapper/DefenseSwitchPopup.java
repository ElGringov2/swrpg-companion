package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.R;

/**
 * Created by mge637 on 02/03/2015.
 */
public class DefenseSwitchPopup {

    private static AlertDialog basePopup;

    public interface onValidatePopupListener {
        public void onValidatePopup() ;
    }


    public static void Show(Context context, final VehicleFighter vehicle, final onValidatePopupListener onValidatePopupListener) {

        View baseView = LayoutInflater.from(context).inflate(R.layout.popup_defense_switch, null);

        iTargetDefense = -1;

        TextView txtAft = (TextView) baseView.findViewById(R.id.txtAft);
        TextView txtFore = (TextView) baseView.findViewById(R.id.txtFore);
        TextView txtPort = (TextView) baseView.findViewById(R.id.txtPort);
        TextView txtStarboard = (TextView) baseView.findViewById(R.id.txtStarboard);

        txtPort.setBackgroundResource(R.drawable.gradient_defense_impossible);
        if (vehicle.getBaseVehicle().getSilhouette() > 4) {
            txtPort.setText(String.valueOf(vehicle.getActualDefensePort()));
            if (vehicle.getActualDefensePort() > 0)
                txtPort.setBackgroundResource(R.drawable.gradient_defense_destination);
        }

        txtStarboard.setBackgroundResource(R.drawable.gradient_defense_impossible);
        if (vehicle.getBaseVehicle().getSilhouette() > 4) {
            txtStarboard.setText(String.valueOf(vehicle.getActualDefenseStarboard()));
            if (vehicle.getActualDefenseStarboard() > 0)
                txtPort.setBackgroundResource(R.drawable.gradient_defense_destination);
        }

        txtAft.setText(String.valueOf(vehicle.getActualDefenseAft()));
        txtAft.setBackgroundResource(R.drawable.gradient_defense_impossible);
        if (vehicle.getActualDefenseAft() > 0)
            txtAft.setBackgroundResource(R.drawable.gradient_defense_destination);

        txtFore.setText(String.valueOf(vehicle.getActualDefenseFore()));
        txtFore.setBackgroundResource(R.drawable.gradient_defense_impossible);
        if (vehicle.getActualDefenseFore() > 0)
            txtFore.setBackgroundResource(R.drawable.gradient_defense_destination);


        txtAft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iTargetDefense == -1 && vehicle.getActualDefenseAft() > 0) {
                    v.setBackgroundResource(R.drawable.gradient_defense_select);
                    iTargetDefense = 0;
                }
                else if (iTargetDefense != -1 && iTargetDefense != 0)
                    UpdateDefense(iTargetDefense, 0, vehicle, onValidatePopupListener);
            }
        });

        txtFore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (iTargetDefense == -1 && vehicle.getActualDefenseFore() > 0){
                    v.setBackgroundResource(R.drawable.gradient_defense_select);
                    iTargetDefense = 1;
                }
                else if (iTargetDefense != -1 && iTargetDefense != 1)
                    UpdateDefense(iTargetDefense, 1, vehicle, onValidatePopupListener);
            }
        });

        if (vehicle.getBaseVehicle().getSilhouette() > 4) {
            txtPort.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iTargetDefense == -1 && vehicle.getActualDefensePort() > 0){
                        v.setBackgroundResource(R.drawable.gradient_defense_select);
                        iTargetDefense = 2;
                    }
                    else if (iTargetDefense != -1 && iTargetDefense != 2)
                        UpdateDefense(iTargetDefense, 2, vehicle, onValidatePopupListener);
                }
            });

            txtStarboard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (iTargetDefense == -1 && vehicle.getActualDefenseStarboard() > 0){
                        v.setBackgroundResource(R.drawable.gradient_defense_select);
                        iTargetDefense = 3;
                    }
                    else if (iTargetDefense != -1 && iTargetDefense != 3)
                        UpdateDefense(iTargetDefense, 3, vehicle, onValidatePopupListener);
                }
            });
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setView(baseView);

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });












        basePopup = builder.create();

        basePopup.show();
    }

    private static int iTargetDefense = -1;

    private static void UpdateDefense(int OldPosition, int NewPosition, VehicleFighter vehicleFighter, onValidatePopupListener onValidatePopupListener) {
        if (OldPosition == 0) vehicleFighter.setActualDefenseAft(vehicleFighter.getActualDefenseAft() - 1);
        else if (OldPosition == 1) vehicleFighter.setActualDefenseFore(vehicleFighter.getActualDefenseFore() - 1);
        else if (OldPosition == 2) vehicleFighter.setActualDefensePort(vehicleFighter.getActualDefensePort() - 1);
        else if (OldPosition == 3) vehicleFighter.setActualDefenseStarboard(vehicleFighter.getActualDefenseStarboard() - 1);

        if (NewPosition == 0) vehicleFighter.setActualDefenseAft(vehicleFighter.getActualDefenseAft() + 1);
        else if (NewPosition == 1) vehicleFighter.setActualDefenseFore(vehicleFighter.getActualDefenseFore() + 1);
        else if (NewPosition == 2) vehicleFighter.setActualDefensePort(vehicleFighter.getActualDefensePort() + 1);
        else if (NewPosition == 3) vehicleFighter.setActualDefenseStarboard(vehicleFighter.getActualDefenseStarboard() + 1);

        onValidatePopupListener.onValidatePopup();
        basePopup.dismiss();
    }


}
