package com.dragonrider.swrpgcompanion.Scenario;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mge637 on 15/08/2015. Gestion des campagnes
 */
public class Campaign {
    private String mCampaignName;
    private List<Scenario> mScenarios = new ArrayList<>();


    public String getCampaignName() {
        return mCampaignName;
    }

    public Campaign setCampaignName(String pCampaignName) {
        this.mCampaignName = pCampaignName;
        return this;
    }

    public List<Scenario> getScenarios() {
        return mScenarios;
    }


}
