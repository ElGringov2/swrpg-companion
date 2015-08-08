package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.R;


/**
 * Created by mge637 on 01/08/2015. Popup d'initiative pour joueurs
 */
public class InitiativePopup {

    public interface IOnValidateInitiative {
        void OnValidate(int Triumph, int Success, int Advantage);
    }


    public static void Show(Context context, String PlayerName, final IOnValidateInitiative OnValidateInitiative) {
        View baseView = LayoutInflater.from(context).inflate(R.layout.listitem_startgroundfightitem, null, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(baseView);
        builder.setTitle(String.format("%s %s", context.getString(R.string.initiativeof), PlayerName));

        final Initiative initiative = new Initiative();

        baseView.findViewById(R.id.ListItemStartGroundFight_IsPresent).setVisibility(View.GONE);

        final TextView txtTriumph = (TextView)baseView.findViewById(R.id.ListItemStartGroundFight_TriumphCount);
        final TextView txtSuccess = (TextView)baseView.findViewById(R.id.ListItemStartGroundFight_SuccessCount);
        final TextView txtAdvantage = (TextView)baseView.findViewById(R.id.ListItemStartGroundFight_AdvantageCount);

        txtAdvantage.setText("0");
        txtSuccess.setText("0");
        txtTriumph.setText("0");

        baseView.findViewById(R.id.ListItemStartGroundFight_TriumphPlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiative.Triumph++;
                txtTriumph.setText(String.valueOf(initiative.Triumph));
            }
        });
        baseView.findViewById(R.id.ListItemStartGroundFight_TriumphMinus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiative.Triumph--;
                txtTriumph.setText(String.valueOf(initiative.Triumph));
            }
        });
        baseView.findViewById(R.id.ListItemStartGroundFight_SuccessPlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiative.Success++;
                txtSuccess.setText(String.valueOf(initiative.Success));
            }
        });
        baseView.findViewById(R.id.ListItemStartGroundFight_SuccessMinus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiative. Success--;
                txtSuccess.setText(String.valueOf(initiative.Success));
            }
        });
        baseView.findViewById(R.id.ListItemStartGroundFight_AdvantagePlus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiative.Advantages++;
                txtAdvantage.setText(String.valueOf(initiative.Advantages));
            }
        });
        baseView.findViewById(R.id.ListItemStartGroundFight_AdvantageMinus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiative.Advantages--;
                txtAdvantage.setText(String.valueOf(initiative.Advantages));
            }
        });

        builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                OnValidateInitiative.OnValidate(initiative.Triumph, initiative.Success, initiative.Advantages);
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
}
