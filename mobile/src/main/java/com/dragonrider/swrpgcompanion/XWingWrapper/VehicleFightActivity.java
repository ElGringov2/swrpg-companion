package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by mge637 on 01/03/2015.
 *
 */
public class VehicleFightActivity extends Activity {

    static VehicleFighterAdapter adapter;

    public static VehicleFighterAdapter getAdapter() {
        return adapter;
    }

    public static void ClearFight() {


        adapter = null;
    }

    private static String filename = "";
    public static void LoadFight(String Filename) {
        adapter = null;
        filename = Filename;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vehiclefight);


        if (adapter == null)
            adapter = new VehicleFighterAdapter(this, new ArrayList<VehicleFighter>());

        if (!Objects.equals(filename, "")) {
            adapter.LoadFromFile(filename);
            filename = "";

            for(final CrewWrapper wrapper : adapter.getCrews())
                if (wrapper.isPlayer)
                    InitiativePopup.Show(this, wrapper.baseNPC.Name, new InitiativePopup.IOnValidateInitiative() {
                        @Override
                        public void OnValidate(int Triumph, int Success, int Advantage) {
                            wrapper.initiative = new Initiative();
                            wrapper.initiative.Advantages = Advantage;
                            wrapper.initiative.Triumph = Triumph;
                            wrapper.initiative.Success = Success;
                        }});




        }

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.RecyclerView);

        recyclerView.setAdapter(adapter);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        RecyclerView initiativeRecyclerView = (RecyclerView)findViewById(R.id.InitiativeRecyclerView);

        initiativeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        initiativeRecyclerView.setAdapter(adapter.getInitiativeAdapter(this));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.vehiclefightmenu, menu);

        return true;

    }



    public void mnuAddVehicle_Click (MenuItem v) {
        VehicleFighterAddPopup.Show(this, new VehicleFighterAddPopup.onSelectVehicleListener() {
            @Override
            public void onSelectVehicle(Vehicle vehicle) {
                adapter.addVehicle(vehicle);
            }
        });
    }


    public void mnuSaveAs_Click (MenuItem v) {
        final EditText text = new EditText(this);
        text.setText(adapter.getFilename());
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.save_as))
                .setView(text)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapter.setFilename(text.getText().toString());
                        dialog.dismiss();
                        adapter.SaveFight();
                    }
                }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).create().show();

    }

    public void mnuSave_Click(MenuItem v) {
        if (adapter.getFilename().isEmpty())
            mnuSaveAs_Click(v);
        else
            adapter.SaveFight();
    }




    public void mnuNextRound_Click(MenuItem v) {
        ((TextView)findViewById(R.id.txtPhase)).setText(R.string.phase_planification);
        adapter.InitiativeOnlyPilot = false;
        findViewById(R.id.InitiativeRecyclerView).setVisibility(View.VISIBLE);
        adapter.NextRound();
        adapter.FinalNotifyDataSetChanged();
    }




    public void SwitchPhase(View view) {
        if (((Button)view).getText() == getString(R.string.phase_plan)) {
            ((TextView)findViewById(R.id.txtPhase)).setText(R.string.phase_planification);
            Intent intent = new Intent(this, VehiculePlanification.class);
            startActivity(intent);
            findViewById(R.id.InitiativeRecyclerView).setVisibility(View.GONE);

        }
        else if (((Button)view).getText() == getString(R.string.phase_man)) {
            ((TextView)findViewById(R.id.txtPhase)).setText(R.string.phase_maneuver);
            adapter.InitiativeOnlyPilot = false;
            findViewById(R.id.InitiativeRecyclerView).setVisibility(View.VISIBLE);
            adapter.NextRound();
            adapter.FinalNotifyDataSetChanged();

        }
        else if (((Button)view).getText() == getString(R.string.phase_ener)) {
            ((TextView) findViewById(R.id.txtPhase)).setText(R.string.phase_energy);
            adapter.EnergyStep(this);
            findViewById(R.id.InitiativeRecyclerView).setVisibility(View.GONE);

        }
        else if (((Button)view).getText() == getString(R.string.phase_mov)) {
            ((TextView)findViewById(R.id.txtPhase)).setText(R.string.phase_movement);
            adapter.InitiativeOnlyPilot = true;
            findViewById(R.id.InitiativeRecyclerView).setVisibility(View.VISIBLE);
            adapter.NextRound();
            adapter.FinalNotifyDataSetChanged();

        }
        else if (((Button)view).getText() == getString(R.string.phase_act)) {
            ((TextView)findViewById(R.id.txtPhase)).setText(R.string.phase_action);
            adapter.InitiativeOnlyPilot = false;
            findViewById(R.id.InitiativeRecyclerView).setVisibility(View.VISIBLE);
            adapter.NextRound();
            adapter.FinalNotifyDataSetChanged();

        }
    }
}
