package com.dragonrider.swrpgcompanion.Scenario;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import java.util.Date;

public abstract class ScenarioItem {
    protected String mName = "";
    protected String mLocation = "";
    private Integer mDuration = 0;
    private Boolean mNotification = false;


    //public abstract View getView(LayoutInflater inflater, ViewGroup parent);

    public abstract void UpdateViewHolder(RecyclerView.ViewHolder holder, Context context, Date duration);



    public String getName() {
        return mName;
    }

    public ScenarioItem setName(String pName) {
        this.mName = pName;
        return this;
    }

    public String getLocation() {
        return mLocation;
    }

    public ScenarioItem setLocation(String pLocation) {
        this.mLocation = pLocation;
        return this;
    }

    public void setDuration(Integer duration) {
        this.mDuration = duration;
    }


    public Integer getDuration() {
        return mDuration;
    }


    public Boolean getNotification() {
        return mNotification;
    }

    public void setNotification(Boolean pNotification) {
        this.mNotification = pNotification;
    }
}
