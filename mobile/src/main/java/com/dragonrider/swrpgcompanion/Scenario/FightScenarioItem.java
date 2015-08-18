package com.dragonrider.swrpgcompanion.Scenario;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.dragonrider.swrpgcompanion.Classes.EncounterFile;
import com.dragonrider.swrpgcompanion.GroundFightActivities.GroundFightMultiPanelActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class FightScenarioItem extends ScenarioItem {

    private String mEncounterFilename = "";



    @Override
    public void UpdateViewHolder(RecyclerView.ViewHolder holder, final Context context, Date duration) {

        ScenarioItemAdapter.FightScenarioViewHolder holder1 = (ScenarioItemAdapter.FightScenarioViewHolder) holder;
        if (getEncounterFilename() != null && !getEncounterFilename().isEmpty())
            holder1.imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EncounterFile encounterFile = EncounterFile.fromFile(getEncounterFilename());
                    encounterFile.LaunchFight();
                    GroundFightMultiPanelActivity.StartFight(context, false, true, false);
                }
            });


        SimpleDateFormat date = new SimpleDateFormat("HH:mm", Locale.FRANCE);
        holder1.textView.setText(date.format(duration.getTime() + (getDuration() * 60 * 1000)) + " " + getName() + "(" + getLocation()+ ") ");

    }

    public String getEncounterFilename() {
        return mEncounterFilename;
    }

    public FightScenarioItem setEncounterFilename(String pEncounterFilename) {
        this.mEncounterFilename = pEncounterFilename;
        return this;
    }
}
