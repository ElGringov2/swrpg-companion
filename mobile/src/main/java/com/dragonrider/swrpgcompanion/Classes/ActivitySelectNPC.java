package com.dragonrider.swrpgcompanion.Classes;

import java.util.List;
import java.util.Objects;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import com.dragonrider.swrpgcompanion.R;

public class ActivitySelectNPC extends Activity {
	
	
	public interface ISelectNPC {
		void OnNPCSelected(String SelectedNPC, int Count) ;
	}
	
	public static ISelectNPC callback;
	
	public static void Show(Context context, boolean CloseOnExit, ISelectNPC Callback) {
		callback = Callback;
		Intent intent = new Intent(context, ActivitySelectNPC.class);
		intent.putExtra("CLOSEONEXIT", CloseOnExit);
		context.startActivity(intent);
	}
	


	private void updateListAdapter(List<SWListBoxItem> list) {
        final  SWGroupListBoxItemAdapter adapter1 = new SWGroupListBoxItemAdapter(this, list);



        ((ExpandableListView)findViewById(R.id.AddFighter_MainList)).setAdapter(adapter1);
        ((ExpandableListView)findViewById(R.id.AddFighter_MainList)).setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {


                final String npcName = ((SWListBoxItem) adapter1.getChild(groupPosition, childPosition)).getName();

                final LinearLayout ll = new LinearLayout(ActivitySelectNPC.this);
                for (int i = 0; i < 5; i++) {
                    ToggleButton toggle = new ToggleButton(ActivitySelectNPC.this);
                    toggle.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {

                            for (int iItm = 0; iItm < ll.getChildCount(); iItm++)
                                ((ToggleButton) ll.getChildAt(iItm)).setChecked(false);

                            ((ToggleButton) v).setChecked(true);

                        }
                    });
                    toggle.setText(String.format("x%d", i + 1));
                    toggle.setTextOn(String.format("x%d", i + 1));
                    toggle.setTextOff(String.format("x%d", i + 1));
                    if (i == 0) toggle.setChecked(true);
                    ll.addView(toggle);
                }


                if ((Boolean) ((SWListBoxItem) adapter1.getChild(groupPosition, childPosition)).getTag())
                    RaiseOnSelectedNPC(npcName, 1);
                else
                    new AlertDialog.Builder(ActivitySelectNPC.this)
                            .setTitle(R.string.dialog_howmuch)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    int count = 0;


                                    for (int iItm = 0; iItm < ll.getChildCount(); iItm++)
                                        if (((ToggleButton) ll.getChildAt(iItm)).isChecked())
                                            count = iItm + 1;


                                    RaiseOnSelectedNPC(npcName, count);

                                }


                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();

                                }
                            })
                            .setView(ll)
                            .show();

                return true;
            }
        });

        findViewById(R.id.waitProgressBar).setVisibility(View.GONE);
        findViewById(R.id.AddFighter_MainList).setVisibility(View.VISIBLE);



    }
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		setContentView(R.layout.activity_add_fighters);



        AsyncTask<Object, Integer, List<SWListBoxItem>> loadCharacters =  new AsyncTask<Object, Integer, List<SWListBoxItem>>() {


            @Override
            protected List<SWListBoxItem> doInBackground(Object... params) {

                Database db = new Database(ActivitySelectNPC.this);

                List<SWListBoxItem> list = db.GetNPCShortList();

                db.close();

                return list;


            }

            @Override
            protected void onPostExecute(List<SWListBoxItem> list) {
                updateListAdapter(list);
            }
        };
		
		loadCharacters.execute();

		


	
		
	}





	private void RaiseOnSelectedNPC(String npcName, int Count) {
		
		npcName = npcName.replace(App.getContext().getString(R.string.minion), "").replace(App.getContext().getString(R.string.nemesis), "").replace(App.getContext().getString(R.string.rival), "");
		npcName = npcName.replace(" []", "");
		
		callback.OnNPCSelected(npcName, Count);
	}
	
	
}
