package com.dragonrider.swrpgcompanion.Scenario;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class ScenarioItem {
    public String Name;
    public String Adventure;
    public String Location;
    public String Campaign;


    //public abstract View getView(LayoutInflater inflater, ViewGroup parent);

    public abstract void UpdateViewHolder(RecyclerView.ViewHolder holder, Context context);
}
