package com.dragonrider.swrpgcompanion.GroundFightActivities;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.Classes.Database;
import com.dragonrider.swrpgcompanion.Classes.PlayerCharacter;
import com.dragonrider.swrpgcompanion.R;
import com.dragonrider.swrpgcompanion.Classes.App;
import com.dragonrider.swrpgcompanion.Classes.GroundFightScene;
import com.dragonrider.swrpgcompanion.Classes.GroundFighter;
import com.dragonrider.swrpgcompanion.Classes.NPC.NPCTypes;
import com.dragonrider.swrpgcompanion.Classes.SWListBoxItem;
import com.dragonrider.swrpgcompanion.Classes.SWListBoxItemAdapter;
import com.dragonrider.swrpgcompanion.NPCViewer.ShowNPCActivity;

@Deprecated
public class OldGroundFighterAdapter extends BaseAdapter {
	
	private List<GroundFighter> innerFighters = new ArrayList<>();
	
	private Context actualContext;
	
	public OldGroundFighterAdapter(Context actualContext) {
		this.actualContext = actualContext;
        PlayerBitmaps = new Hashtable<>();
	}

	public Context getActualContext() {
		return actualContext;
	}
	
	public void setFighters(List<GroundFighter> list) {
		innerFighters = list;
	}
	
	public List<GroundFighter> getFighters() {
		return innerFighters;
	}
	
	

	@Override
	public int getCount() {
		return innerFighters.size();
	}

	@Override
	public Object getItem(int position) {
		return innerFighters.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}




	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		final GroundFighter myFighter = innerFighters.get(position);
		
		if (myFighter.isPlayer)
            return getPlayerView(position, convertView, parent, myFighter);




		
		
		if (myFighter.getCount() == 0)
            return new LinearLayout(actualContext);

		convertView = LayoutInflater.from(App.getContext()).inflate(R.layout.listitem_groundfighter, parent, false);

		
		if (myFighter.Played >= 0)
			convertView.findViewById(R.id.GroundFighterItemBackgroundLayout).setBackgroundResource(R.drawable.groundfighteritem_played_border);
		
		convertView.findViewById(R.id.GroundFighterItemImage).setBackground(myFighter.getColor());
		
		if (myFighter.getBase().getImage() != null) {
			
			((ImageView)convertView.findViewById(R.id.GroundFighterItemImage)).setImageBitmap(myFighter.getBase().getImage());
			
		}
		
		
		String fighterName = innerFighters.get(position).Name;
		
		if (innerFighters.get(position).getCount() > 1)
			fighterName = String.format("%dx%s", 
					innerFighters.get(position).getCount(),
					fighterName);
		((TextView)convertView.findViewById(R.id.GroundFighterItemName)).setText(fighterName);
		
		String WoundString = String.format("%s: %d/%d", App.getContext().getString(R.string.wounds),
                myFighter.ActualWounds,
                myFighter.TotalWounds);
        //int WoundValue = myFighter.TotalWounds - myFighter.ActualWounds;
		


        String status = myFighter.getStatus();

        for (String s : myFighter.Attacks)
            status += "\n" + "Attaque sur " + s;

        status += "\n";

        for (String s : myFighter.Defends)
            status += "\n" + "Attaqué par " + s;



        ((TextView)convertView.findViewById(R.id.GroundFighterItemStatusText)).setText(status);
		
		
		final TextView txtWounds = ((TextView)convertView.findViewById(R.id.GroundFighterItemWounds));
		txtWounds.setText(WoundString);

		((SeekBar)convertView.findViewById(R.id.GroundFighterItemWoundsBar)).setProgress(myFighter.ActualWounds);
		((SeekBar)convertView.findViewById(R.id.GroundFighterItemWoundsBar)).setMax(myFighter.TotalWounds);
        final View finalConvertView = convertView;
        ((SeekBar)convertView.findViewById(R.id.GroundFighterItemWoundsBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                {
                    myFighter.setActualWounds(progress);
                    String WoundString = String.format("%s: %d/%d", App.getContext().getString(R.string.wounds),
                            myFighter.ActualWounds,
                            myFighter.TotalWounds);
                    txtWounds.setText(WoundString);

                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


		if (innerFighters.get(position).getBase().Type == NPCTypes.Nemesis) {
			String StrainString = String.format("%s: %d/%d", App.getContext().getString(R.string.strain),
					myFighter.ActualStrain,
					myFighter.TotalStrain);
			//int StrainValue = myFighter.TotalStrain - myFighter.ActualStrain;

            final TextView txtStrain = ((TextView)convertView.findViewById(R.id.GroundFighterItemStrain));
            txtStrain.setText(StrainString);
			((SeekBar)convertView.findViewById(R.id.GroundFighterItemStrainBar)).setProgress(myFighter.ActualStrain);
			((SeekBar)convertView.findViewById(R.id.GroundFighterItemStrainBar)).setMax(myFighter.TotalStrain);
            ((SeekBar)convertView.findViewById(R.id.GroundFighterItemStrainBar)).setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser)
                        myFighter.setActualStrain(progress);
                    String StrainString = String.format("%s: %d/%d", App.getContext().getString(R.string.strain),
                            myFighter.ActualStrain,
                            myFighter.TotalStrain);
                    txtStrain.setText(StrainString);

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
			convertView.findViewById(R.id.GroundFighterItemStrain).setVisibility(View.GONE);
			convertView.findViewById(R.id.GroundFighterItemStrainBar).setVisibility(View.GONE);
			
		}
		
		
		//spinner
		int iColor = R.drawable.spinner_background_green;
		if (myFighter.Played >= 0)
			iColor = R.drawable.spinner_background_red;
				
		
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
		final Spinner ManeuverSpinner = (Spinner)convertView.findViewById(R.id.GroundFighterItemManeuverSpinner);
		ManeuverSpinner.setAdapter(maneuverAdapter);
		
		
		ManeuverSpinner.setBackgroundResource(iColor);
		
		ManeuverSpinner.setSelection(iPosition);


		
		List<SWListBoxItem> actions = new ArrayList<>();

		iPosition = 0;
		iCount = 0;
		for(String str : myFighter.GetPossibleActions(GroundFightScene.PlayerNames, actualContext)) {
			if (myFighter.LastAction.equals(str)) iPosition = iCount;
			actions.add(new SWListBoxItem(str, ""));
			iCount++;
		}
		

		final SWListBoxItemAdapter actionAdapter =  new SWListBoxItemAdapter(App.getContext(), actions);
		final Spinner ActionSpinner = (Spinner)convertView.findViewById(R.id.GroundFighterItemActionSpinner);
		ActionSpinner.setAdapter(actionAdapter);
		
		ActionSpinner.setBackgroundResource(iColor);
		ActionSpinner.setSelection(iPosition);

		
		//buttons
		
		convertView.findViewById(R.id.GroundFighterItemButtonApply).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                GroundFightScene.setPlayed(position,
                        ((SWListBoxItem) ActionSpinner.getSelectedItem()).getName(),
                        ((SWListBoxItem) ManeuverSpinner.getSelectedItem()).getName()
                );

            }
        });
		
		convertView.findViewById(R.id.GroundFighterItemButtonDamage).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                GroundFightScene.Fighters.get(position).DamageUI(actualContext, null, null);

            }
        });
		
		convertView.findViewById(R.id.GroundFighterItemButtonInfos).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ShowNPCActivity.loadFromNPCName(actualContext, GroundFightScene.Fighters.get(position).getBase().Name);

            }
        });
		
		return convertView;
	}


    static Hashtable<String, Bitmap> PlayerBitmaps;


    private View getPlayerView(final int position, View convertView, ViewGroup parent, GroundFighter myFighter) {



        if (convertView == null || convertView.getTag() == null) {

            convertView = LayoutInflater.from(actualContext).inflate(R.layout.listitem_groundfighter_player, parent, false);


            if (PlayerBitmaps.containsKey(myFighter.Name))
                ((ImageView) convertView.findViewById(R.id.GroundFighterItemImage)).setImageBitmap(PlayerBitmaps.get(myFighter.Name));
            else {
                Database db = new Database(actualContext);
                List<PlayerCharacter> pcs = db.getAllPC();
                for (PlayerCharacter pc : pcs) {

                    final int maxSize = 100;
                    int outWidth;
                    int outHeight;
                    int inWidth = pc.getImage().getWidth();
                    int inHeight = pc.getImage().getHeight();
                    if (inWidth > inHeight) {
                        outWidth = maxSize;
                        outHeight = (inHeight * maxSize) / inWidth;
                    } else {
                        outHeight = maxSize;
                        outWidth = (inWidth * maxSize) / inHeight;
                    }
                    Bitmap rescaled = Bitmap.createScaledBitmap(pc.getImage(), outWidth, outHeight, false);

                    PlayerBitmaps.put(pc.CharacterName, rescaled);

                    if (pc.CharacterName.equals(myFighter.Name)) {
                        ((ImageView) convertView.findViewById(R.id.GroundFighterItemImage)).setImageBitmap(rescaled);
                    }
                }
            }


            ((TextView) convertView.findViewById(R.id.GroundFighterPlayerItemName)).setText(innerFighters.get(position).Name);
            convertView.findViewById(R.id.GroundFighterItemButtonApply).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    GroundFightScene.setPlayed(position, "", "");

                }
            });



        }


        if (myFighter.Played >=0)
            convertView.findViewById(R.id.GroundFighterItemBackgroundLayout).setBackgroundResource(R.drawable.groundfighteritem_played_border);

        ((TextView) convertView.findViewById(R.id.GroundFighterPlayerItemStatus)).setText(
                String.format("%s", innerFighters.get(position).Played >= 0 ? actualContext.getString(R.string.fightstatus_played) : "")
        );
        return convertView;
    }

}
