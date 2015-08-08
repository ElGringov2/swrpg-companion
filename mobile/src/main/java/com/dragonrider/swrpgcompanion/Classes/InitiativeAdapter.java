package com.dragonrider.swrpgcompanion.Classes;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.dragonrider.swrpgcompanion.R;
import com.dragonrider.swrpgcompanion.XWingWrapper.CrewWrapper;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by mge637 on 01/08/2015. Gere les listes d'initiative
 */
public class InitiativeAdapter<T> extends RecyclerView.Adapter<InitiativeAdapter.SimpleViewHolder> {

    /**
     * Liste des slots d'initiative (0 = PNJ, 1 = PJ)
     */
    private List<Integer> mSlots;
    /**
     * Liste des differents objets du combat qui ont déjà joué (null = vide, un object = déjà joué)
     */
    private List<T> mObjects;


    private Context context;

    public InitiativeAdapter(Context context) {
        this.context = context;
        this.mSlots = new ArrayList<>();
        this.mObjects = new ArrayList<>();
    }


    public class SimpleViewHolder extends RecyclerView.ViewHolder {

        public SimpleViewHolder(ImageView itemView) {
            super(itemView);

            imageView = itemView;
        }


        public ImageView imageView;



    }

    /**
     * Called when RecyclerView needs a new {@link SimpleViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(SimpleViewHolder, int)}. Since it will be re-used to display different
     * items in the data set, it is a good idea to cache references to sub views of the View to
     * avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(SimpleViewHolder, int)
     */
    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleViewHolder(new ImageView(parent.getContext()));
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method
     * should update the contents of the {@link SimpleViewHolder#itemView} to reflect the item at
     * the given position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this
     * method again if the position of the item changes in the data set unless the item itself
     * is invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside this
     * method and should not keep a copy of it. If you need the position of an item later on
     * (e.g. in a click listener), use {@link SimpleViewHolder#getPosition()} which will have the
     * updated position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(InitiativeAdapter.SimpleViewHolder holder, final int position) {


        int value = (int)(64 * App.getContext().getResources().getDisplayMetrics().density);

        holder.imageView.setLayoutParams(new RecyclerView.LayoutParams(value, value));
        holder.imageView.setImageBitmap(null);
        holder.imageView.setBackground(null);

        if (mObjects.get(position) == null) {

            if (mSlots.get(position) == 1)
                holder.imageView.setBackgroundResource(R.drawable.playeradd);
            else
                holder.imageView.setBackgroundResource(R.drawable.useradd);


        }
        else {

            if (mObjects.get(position) instanceof CrewWrapper)
                holder.imageView.setImageBitmap(((CrewWrapper)mObjects.get(position)).baseNPC.getImage());
            if (mObjects.get(position) instanceof GroundFighter)
                holder.imageView.setImageBitmap(((GroundFighter)mObjects.get(position)).getBase().getImage());
        }

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RaiseOnInitiativeClick(mSlots.get(position) == 1);
            }
        });


    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return mSlots.size();
    }



    public static InitiativeAdapter<CrewWrapper> fromCrewWrapperList(Context context, List<CrewWrapper> crewWrappers) {

        InitiativeAdapter<CrewWrapper> initiativeAdapter = new InitiativeAdapter<>(context);
        initiativeAdapter.updateData(crewWrappers);

        return initiativeAdapter;

    }


    public interface IInitiativeSlotClick {
        void Click(boolean isPlayer, Context context) ;
    }

    public IInitiativeSlotClick onInitiativeClick;

    private void RaiseOnInitiativeClick(boolean isPlayer) {
        if (onInitiativeClick != null)
            onInitiativeClick.Click(isPlayer, context);
    }

    public int Play(T player) {

        if (player instanceof GroundFighter && ((GroundFighter)player).Played != -1) return ((GroundFighter)player).Played;
        if (player instanceof CrewWrapper && ((CrewWrapper)player).Played != -1) return ((CrewWrapper)player).Played;

        int Position = 0;
        for (T obj : mObjects)
        {
            if (obj != null)
                Position++;
            else
                break;
        }
        if (Position == mObjects.size()) return -1;


        mObjects.set(Position, player);
        this.notifyDataSetChanged();

        return Position;
    }


    public void updateData(List<T> items) {
        if (items.size() == 0)
        {
            this.mSlots = new ArrayList<>();
            this.mObjects = new ArrayList<>();
        }
        else {
            this.mSlots = new ArrayList<>();
            this.mObjects = new ArrayList<>();
            for (int i = 0; i < items.size(); i++) {
                this.mObjects.add(null);
            }

            if (items.get(0) instanceof CrewWrapper)
                for(T wrapper : items)
                {
                    mSlots.add(((CrewWrapper)wrapper).isOnPlayerSlot ? 1 : 0);
                    if (((CrewWrapper)wrapper).Played != -1)
                        mObjects.set(((CrewWrapper) wrapper).Played, wrapper);


                }
            else if (items.get(0) instanceof GroundFighter)
                for (T fighter : items)
                {
                    mSlots.add(((GroundFighter) fighter).isAlly() ? 1 : 0);
                    if (((GroundFighter) fighter).Played >= 0)
                        mObjects.set(((GroundFighter) fighter).Played, fighter);
                }
        }


        this.notifyDataSetChanged();
    }
}
