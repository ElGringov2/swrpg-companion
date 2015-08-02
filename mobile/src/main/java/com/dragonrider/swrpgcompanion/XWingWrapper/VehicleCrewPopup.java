package com.dragonrider.swrpgcompanion.XWingWrapper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.Classes.Database;
import com.dragonrider.swrpgcompanion.Classes.NPC;
import com.dragonrider.swrpgcompanion.Classes.Skill;
import com.dragonrider.swrpgcompanion.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mge637 on 03/03/2015.
 *
 */
public class VehicleCrewPopup {

    public interface onValidatePopupListener {
        void onValidatePopup(Object Result);
    }

    public static void Show(Context context, onValidatePopupListener onValidate) {

        show(context, onValidate, new CrewAdapter(context), false);
    }

    public static void Show(Context context,onValidatePopupListener OnValidate, List<NPC> CustomList ) {

        BaseAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, CustomList);

        show(context, OnValidate, adapter, true);



    }


    private static void show(final Context context, final onValidatePopupListener onValidate, BaseAdapter adapter, boolean isPlayerMode) {


        crewAdapter = adapter;

        AlertDialog.Builder builder = new AlertDialog.Builder(context);


        builder.setAdapter(crewAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onValidate.onValidatePopup(crewAdapter.getItem(which));
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });


        if (!isPlayerMode)
            builder.setNeutralButton("Non-combattants", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    ((CrewAdapter)crewAdapter).IncludeNonFightersNPC();
                    dialog.dismiss();
                    show(context, onValidate, crewAdapter, true);

                }
            });

        builder.create().show();



    }

    private static BaseAdapter crewAdapter;



    private static class CrewAdapter extends BaseAdapter {

        private List<NPC> npcs;


        private List<NPC> nonFightersNPC;

        public void IncludeNonFightersNPC() {
            npcs.addAll(nonFightersNPC);

        }



        public CrewAdapter(Context context) {

            Database db=  new Database(context);

            npcs = new ArrayList<>();
            nonFightersNPC = new ArrayList<>();

            List<NPC> tempNPC = db.getAllNPC();

            for (NPC npc : tempNPC)
                if (npc.GetSkill(Skill.Skills.gunnery.ordinal()).Value > 0
                    || npc.GetSkill(Skill.Skills.piloting_space.ordinal()).Value > 0)
                    npcs.add(npc);
                else
                    nonFightersNPC.add(npc);

            db.close();

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
            if (npcs != null)
                return npcs.size();
            else
                return 0;
        }

        @Override
        public Object getItem(int position) {
            if (npcs != null)
                return npcs.get(position);
            else
                return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }


        private static class NPCViewHolder {
            public TextView TitleTextView;
            public TextView DescriptionTextView;
            public ImageView ItemImageView;

            public NPCViewHolder(View baseView) {
                TitleTextView = (TextView)baseView.findViewById(R.id.swListItem_Title);
                DescriptionTextView = (TextView)baseView.findViewById(R.id.swListItem_Desc);
                ItemImageView = (ImageView)baseView.findViewById(R.id.swListItem_Image);
            }
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null)
                convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem_swlistitem, parent, false);

            NPCViewHolder holder;
            if (convertView.getTag() == null)
                holder = new NPCViewHolder(convertView);
            else
                holder = (NPCViewHolder)convertView.getTag();



            holder.TitleTextView.setText(npcs.get(position).Name);
            holder.DescriptionTextView.setText(npcs.get(position).Description);
            holder.ItemImageView.setImageBitmap(npcs.get(position).getImage());

            convertView.setTag(holder);

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
            return npcs == null;
        }
    }
}
