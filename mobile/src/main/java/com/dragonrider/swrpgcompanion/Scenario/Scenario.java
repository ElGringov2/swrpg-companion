package com.dragonrider.swrpgcompanion.Scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mge637 on 15/08/2015.
 */
public class Scenario {
    private String mName;

    private List<ScenarioItem> mItems = new ArrayList<>();

    public String getName() {
        return mName;
    }

    public Scenario setName(String pName) {
        this.mName = pName;
        return this;
    }

    public List<ScenarioItem> getItems() {
        return mItems;
    }

    public Scenario setItems(List<ScenarioItem> pItems) {
        this.mItems = pItems;
        return this;
    }
}
