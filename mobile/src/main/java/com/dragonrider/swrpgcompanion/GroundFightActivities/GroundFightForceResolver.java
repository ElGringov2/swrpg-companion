package com.dragonrider.swrpgcompanion.GroundFightActivities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.R;
import com.dragonrider.swrpgcompanion.Classes.DiceRoller;
import com.dragonrider.swrpgcompanion.Classes.DiceRoller.IValidateRoll;
import com.dragonrider.swrpgcompanion.Classes.GroundFightScene;
import com.dragonrider.swrpgcompanion.Classes.GroundFighter;
import com.dragonrider.swrpgcompanion.Classes.RollResult;
import com.dragonrider.swrpgcompanion.Classes.Util;

public class GroundFightForceResolver extends Activity {
	
	public static void Resolve(Context actualContext, int position) {
		Intent intent = new Intent(actualContext, GroundFightForceResolver.class);
		intent.putExtra("ATTACKER", position);

		actualContext.startActivity(intent);
		
	}
	
	RollResult innerRollResult;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_ground_fight_force_resolver);
		
		
		innerRollResult = new RollResult();

		
		
		List<String> lst = new ArrayList<String>();
		GroundFighter fighter = GroundFightScene.getFighters().get(getIntent().getExtras().getInt("ATTACKER"));
		
		innerRollResult.DiceForce = fighter.getForceDices();
		
		innerRollResult.PopulateFullView(this, (ViewGroup) findViewById(R.id.ForceResolverMainRollResult));
		
		for (String str : fighter.getBase().Abilities) {
			if (str.startsWith(getString(R.string.force_power)))
				lst.add(str);
		}
		
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, lst) {
			public android.view.View getView(int position, android.view.View convertView, ViewGroup parent) {
				View view = super.getView(position, convertView, parent);
				Util.setTextViewSymbols((TextView) view, getItem(position));
				return view;
				
			};
		};
		((ListView)findViewById(R.id.ForceResolverForceList)).setAdapter(adapter);
		
		((ImageButton)findViewById(R.id.ForceResolverChangeRoll)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				DiceRoller roller = new DiceRoller(GroundFightForceResolver.this);
				roller.setRollResult(innerRollResult);
				roller.setValidate(new IValidateRoll() {
					
					@Override
					public void validate(RollResult rr) {
						innerRollResult = rr;
						innerRollResult.PopulateFullView(GroundFightForceResolver.this, (ViewGroup) findViewById(R.id.ForceResolverMainRollResult));
						
					}
				});
				roller.ShowAlertDialog();
				
			}
		});
		
		
		
	}


}
