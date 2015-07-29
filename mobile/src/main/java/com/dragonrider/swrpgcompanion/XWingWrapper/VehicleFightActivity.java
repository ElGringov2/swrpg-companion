package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.dragonrider.swrpgcompanion.R;

import java.util.ArrayList;

/**
 * Created by mge637 on 01/03/2015.
 *
 */
public class VehicleFightActivity extends Activity {

    static VehicleFighterAdapter adapter;

    public static void ClearFight() {
        if (adapter != null)
            adapter.ClearFighters();
        else
            adapter = new VehicleFighterAdapter(new ArrayList<VehicleFighter>());
    }

    public static void LoadFight(String Filename) {
        if (adapter == null)
            adapter.ClearFighters();

        adapter.LoadFromFile(Filename);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vehiclefight);


        if (adapter == null)
            adapter = new VehicleFighterAdapter(new ArrayList<VehicleFighter>());

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.RecyclerView);

        recyclerView.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);

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
}
