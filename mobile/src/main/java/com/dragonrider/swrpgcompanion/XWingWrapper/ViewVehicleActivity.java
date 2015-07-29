package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.dragonrider.swrpgcompanion.Classes.Database;
import com.dragonrider.swrpgcompanion.R;

import java.util.List;

/**
 * Created by mge637 on 01/03/2015.
 * Permet de voir les véhicules en base de données
 */
public class ViewVehicleActivity extends Activity {

    private RecyclerView mRecyclerView;
    private VehicleAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_vehicleview);


        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        Database db = new Database(this);
        List<Vehicle> vehicleList = db.getAllVehicles();

        mAdapter = new VehicleAdapter(vehicleList);
        mRecyclerView.setAdapter(mAdapter);

    }





}
