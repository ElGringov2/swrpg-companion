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
import android.widget.EditText;

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
        }

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.RecyclerView);

        recyclerView.setAdapter(adapter);


        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        RecyclerView initiativeRecyclerView = (RecyclerView)findViewById(R.id.InitiativeRecyclerView);

        initiativeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        initiativeRecyclerView.setAdapter(adapter.getInitiativeAdapter(this));


        findViewById(R.id.ButtonEnergy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.EnergyStep(VehicleFightActivity.this);
            }
        });
        findViewById(R.id.ButtonPlan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VehicleFightActivity.this, VehiculePlanification.class);
                startActivity(intent);
            }
        });

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

    public void mnuEnergy_Click(MenuItem v) {
        adapter.EnergyStep(this);
    }



    public void mnuNextRound_Click(MenuItem v) {
        adapter.NextRound();
        adapter.FinalNotifyDataSetChanged();
    }


    public void mnuPlan_Click(MenuItem item) {
        Intent intent = new Intent(this, VehiculePlanification.class);
        startActivity(intent);

    }
}
