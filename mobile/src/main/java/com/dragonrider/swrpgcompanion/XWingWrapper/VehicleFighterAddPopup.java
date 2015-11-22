package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.Classes.Database;
import com.dragonrider.swrpgcompanion.R;

import java.util.List;

/**
 * Created by mge637 on 02/03/2015.
 * Popup de selection de vaisseau
 */
public class VehicleFighterAddPopup {


    public static void Show(Context context, onSelectVehicleListener listener) {
        Show(context, listener, true);
    }

    public static void Show(Context context, final onSelectVehicleListener listener, boolean OnlyXWing) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        Database db = new Database(context);


        if (OnlyXWing) {

            final Adapter adapter = new Adapter(db.getXWingVehicles());

            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onSelectVehicle((Vehicle) (adapter).getItem(which));
                    dialog.dismiss();
                }
            });
        }
        else {
            final Adapter adapter = new Adapter(db.getAllVehicles());

            builder.setAdapter(adapter, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    listener.onSelectVehicle((Vehicle) (adapter).getItem(which));
                    dialog.dismiss();
                }
            });
        }



        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();

    }



    public interface onSelectVehicleListener {
        void onSelectVehicle(Vehicle vehicle);
    }



    private static class Adapter implements ListAdapter {

        private List<Vehicle> vehicleList;

        private Adapter(List<Vehicle> vehicleList) {
            this.vehicleList = vehicleList;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return true;
        }

        @Override
        public boolean isEnabled(int position) {
            return true;
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {

        }

        @Override
        public int getCount() {
            return vehicleList.size();
        }

        @Override
        public Object getItem(int position) {
            return vehicleList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_vehicle, parent, false);

            if (convertView.getTag() == null) {
                convertView.setTag(onCreateViewHolder(convertView));

            }

            onBindViewHolder((ViewHolder)convertView.getTag(), position);


            return convertView;


        }

        @Override
        public int getItemViewType(int position) {
            return 0;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }


        ViewHolder onCreateViewHolder(View baseView) {
            return new ViewHolder(
                    (TextView)baseView.findViewById(R.id.title_text),
                    (TextView)baseView.findViewById(R.id.desc_text),
                    (ImageView)baseView.findViewById(R.id.imageView));


        }

        private void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.imageView.setImageBitmap(vehicleList.get(i).getImageIcon());
            viewHolder.titleTextView.setText(vehicleList.get(i).getName());
            viewHolder.descTextView.setText(vehicleList.get(i).getFullDescription());
        }


        class ViewHolder {
            public TextView titleTextView;
            public TextView descTextView;
            public ImageView imageView;
            public ViewHolder(TextView title, TextView desc, ImageView view) {
                titleTextView = title;
                descTextView = desc;
                imageView = view;
            }
        }
    }
}
