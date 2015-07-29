package com.dragonrider.swrpgcompanion.Scenario;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.dragonrider.swrpgcompanion.Classes.SwipeDismissRecyclerViewTouchListener;
import com.dragonrider.swrpgcompanion.R;

import java.util.ArrayList;
import java.util.List;

public class activityScenarioViewer extends Activity {

    public static ScenarioItemAdapter mainAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_scenario_viewer);

        List<ScenarioItem> items = getExampleScenariosItems();
        mainAdapter = new ScenarioItemAdapter(items, this);
        mainAdapter.ScenarioName = "Nouveau scénario";





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

    private List<ScenarioItem> getExampleScenariosItems() {
        List<ScenarioItem> items = new ArrayList<>();

        TextScenarioItem item = new TextScenarioItem();
        item.Name = "Reliques";
        item.Text = "Faite un jet de [skillroll id=\"4\" difficulty=\"3\" upgrade=\"1\" boost=\"1\" setback=\"0\"]. En cas de [DESPAIR], les mechants tuent automatiquement les gentils. Sinon, pour chaque [SUCCESS] obtenu, les gentils avancent d'une case, et pour chaque [FAILURE] obtenu, les mechant avancent d'une case. Les [ADVANTAGE] ainsi que les [THREAT] ne servent qu'a regagner ou perdre du stress.";
        items.add(item);
        item = new TextScenarioItem();
        item.Name = "Reliques";
        item.Text = "Faite un jet de [skillroll id=\"4\" difficulty=\"3\" upgrade=\"1\" boost=\"1\" setback=\"0\"]. En cas de [DESPAIR], les mechants tuent automatiquement les gentils. Sinon, pour chaque [SUCCESS] obtenu, les gentils avancent d'une case, et pour chaque [FAILURE] obtenu, les mechant avancent d'une case. Les [ADVANTAGE] ainsi que les [THREAT] ne servent qu'a regagner ou perdre du stress.";
        items.add(item);
        item = new TextScenarioItem();
        item.Name = "Reliques";
        item.Text = "Faite un jet de [skillroll id=\"4\" difficulty=\"3\" upgrade=\"1\" boost=\"1\" setback=\"0\"]. En cas de [DESPAIR], les mechants tuent automatiquement les gentils. Sinon, pour chaque [SUCCESS] obtenu, les gentils avancent d'une case, et pour chaque [FAILURE] obtenu, les mechant avancent d'une case. Les [ADVANTAGE] ainsi que les [THREAT] ne servent qu'a regagner ou perdre du stress.";
        items.add(item);
        item = new TextScenarioItem();
        item.Name = "Reliques";
        item.Text = "Faite un jet de [skillroll id=\"4\" difficulty=\"3\" upgrade=\"1\" boost=\"1\" setback=\"0\"]. En cas de [DESPAIR], les mechants tuent automatiquement les gentils. Sinon, pour chaque [SUCCESS] obtenu, les gentils avancent d'une case, et pour chaque [FAILURE] obtenu, les mechant avancent d'une case. Les [ADVANTAGE] ainsi que les [THREAT] ne servent qu'a regagner ou perdre du stress.";
        items.add(item);
        item = new TextScenarioItem();
        item.Name = "Reliques";
        item.Text = "Faite un jet de [skillroll id=\"4\" difficulty=\"3\" upgrade=\"1\" boost=\"1\" setback=\"0\"]. En cas de [DESPAIR], les mechants tuent automatiquement les gentils. Sinon, pour chaque [SUCCESS] obtenu, les gentils avancent d'une case, et pour chaque [FAILURE] obtenu, les mechant avancent d'une case. Les [ADVANTAGE] ainsi que les [THREAT] ne servent qu'a regagner ou perdre du stress.";
        items.add(item);


        return items;
    }


}
