package com.dragonrider.swrpgcompanion.GroundFightActivities;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.Classes.App;
import com.dragonrider.swrpgcompanion.Classes.GenericEditor;
import com.dragonrider.swrpgcompanion.Classes.GroundFightScene;
import com.dragonrider.swrpgcompanion.Classes.GroundFighter;
import com.dragonrider.swrpgcompanion.Classes.NPC;
import com.dragonrider.swrpgcompanion.Classes.SWListBoxItem;
import com.dragonrider.swrpgcompanion.Classes.SWListBoxItemAdapter;
import com.dragonrider.swrpgcompanion.NPCViewer.ShowNPCActivity;
import com.dragonrider.swrpgcompanion.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mge637 on 07/08/2015.
 * Gestion des listes de groundfighter
 */
public class GroundFighterAdapter extends RecyclerView.Adapter<GroundFighterAdapter.GroundFighterViewHolder> {

    /**
     * Liste des combattants
     */
    private List<GroundFighter> Fighters = new ArrayList<>();

    /**
     * Called when RecyclerView needs a new {@link GroundFighterViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(GroundFighterViewHolder, int)}. Since it will be re-used to display different
     * items in the data set, it is a good idea to cache references to sub views of the View to
     * avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(GroundFighterViewHolder, int)
     */
    @Override
    public GroundFighterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View baseView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_groundfighter, parent, false);
        return new GroundFighterViewHolder(baseView);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the {@link GroundFighterViewHolder#itemView} to reflect the item at
     * the given position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this
     * method again if the position of the item changes in the data set unless the item itself
     * is invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside this
     * method and should not keep a copy of it. If you need the position of an item later on
     * (e.g. in a click listener), use {@link GroundFighterViewHolder#getPosition()} which will have the
     * updated position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(GroundFighterViewHolder holder, int position) {
        holder.updateView(Fighters.get(position), position);

    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return Fighters.size();
    }

    /**
     * Obtient la liste des combattants
     * @return la liste des combattants
     */
    public List<GroundFighter> getFighters() {
        return Fighters;
    }

    public GroundFighterAdapter setFighters(List<GroundFighter> fighters) {
        this.Fighters = fighters;
        this.notifyDataSetChanged();
        return this;
    }


    public class GroundFighterViewHolder extends RecyclerView.ViewHolder {

        private View NPCLayout;
        private View backgroundView;
        private ImageView imageView;
        private TextView nameView;
        private TextView woundView;
        private TextView statusView;
        private SeekBar woundBar;
        private TextView strainView;
        private SeekBar strainBar;
        private Spinner maneuverSpinner;
        private Spinner actionSpinner;
        private View btnApply;
        private View btnDamage;
        private View btnInfos;
        private View btnOverflow;


        public GroundFighterViewHolder(View convertView) {
            super(convertView);

            NPCLayout = convertView.findViewById(R.id.NPCLayout);

            backgroundView = convertView.findViewById(R.id.GroundFighterItemBackgroundLayout);
            imageView = (ImageView) convertView.findViewById(R.id.GroundFighterItemImage);
            nameView = (TextView)convertView.findViewById(R.id.GroundFighterItemName);
            woundView = (TextView)convertView.findViewById(R.id.GroundFighterItemWounds);
            statusView = (TextView)convertView.findViewById(R.id.GroundFighterItemStatusText);
            woundBar = (SeekBar)convertView.findViewById(R.id.GroundFighterItemWoundsBar);
            strainView = (TextView)convertView.findViewById(R.id.GroundFighterItemStrain);
            strainBar = (SeekBar)convertView.findViewById(R.id.GroundFighterItemStrainBar);
            maneuverSpinner = (Spinner)convertView.findViewById(R.id.GroundFighterItemManeuverSpinner);
            actionSpinner = (Spinner)convertView.findViewById(R.id.GroundFighterItemActionSpinner);
            btnApply = convertView.findViewById(R.id.GroundFighterItemButtonApply);
            btnDamage = convertView.findViewById(R.id.GroundFighterItemButtonDamage);
            btnInfos = convertView.findViewById(R.id.GroundFighterItemButtonInfos);
            btnOverflow = convertView.findViewById(R.id.GroundFighterItemButtonOverflow);

        }

        public void updateView(GroundFighter myFighter, int position) {
            if (myFighter.Played >= 0)
                backgroundView.setBackgroundResource(R.drawable.groundfighteritem_played_border);
            else
                backgroundView.setBackground(null);

            imageView.setBackground(myFighter.getColor());
            if (myFighter.getBase().getImage() != null)
                imageView.setImageBitmap(myFighter.getBase().getImage());

            String fighterName = myFighter.getFullName();
            if (!myFighter.isPlayer && myFighter.getBase().Type == NPC.NPCTypes.Minion)
                fighterName = String.format("%dx%s",
                        myFighter.getCount(),
                        fighterName);
            nameView.setText(fighterName);

            if (myFighter.isPlayer)
                updatePlayerView(myFighter, position);
            else
                updateNonPlayerView(myFighter, position);
        }

        private void updatePlayerView(final GroundFighter myFighter, final int position) {


            NPCLayout.setVisibility(View.GONE);
            btnInfos.setVisibility(View.GONE);
            btnDamage.setVisibility(View.GONE);


            btnApply.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    GroundFightScene.setPlayed(position,
                            "",
                            ""
                    );

                }
            });

            btnOverflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu menu = new PopupMenu(btnOverflow.getContext(), btnOverflow);
                    menu.getMenuInflater().inflate(R.menu.ground_fight_overflow, menu.getMenu());
                    menu.getMenu().findItem(R.id.mnuGroundFightOverflowDuplicate).setVisible(false);
                    menu.getMenu().findItem(R.id.mnuGroundFightOverflowSetAsAlly).setVisible(false);
                    menu.getMenu().findItem(R.id.mnuGroundFightOverflowSetAsEnnemy).setVisible(false);
                    menu.setOnMenuItemClickListener(new onMenuItemClickListener(myFighter));
                    menu.show();
                }
            });

        }

        private void updateNonPlayerView(final GroundFighter myFighter, final int position) {


            NPCLayout.setVisibility(View.VISIBLE);
            btnInfos.setVisibility(View.VISIBLE);
            btnDamage.setVisibility(View.VISIBLE);

            woundView.setText(String.format("%s: %d/%d", woundView.getContext().getString(R.string.wounds),
                    myFighter.ActualWounds,
                    myFighter.TotalWounds));


            String status = myFighter.getStatus();
            for (String s : myFighter.Attacks)
                status += "\n" + "Attaque sur " + s;
            status += "\n";
            for (String s : myFighter.Defends)
                status += "\n" + "Attaqué par " + s;
            statusView.setText(status);


            woundBar.setProgress(myFighter.ActualWounds);
            woundBar.setMax(myFighter.TotalWounds);
            woundBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser)
                    {
                        myFighter.setActualWounds(progress);
                        String WoundString = String.format("%s: %d/%d", woundBar.getContext().getString(R.string.wounds),
                                myFighter.ActualWounds,
                                myFighter.TotalWounds);
                        woundView.setText(WoundString);

                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });


            if (myFighter.getBase().Type == NPC.NPCTypes.Nemesis) {



                strainView.setText(String.format("%s: %d/%d", App.getContext().getString(R.string.strain),
                        myFighter.ActualStrain,
                        myFighter.TotalStrain));
                strainBar.setProgress(myFighter.ActualStrain);
                strainBar.setMax(myFighter.TotalStrain);
                strainBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        if (fromUser)
                            myFighter.setActualStrain(progress);

                        strainView.setText(String.format("%s: %d/%d", App.getContext().getString(R.string.strain),
                                myFighter.ActualStrain,
                                myFighter.TotalStrain));

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

            }

            else {
                strainBar.setVisibility(View.GONE);
                strainView.setVisibility(View.GONE);

            }

            //spinner
            int iColor = R.drawable.spinner_background_green;
            if (myFighter.Played >= 0)
                iColor = R.drawable.spinner_background_red;


            //manoeuvres

            List<SWListBoxItem> maneuvers = new ArrayList<>();

            int iPosition = 0;
            int iCount = 0;
            for(String str : myFighter.GetPossibleManeuvers()) {
                if (myFighter.LastManeuver.equals(str)) iPosition = iCount;
                if (str.contains("#"))
                {
                    String[] split = str.split("[#]");
                    maneuvers.add(new SWListBoxItem(split[0], split[1]));
                }
                else
                    maneuvers.add(new SWListBoxItem(str, ""));

                iCount++;
            }

            final SWListBoxItemAdapter maneuverAdapter =  new SWListBoxItemAdapter(App.getContext(), maneuvers);

            maneuverSpinner.setAdapter(maneuverAdapter);


            maneuverSpinner.setBackgroundResource(iColor);

            maneuverSpinner.setSelection(iPosition);



            //Actions
            List<SWListBoxItem> actions = new ArrayList<>();

            iPosition = 0;
            iCount = 0;
            for(String str : myFighter.GetPossibleActions(GroundFightScene.PlayerNames, actionSpinner.getContext())) {
                if (myFighter.LastAction.equals(str)) iPosition = iCount;
                actions.add(new SWListBoxItem(str, ""));
                iCount++;
            }


            final SWListBoxItemAdapter actionAdapter =  new SWListBoxItemAdapter(App.getContext(), actions);

            actionSpinner.setAdapter(actionAdapter);

            actionSpinner.setBackgroundResource(iColor);
            actionSpinner.setSelection(iPosition);


            //Buttons


            btnApply.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    GroundFightScene.setPlayed(position,
                            ((SWListBoxItem) actionSpinner.getSelectedItem()).getName(),
                            ((SWListBoxItem) maneuverSpinner.getSelectedItem()).getName()
                    );

                }
            });

            btnDamage.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    myFighter.DamageUI(btnDamage.getContext(), GroundFighterAdapter.this, new GenericEditor.IOnPopupClosed() {
                        @Override
                        public void OnClosed() {

                            if (myFighter.getCount() == 0) {
                                new AlertDialog.Builder(btnDamage.getContext())
                                        .setTitle(R.string.remove_dead_player)
                                        .setMessage(R.string.remove_dead_player_question)
                                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (myFighter.isPlayer)
                                                    GroundFightScene.Players.add(myFighter.getBase());

                                                Fighters.remove(myFighter);
                                                GroundFighterAdapter.this.notifyDataSetChanged();
                                                GroundFightScene.getInitiativeAdapter().updateData(Fighters);
                                                dialog.dismiss();
                                            }
                                        })
                                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }

                                        })
                                        .show();
                            }

                        }
                    });

                }
            });

            btnInfos.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    ShowNPCActivity.loadFromNPCName(btnInfos.getContext(), myFighter.getBase().Name);

                }
            });

            btnOverflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu menu = new PopupMenu(btnOverflow.getContext(), btnOverflow);
                    menu.getMenuInflater().inflate(R.menu.ground_fight_overflow, menu.getMenu());
                    if (myFighter.isAlly())
                        menu.getMenu().findItem(R.id.mnuGroundFightOverflowSetAsAlly).setVisible(false);
                    else
                        menu.getMenu().findItem(R.id.mnuGroundFightOverflowSetAsEnnemy).setVisible(false);
                    menu.setOnMenuItemClickListener(new onMenuItemClickListener(myFighter));
                    menu.show();
                }
            });

        }

        private class onMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

            GroundFighter fighter;

            public onMenuItemClickListener(GroundFighter myFighter) {
                this.fighter = myFighter;
            }

            /**
             * This method will be invoked when a menu item is clicked if the item itself did
             * not already handle the event.
             *
             * @param item {@link MenuItem} that was clicked
             * @return <code>true</code> if the event was handled, <code>false</code> otherwise.
             */
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (item.getItemId() == R.id.mnuGroundFightOverflowDuplicate) {
                    GroundFighter duplicate = new GroundFighter(GroundFightScene.PlayerNames.size());
                    duplicate.setBase(fighter.getBase());
                    duplicate.setAlly(fighter.isAlly());
                    GroundFightScene.AddFighter(duplicate);
                    return true;
                }
                if (item.getItemId() == R.id.mnuGroundFightOverflowRemove) {
                    if (fighter.isPlayer)
                        GroundFightScene.Players.add(fighter.getBase());
                    Fighters.remove(fighter);
                    GroundFighterAdapter.this.notifyDataSetChanged();
                    GroundFightScene.getInitiativeAdapter().updateData(Fighters);
                    return true;
                }
                if (item.getItemId() == R.id.mnuGroundFightOverflowSetAsAlly)
                {
                    fighter.setAlly(true);
                    GroundFighterAdapter.this.notifyDataSetChanged();
                    GroundFightScene.getInitiativeAdapter().updateData(Fighters);
                    return true;
                }
                if (item.getItemId() == R.id.mnuGroundFightOverflowSetAsEnnemy)
                {
                    fighter.setAlly(false);
                    GroundFighterAdapter.this.notifyDataSetChanged();
                    GroundFightScene.getInitiativeAdapter().updateData(Fighters);
                    return true;
                }
                return false;
            }
        }
    }

}
