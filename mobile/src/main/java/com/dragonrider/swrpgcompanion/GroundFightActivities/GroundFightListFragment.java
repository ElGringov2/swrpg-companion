package com.dragonrider.swrpgcompanion.GroundFightActivities;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.dragonrider.swrpgcompanion.Classes.GroundFightScene;
import com.dragonrider.swrpgcompanion.R;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class GroundFightListFragment extends Fragment {




    View MainView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        MainView = inflater.inflate(R.layout.fragment_ground_fight_list, container, false);

        RecyclerView mainList = (RecyclerView)MainView.findViewById(R.id.GroundFightActivyty_MainList);
        mainList.setAdapter(GroundFightScene.MainAdapter);
        mainList.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));


        RecyclerView rinit = ((RecyclerView)MainView.findViewById(R.id.init));
        rinit.setAdapter(GroundFightScene.getInitiativeAdapter());
        rinit.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));




        return MainView;
    }


}
