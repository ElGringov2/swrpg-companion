package com.dragonrider.swrpgcompanion.GroundFightActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


import com.dragonrider.swrpgcompanion.Classes.NPC;
import com.dragonrider.swrpgcompanion.Classes.SWListBoxItem;
import com.dragonrider.swrpgcompanion.Classes.SWListBoxItemAdapter;
import com.dragonrider.swrpgcompanion.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mge637 on 07/08/2015. Ajoute un NPC
 */
public class SelectPlayerPopup {


    public interface IOnValidatePlayerAddPopup {
        void OnSelectPlayer(NPC Player);
    }

    public static void Show(Context context, List<NPC> Players, final IOnValidatePlayerAddPopup OnValidate ) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setTitle(R.string.add_player);

        final ArrayList<SWListBoxItem> list = new ArrayList<>();
        for (NPC player : Players)
            list.add(new SWListBoxItem(player.Name, "").setImage(player.getImage()).setTag(player));





        builder.setAdapter(new SWListBoxItemAdapter(context, list), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (OnValidate != null)
                    OnValidate.OnSelectPlayer((NPC) list.get(which).getTag());
            }
        });

        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });



        builder.show();
    }
}
