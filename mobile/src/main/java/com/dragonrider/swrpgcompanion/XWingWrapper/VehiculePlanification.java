package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.dragonrider.swrpgcompanion.R;

public class VehiculePlanification extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicule_planification);


        VehiculePlanificationAdapter mainAdapter = new VehiculePlanificationAdapter();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.RecyclerView);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mainAdapter);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        VehicleFightActivity.getAdapter().notifyDataSetChanged();
    }
}
