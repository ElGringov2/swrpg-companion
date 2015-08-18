package com.dragonrider.swrpgcompanion.Scenario;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.view.Menu;
import android.view.MenuItem;

import com.dragonrider.swrpgcompanion.Classes.SwipeDismissRecyclerViewTouchListener;
import com.dragonrider.swrpgcompanion.R;

import java.util.Date;



public class activityScenarioViewer extends Activity {

    public static ScenarioItemAdapter mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_scenario_viewer);




        setTitle(mainAdapter.ScenarioName);
        RecyclerView mainView = (RecyclerView)findViewById(R.id.listView1);

        mainView.setItemAnimator(new DefaultItemAnimator());
        mainView.setLayoutManager(new LinearLayoutManager(this));
        mainView.setAdapter(mainAdapter);
        SwipeDismissRecyclerViewTouchListener listener = new SwipeDismissRecyclerViewTouchListener(mainView, new SwipeDismissRecyclerViewTouchListener.DismissCallbacks() {
            @Override
            public boolean canDismiss(int position) {
                return true;
            }

            @Override
            public void onDismiss(RecyclerView recyclerView, int[] reverseSortedPositions) {

                for (int position : reverseSortedPositions)
                    mainAdapter.removeAt(position);
                mainAdapter.notifyDataSetChanged();
            }
        });

        mainView.setOnTouchListener(listener);
        mainView.setOnScrollListener(listener.makeScrollListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_scenario_viewer, menu);

        if (mainAdapter.getDate() != null)
            menu.findItem(R.id.action_start).setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_start) {


            mainAdapter.setDate(new Date());
            mainAdapter.notifyDataSetChanged();

            item.setVisible(false);


            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
