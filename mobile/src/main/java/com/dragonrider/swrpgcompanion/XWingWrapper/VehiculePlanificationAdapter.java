package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.R;



/**
 * Created by mge637 on 29/10/2015.j
 */
public class VehiculePlanificationAdapter extends RecyclerView.Adapter<VehiculePlanificationAdapter.ViewHolder> {


    public VehiculePlanificationAdapter() {

    }


    /**
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(ViewHolder, int)}. Since it will be re-used to display different
     * items in the data set, it is a good idea to cache references to sub views of the View to
     * avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(ViewHolder, int)
     */
    @Override
    public VehiculePlanificationAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_vehiculeplanification, parent, false);


        return new ViewHolder(baseView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the {@link ViewHolder#itemView} to reflect the item at
     * the given position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this
     * method again if the position of the item changes in the data set unless the item itself
     * is invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside this
     * method and should not keep a copy of it. If you need the position of an item later on
     * (e.g. in a click listener), use {@link ViewHolder#getPosition()} which will have the
     * updated position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(VehiculePlanificationAdapter.ViewHolder holder, int position) {
        holder.UpdateViewHolder(position);
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return VehicleFightActivity.getAdapter().getFighters().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {


        private TextView textView;
        private GridLayout gridLayout;


        public ViewHolder(View itemView) {
            super(itemView);


            textView = (TextView) itemView.findViewById(R.id.TextView);
            gridLayout = (GridLayout) itemView.findViewById(R.id.GridLayout);

        }

        public void UpdateViewHolder(int position) {
            final VehicleFighter fighter = VehicleFightActivity.getAdapter().getFighters().get(position);

            textView.setText(fighter.getBaseVehicle().getName());

            Context context = gridLayout.getContext();

            int[][] maneuvers = ManeuverInfos.getManeuvers(fighter.getBaseVehicle().getManeuverMapName());

            LinearLayout.LayoutParams imageParam = new LinearLayout.LayoutParams(80,80);



            int speed = maneuvers.length - 1;

            for (int line = maneuvers.length - 1; line >= 0; line--) {

                if (maneuvers[line][0] == 0 && maneuvers[line][1] == 0 && maneuvers[line][2] == 0 && maneuvers[line][3] == 0 && maneuvers[line][4] == 0 && maneuvers[line][5] == 0) {
                    speed--;
                    continue;
                }
                final int finalspeed = speed;

                TextView textView=  new TextView(gridLayout.getContext());
                textView.setText(String.valueOf(speed));
                textView.setTextSize(32);
                textView.setLayoutParams(imageParam);
                gridLayout.addView(textView);

                ImageView imageView = new ImageView(context);
                if (maneuvers[line][0] == 1)
                    imageView.setImageResource(R.drawable.man_turnleft0);
                if (maneuvers[line][0] == 2)
                    imageView.setImageResource(R.drawable.man_turnleft1);
                if (maneuvers[line][0] == 3)
                    imageView.setImageResource(R.drawable.man_turnleft2);
                imageView.setLayoutParams(imageParam);
                if (fighter.getSelectedSpeed() == speed && fighter.getSelectedManeuver() == 0)
                    imageView.setBackgroundColor(Color.RED);
                if (maneuvers[line][0] != 0)
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fighter.setSelectedManeuver(0);
                        fighter.setSelectedSpeed(finalspeed);
                        ResetBackground();
                        v.setBackgroundColor(Color.RED);
                    }
                });
                gridLayout.addView(imageView);

                imageView = new ImageView(context);
                if (maneuvers[line][1] == 1)
                    imageView.setImageResource(R.drawable.man_bankleft0);
                if (maneuvers[line][1] == 2)
                    imageView.setImageResource(R.drawable.man_bankleft1);
                if (maneuvers[line][1] == 3)
                    imageView.setImageResource(R.drawable.man_bankleft2);
                if (maneuvers[line][1] != 0)
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fighter.setSelectedManeuver(1);
                            fighter.setSelectedSpeed(finalspeed);
                            ResetBackground();
                            v.setBackgroundColor(Color.RED);
                    }
                });
                if (fighter.getSelectedSpeed() == speed && fighter.getSelectedManeuver() == 1)
                    imageView.setBackgroundColor(Color.RED);
                imageView.setLayoutParams(imageParam);
                gridLayout.addView(imageView);

                imageView = new ImageView(context);
                if (maneuvers[line][2] == 1)
                    imageView.setImageResource(R.drawable.man_straight0);
                if (maneuvers[line][2] == 2)
                    imageView.setImageResource(R.drawable.man_straight1);
                if (maneuvers[line][2] == 3)
                    imageView.setImageResource(R.drawable.man_straight2);
                if (maneuvers[line][2] != 0)
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        fighter.setSelectedManeuver(2);
                        fighter.setSelectedSpeed(finalspeed);
                        ResetBackground();
                        v.setBackgroundColor(Color.RED);
                    }
                });
                if (fighter.getSelectedSpeed() == speed && fighter.getSelectedManeuver() == 2)
                    imageView.setBackgroundColor(Color.RED);

                imageView.setLayoutParams(imageParam);
                gridLayout.addView(imageView);

                imageView = new ImageView(context);
                if (maneuvers[line][3] == 1)
                    imageView.setImageResource(R.drawable.man_bankright0);
                if (maneuvers[line][3] == 2)
                    imageView.setImageResource(R.drawable.man_bankright1);
                if (maneuvers[line][3] == 3)
                    imageView.setImageResource(R.drawable.man_bankright2);
                if (maneuvers[line][3] != 0)
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fighter.setSelectedManeuver(3);
                            fighter.setSelectedSpeed(finalspeed);
                            ResetBackground();
                            v.setBackgroundColor(Color.RED);
                        }
                    });
                if (fighter.getSelectedSpeed() == speed && fighter.getSelectedManeuver() == 3)
                    imageView.setBackgroundColor(Color.RED);

                imageView.setLayoutParams(imageParam);
                gridLayout.addView(imageView);


                imageView = new ImageView(context);
                if (maneuvers[line][4] == 1)
                    imageView.setImageResource(R.drawable.man_turnright0);
                if (maneuvers[line][4] == 2)
                    imageView.setImageResource(R.drawable.man_turnright1);
                if (maneuvers[line][4] == 3)
                    imageView.setImageResource(R.drawable.man_turnright2);
                if (maneuvers[line][4] != 0)
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            fighter.setSelectedManeuver(4);
                            fighter.setSelectedSpeed(finalspeed);
                            ResetBackground();
                            v.setBackgroundColor(Color.RED);
                        }
                    });
                if (fighter.getSelectedSpeed() == speed && fighter.getSelectedManeuver() == 4)
                    imageView.setBackgroundColor(Color.RED);

                imageView.setLayoutParams(imageParam);
                gridLayout.addView(imageView);



                imageView = new ImageView(context);
                if (maneuvers[line].length > 5) {
                    if (maneuvers[line][5] == 3)
                        imageView.setImageResource(R.drawable.man_koiogram2);
                    if (maneuvers[line][5] != 0)
                        imageView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fighter.setSelectedManeuver(5);
                                fighter.setSelectedSpeed(finalspeed);
                                ResetBackground();
                                v.setBackgroundColor(Color.RED);
                            }
                        });
                    if (fighter.getSelectedSpeed() == speed && fighter.getSelectedManeuver() == 5)
                        imageView.setBackgroundColor(Color.RED);

                }
                imageView.setLayoutParams(imageParam);
                gridLayout.addView(imageView);





                speed--;
            }

        }

        private void ResetBackground() {
            for (int i = 0; i < gridLayout.getChildCount(); i++) {
                gridLayout.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
            }
        }
    }
}
