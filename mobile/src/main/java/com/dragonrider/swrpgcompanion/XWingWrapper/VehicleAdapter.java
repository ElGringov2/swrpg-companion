package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.R;

import java.util.List;

/**
 * Created by mge637 on 01/03/2015.
 */
public class VehicleAdapter extends RecyclerView.Adapter<VehicleAdapter.ViewHolder> {

    private List<Vehicle> Vehicles;

    public VehicleAdapter(List<Vehicle> vehicles) {
        Vehicles = vehicles;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View baseView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listitem_vehicle, viewGroup, false);
        ViewHolder holder = new ViewHolder(baseView,
                (TextView)baseView.findViewById(R.id.title_text),
                (TextView)baseView.findViewById(R.id.desc_text),
                (ImageView)baseView.findViewById(R.id.imageView));

        return holder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.imageView.setImageBitmap(Vehicles.get(i).getImageIcon());
        viewHolder.titleTextView.setText(Vehicles.get(i).getName());
        viewHolder.descTextView.setText(Vehicles.get(i).getFullDescription());
    }

    @Override
    public int getItemCount() {
        return Vehicles.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView;
        public TextView descTextView;
        public ImageView imageView;
        public ViewHolder(View baseView, TextView title, TextView desc, ImageView view) {
            super(baseView);
            titleTextView = title;
            descTextView = desc;
            imageView = view;
        }
    }


}
