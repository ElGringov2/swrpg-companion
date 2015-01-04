package com.dragonrider.swrpgcompanion.Scenario;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.Classes.XmlImport;
import com.dragonrider.swrpgcompanion.R;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public class ScenarioItemAdapter extends RecyclerView.Adapter {


    public String ScenarioName;

    public static ScenarioItemAdapter getFromFile(String filename, Context context) {


        return new ScenarioItemAdapter(XmlImport.ImportScenarioFile(filename), context);



    }




    private List<ScenarioItem> Items;

    private Context context;

    public ScenarioItemAdapter(List<ScenarioItem> items, Context context) {
        Items = items;
        this.context = context;
    }

    public void removeAt(int position) {
        Items.remove(position);
        super.notifyItemRemoved(position);
    }

//
//    @Override
//    public int getCount() {
//        return Items.size();
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return Items.get(i);
//    }






    public static class TextScenarioViewHolder extends RecyclerView.ViewHolder {

        public TextScenarioViewHolder(View itemView) {
            super(itemView);

            textView1 = (TextView) itemView.findViewById(R.id.textView);
            textView2 = (TextView) itemView.findViewById(R.id.textView2);

        }

        public TextView textView1;
        public TextView textView2;


    }


    public static class FightScenarioViewHolder extends RecyclerView.ViewHolder {
        public FightScenarioViewHolder(View itemView) {
            super(itemView);
        }

        public ImageButton imageButton;
        public TextView textView;


    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {



        View v;

        switch (viewType) {

            case 1:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scenarioitem_text, parent, false);
                return new TextScenarioViewHolder(v);
            case 2:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.scenarioitem_fight, parent, false);
                return new FightScenarioViewHolder(v);

        }


        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ScenarioItem item = Items.get(position);

        item.UpdateViewHolder(holder, context);



    }

    @Override
    public int getItemViewType(int position) {

        ScenarioItem item = Items.get(position);

        if (item.getClass() == TextScenarioItem.class)
            return 1;
        if (item.getClass() == FightScenarioItem.class)
            return 2;

        return super.getItemViewType(position);


    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemCount() {
        return Items.size();
    }

//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        return Items.get(i).getView(LayoutInflater.from(context), viewGroup);
//    }
}
