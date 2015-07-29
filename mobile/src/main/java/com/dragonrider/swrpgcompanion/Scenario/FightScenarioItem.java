package com.dragonrider.swrpgcompanion.Scenario;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.dragonrider.swrpgcompanion.Classes.EncounterFile;

import com.dragonrider.swrpgcompanion.GroundFightActivities.GroundFightMultiPanelActivity;
import com.dragonrider.swrpgcompanion.R;


public class FightScenarioItem extends ScenarioItem {

    public String encounterFilename;


//    @Override
//    public View getView(LayoutInflater inflater, ViewGroup parent) {
//        View convertView = inflater.inflate(R.layout.scenarioitem_fight, parent, false);
//
//        final Context context = convertView.getContext();
//
//        convertView.findViewById(R.id.ImageButton).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                EncounterFile encounterFile = EncounterFile.fromFile(encounterFilename);
//                encounterFile.LaunchFight();
//                GroundFightMultiPanelActivity.StartFight(context, false, true, false);
//            }
//        });
//
//        ((TextView)convertView.findViewById(R.id.textView)).setText(Name);
//
//
//        return convertView;
//    }

    @Override
    public void UpdateViewHolder(RecyclerView.ViewHolder holder, final Context context) {

        ScenarioItemAdapter.FightScenarioViewHolder holder1 = (ScenarioItemAdapter.FightScenarioViewHolder) holder;
        holder1.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EncounterFile encounterFile = EncounterFile.fromFile(encounterFilename);
                encounterFile.LaunchFight();
                GroundFightMultiPanelActivity.StartFight(context, false, true, false);
            }
        });

        holder1.textView.setText(Name);

    }
}
