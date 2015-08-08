package com.dragonrider.swrpgcompanion.Classes;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.dragonrider.swrpgcompanion.Classes.NPC.NPCTypes;
import com.dragonrider.swrpgcompanion.GroundFightActivities.GroundFighterAdapter;
import com.dragonrider.swrpgcompanion.R;
import com.dragonrider.swrpgcompanion.XWingWrapper.Initiative;

import java.util.ArrayList;
import java.util.List;

public class GroundFighter {

	public boolean IsActive = false;
	public String id;
	public int ActualWounds;
    private boolean ally = false;

    public GroundFighter setActualWounds(int Wounds) {
        this.ActualWounds = Wounds;
        return this;
    }

	public int ActualStrain;

    public GroundFighter setActualStrain (int Strain) {
        this.ActualStrain = Strain;
        return this;
    }
	private NPC Base = new NPC();
	public Weapon InHand;
    public String Name;


    public String getFullName() {

        if (Base == null) return Name;
        String sReturn = Base.Name;

        if (Color.getColor() == 0xFFFF0000) sReturn += " (Rouge)";
        if (Color.getColor() == 0xFF00FF00) sReturn += " (Vert)";
        if (Color.getColor() == 0xFF0000FF) sReturn += " (Bleu)";
        if (Color.getColor() == 0xFFFFFF00) sReturn += " (Jaune)";
        if (Color.getColor() == 0xFF00FFFF) sReturn += " (Cyan)";
        if (Color.getColor() == 0xFFFF00FF) sReturn += " (Magenta)";

        return sReturn;


    }

    public int X;
    public int Y;

    public GroundFighter setCoord(int x, int y) {
        this.X = x;
        this.Y = y;
        return this;
    }

	public int TotalWounds = 0;

	public int TotalStrain = 0;
	
	private int playerCount;
	public boolean isPlayer = false;

	public String LastAction = "Aucune";
	public String LastManeuver = "Aucune";
	
	public void setLastAction(String lastAction) {
		LastAction = lastAction;
	}
	public void setLastManeuver(String lastManeuver) {
		LastManeuver = lastManeuver;
	}
	
	public List<String> Attacks = new ArrayList<>();
    public List<String> Defends = new ArrayList<>();

    public void addAttack(String name) {
        Attacks.add(name);
    }

    public void addDefends(String name) {
        Defends.add(name);
    }

	private ColorDrawable Color = new ColorDrawable(0x00000000);
	
	
	
	private int aimingBonus = 0;
	
	public void setAimingBonus(int Bonus) {
		aimingBonus = Bonus;
		if (aimingBonus > 2) aimingBonus = 2;
		
		
	}
	public int getAimingBonus(){
		return aimingBonus;
	}
	
	public void increaseAimingBonus() {
		if (aimingBonus < 2)
			aimingBonus++;
	}
	
	public Boolean isDefensive = false;
	public Boolean isProne = false;
	public Boolean isCover = false;

	private Initiative mainInitiative = new Initiative();
	
	public Initiative getMainInitiative() {
		return mainInitiative;
	}


    public void setMainInitiative(int Triumph, int Success, int Advantage) {
        this.mainInitiative.Triumph = Triumph;
        this.mainInitiative.Success = Success;
        this.mainInitiative.Advantages = Advantage;

    }
	

	
	public void setInitiativeRoll(RollResult initiativeRoll) {
        this.mainInitiative.Triumph = initiativeRoll.Triumph();
        this.mainInitiative.Success = initiativeRoll.Succcess();
        this.mainInitiative.Advantages = initiativeRoll.Advantage();
	}
	

	
	public int Played = -1;
	
	public List<CriticalInjury> Criticals = new ArrayList<>();


    
	
	public GroundFighter(int PlayerCount) {
		playerCount = PlayerCount;

	}


	@Override
	public String toString() {
		String sReturn = "";
		if (Base.Type.equals(NPCTypes.Minion)) sReturn = String.valueOf(getCount()) + "x";
		sReturn += Base.Name + "\nW" + ActualWounds + "/" + TotalWounds + ", S" + ActualStrain + "/" + TotalStrain;
		String sThirdLine = "";
		if (Played >= 0) sThirdLine += App.getContext().getString(R.string.fightstatus_played);
		if (isProne) sThirdLine += (sThirdLine.equals("") ? "" : " - ") + App.getContext().getString(R.string.fightstatus_prone);
		if (isCover) sThirdLine += (sThirdLine.equals("") ? "" : " - ") + App.getContext().getString(R.string.fightstatus_cover);
		if (isDefensive) sThirdLine += (sThirdLine.equals("") ? "" : " - ") + App.getContext().getString(R.string.fightstatus_defensive);
		
		if (IsActive) sThirdLine += "\nActif";

		
		if (!sThirdLine.equals("")) sReturn += "\n" + sThirdLine;
		return sReturn;
	}


	
	
	
	public GroundFighter setBase(NPC npc){
		Base= npc;
		if (Base == null) {
			isPlayer = true;
			return this;
		}
		if (Base.Type.equals(NPCTypes.Minion)){
			
			TotalWounds = playerCount * Base.TotalWounds;
			TotalStrain = 0;
		}
		if (Base.Type.equals(NPCTypes.Nemesis)) {
			TotalWounds = Base.TotalWounds;
			TotalStrain = Base.TotalStrain;
				
		}
		if (Base.Type.equals(NPCTypes.Rival))
		{
			TotalWounds = Base.TotalWounds;
			TotalStrain = 0;
		}
		
		
		

        this.Name = Base.Name;
		return this;
		
	}
	public NPC getBase() {
		return Base;
	}
	
	public int getCount() {

        if (isPlayer)
            return 1;

		if (ActualWounds >= TotalWounds)
			return 0;
		
		if (Base.Type.equals(NPCTypes.Minion)) {

            return (int) Math.ceil(((double)TotalWounds - (double)ActualWounds) / (double)TotalWounds * playerCount);
			
		}
		else return 1;
		
		
		
	}
	
	
	public RollResult GetSkillRollResult(int skillID)
    {


		Skill sk = Base.GetSkill(skillID);



        int skValue = sk.Value;
        if (Base.Type == NPCTypes.Minion)
            skValue = (skValue > 0 ? getCount() - 1 : 0);

        RollResult rr = new RollResult();
        




        int iCharValue = Base.Characteristics.get(sk.CharacteristicID);

        if (iCharValue > skValue)
        {
            rr.DiceAbility = iCharValue;
            rr.UpgradePositivePool(skValue);

        }
        else
        {
            rr.DiceAbility = skValue;
            rr.UpgradePositivePool(iCharValue);
        }


        return rr;
    }
	

    public List<String> GetPossibleManeuvers () {
        List<String> lst = new ArrayList<>();
        lst.add(App.getContext().getString(R.string.none));
        if (aimingBonus <= 1) lst.add(App.getContext().getString(R.string.action_aim_plusoneboost) + "#" + App.getContext().getString(R.string.action_desc_aim_plusoneboost));
        lst.add(App.getContext().getString(R.string.action_assist) + "#" + App.getContext().getString(R.string.action_desc_assist));
        lst.add(App.getContext().getString(R.string.action_defensivestance_desc) + "#" + App.getContext().getString(R.string.action_desc_defensivestance_desc) );
        lst.add(App.getContext().getString(R.string.action_movinglargeitem));
        lst.add(App.getContext().getString(R.string.action_open_close_door));
        if (!isCover) lst.add(App.getContext().getString(R.string.action_cover_desc) + "#" + App.getContext().getString(R.string.action_desc_cover_desc));
        lst.add(App.getContext().getString(R.string.action_mound_dismount));
        lst.add(App.getContext().getString(R.string.action_move));
        if (isProne) lst.add(App.getContext().getString(R.string.action_standfromprone));
        else lst.add(App.getContext().getString(R.string.action_drop_prone) + "#" + App.getContext().getString(R.string.action_desc_drop_prone));
        lst.add(App.getContext().getString(R.string.action_prepare));
        for (Item item : Base.Items)
        {
        	if (item.getClass() != Weapon.class) continue;
            if (this.InHand != item)
                lst.add(App.getContext().getString(R.string.action_equipweapon) + " " + item.Name);
            else
                lst.add(App.getContext().getString(R.string.action_holsterweapon) + " " + item.Name);
        }
        return lst;
    }

    public void setPlayerCount(int iCount) {
        this.playerCount = iCount;
        if (Base.Type.equals(NPCTypes.Minion)){

            TotalWounds = iCount * Base.TotalWounds;
            TotalStrain = 0;
        }
    }

    
    
    public List<String> GetPossibleActions(List<String> PlayerNames, Context context)
    {

        List<String> lst = new ArrayList<>();
        lst.add(context.getString(R.string.none));
        lst.add(context.getString(R.string.swap_action_maneuver));
        lst.add(context.getString(R.string.special_abilities));
        if (getBase().ForceRating > 0)
        	lst.add(context.getString(R.string.action_force_power));
        lst.add(context.getString(R.string.skill));
        for (String item : PlayerNames)
            lst.add("Attaque sur " + item);
        for (GroundFighter fighter : GroundFightScene.getFighters()) {
            if (PlayerNames.contains(fighter.Name)) continue;
            if (fighter.equals(this)) continue;

            lst.add("Attaque sur " + fighter.getFullName());

        }

        

        return lst;
    }
	public ColorDrawable getColor() {
		return Color;
	}
	
	public void setColor(int iId) {
		ColorDrawable[] colors = {
                new ColorDrawable(0x00FF0000),
				new ColorDrawable(0xFFFF0000),
				new ColorDrawable(0xFF00FF00),
				new ColorDrawable(0xFF0000FF),
                new ColorDrawable(0xFFFFFF00),
                new ColorDrawable(0xFF00FFFF),
				new ColorDrawable(0xFFFF00FF),
		};
		this.Color = colors[iId];
	}
	
//
//    private List<String> getAllFormattedDamageText() {
//        ArrayList<String> list = new ArrayList<>();
//        for (int i = getBase().Soak; i < TotalWounds + getBase().Soak - ActualWounds + 1; i++)
//            list.add(formatDamageText(i));
//
//        return list;
//
//    }
//
//
//    private String formatDamageText(int Damage) {
//
//        int TotalSoak = getBase().Soak;
//        int ArmorSoak = getBase().Soak - getBase().Characteristics.get(0);
//
//
//        String sReturn = String.format("%d", Damage);
//
//
//
//        if (Damage <= TotalSoak) {
//            sReturn += " ou moins: pas de dégâts.";
//            return sReturn;
//        }
//
//        if (Damage - TotalSoak >= (TotalWounds - ActualWounds)) {
//            sReturn += " ou plus: Mort.";
//            return sReturn;
//        }
//
//        sReturn += ": " + String.valueOf(Damage - TotalSoak) + " dégâts (";
//        for (int iIgnoreArmor = 1; iIgnoreArmor <= ArmorSoak; iIgnoreArmor++) {
//            if (Damage - (TotalSoak - iIgnoreArmor) >= (TotalWounds - ActualWounds))
//                sReturn += String.format("mort avec Pierce %s, ", iIgnoreArmor);
//            else {
//                sReturn += String.format("%d avec Pierce %d, ", Damage - (TotalSoak - iIgnoreArmor), iIgnoreArmor);
//
//                if (getBase().Type == NPCTypes.Minion)
//                    sReturn += String.format("reste %d larbins, ",
//                            (int) Math.ceil((((double) TotalWounds - (double) (ActualWounds + Damage - TotalSoak)) / (double) TotalWounds) * playerCount));
//
//
//            }
//
//
//        }
//
//        sReturn = (sReturn + "!!!").replace(", !!!", ")").replace("!!!", ")");
//
//
//        return sReturn;
//
//
//    }
//

	
	public void DamageUI(final Context context, final GroundFighterAdapter parentAdapter, final GenericEditor.IOnPopupClosed onPopupClosed) {

        //List<String> strings = getAllFormattedDamageText();


		//ArrayAdapter<String> adapter = new ArrayAdapter<> (context, android.R.layout.simple_dropdown_item_1line, strings);
		
		
		
		View baseView = LayoutInflater.from(context).inflate(R.layout.popup_damage_groundfighter, null);
		
		//ListView lv =  (ListView) baseView.findViewById(R.id.popupDamageGroundFighterMainList);
		//lv.setAdapter(adapter);
		
		
		

		TextView tview = (TextView) baseView.findViewById(R.id.popupDamageGroundFighterDifficulty);
		
		int iUpgrades = Talent.getCount(Talent.TalentIDs.adversary, getBase());

		
		RollResult diffRoll = new RollResult();
		diffRoll.DiceDifficulty = 2;
		diffRoll.DiceSetback = getBase().MeleeDefense + (isDefensive ? 1 : 0);
		diffRoll.DiceBoost = (isProne ? 1 : 0);
		diffRoll.UpgradeNegativePool(iUpgrades);
		String text = "CàC:" + diffRoll.toSymbolFormattableString();
				
		diffRoll = new RollResult();
		diffRoll.DiceDifficulty = 1;
		diffRoll.DiceSetback = getBase().RangedDefense + (isCover ? 1 : 0) + (isProne ? 1 : 0);
		diffRoll.UpgradeNegativePool(iUpgrades);
		text += "\nCourte:" + diffRoll.toSymbolFormattableString(); 
		
				
		diffRoll = new RollResult();
		diffRoll.DiceDifficulty = 2;
		diffRoll.DiceSetback = getBase().RangedDefense + (isCover ? 1 : 0) + (isProne ? 1 : 0);
		diffRoll.UpgradeNegativePool(iUpgrades);
		text += "\nMoyenne:" + diffRoll.toSymbolFormattableString(); 
		
		diffRoll = new RollResult();
		diffRoll.DiceDifficulty = 3;
		diffRoll.DiceSetback = getBase().RangedDefense + (isCover ? 1 : 0) + (isProne ? 1 : 0);
		diffRoll.UpgradeNegativePool(iUpgrades);
		text += "\nLongue:" + diffRoll.toSymbolFormattableString(); 
		
		diffRoll = new RollResult();
		diffRoll.DiceDifficulty = 4;
		diffRoll.DiceSetback = getBase().RangedDefense + (isCover ? 1 : 0) + (isProne ? 1 : 0);
		diffRoll.UpgradeNegativePool(iUpgrades);
		text += "\nExtrème:" + diffRoll.toSymbolFormattableString();
		
		Util.setTextViewSymbols(tview, text);


        final TextView resultTextView = (TextView)baseView.findViewById(R.id.textView1);
        final TextView damageTextView = (TextView)baseView.findViewById(R.id.txtDamage);

        final int brawnSoak = this.getBase().Characteristics.get(Characteristic.Characteristics.Brawn.ordinal());

        final int armorSoak = this.getBase().Soak - brawnSoak;

        final int meleeLightSaber = this.getBase().getTalent(Talent.TalentIDs.parry) + 2;

        final int rangedLightSaber = this.getBase().getTalent(Talent.TalentIDs.reflect) + 2;


        final SeekBar damageSeekBar = (SeekBar) baseView.findViewById(R.id.seekBar1);

        final RadioButton rdoBreach1 = (RadioButton) baseView.findViewById(R.id.rdoBreach2);
        final RadioButton rdoBreach2 = (RadioButton) baseView.findViewById(R.id.rdoBreach3);

        final RadioButton rdoPierce1 = (RadioButton) baseView.findViewById(R.id.rdoPierce2);
        final RadioButton rdoPierce2 = (RadioButton) baseView.findViewById(R.id.rdoPierce3);
        final RadioButton rdoPierce3 = (RadioButton) baseView.findViewById(R.id.rdoPierce4);
        final RadioButton rdoPierce4 = (RadioButton) baseView.findViewById(R.id.rdoPierce5);

        final RadioButton rdoCritical1 = (RadioButton) baseView.findViewById(R.id.rdoCritical2);
        final RadioButton rdoCritical2 = (RadioButton) baseView.findViewById(R.id.rdoCritical3);
        final RadioButton rdoCritical3 = (RadioButton) baseView.findViewById(R.id.rdoCritical4);
        final RadioButton rdoCritical4 = (RadioButton) baseView.findViewById(R.id.rdoCritical5);

        damageSeekBar.setMax(30);
        damageSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                int finalSoak = armorSoak -
                        (rdoBreach1.isChecked() ? 10 : 0) -
                        (rdoBreach2.isChecked() ? 20 : 0) -
                        (rdoPierce1.isChecked() ? 1 : 0) -
                        (rdoPierce2.isChecked() ? 1 : 0) -
                        (rdoPierce3.isChecked() ? 1 : 0) -
                        (rdoPierce4.isChecked() ? 1 : 0);
                if (finalSoak < 0) finalSoak = 0;


                int finalMeleeDamage = progress - finalSoak - meleeLightSaber;
                if (finalMeleeDamage < 0) finalMeleeDamage = 0;

                int finalRangedDamage = progress - finalSoak - rangedLightSaber;
                if (finalRangedDamage < 0) finalRangedDamage = 0;

                String meleeDamageText = String.valueOf(finalMeleeDamage);
                if (finalMeleeDamage == 0) meleeDamageText = "Aucun dégâts.";
                if (finalMeleeDamage + ActualWounds >= TotalWounds) meleeDamageText = "A terre.";

                String rangedDamageText = String.valueOf(finalRangedDamage);
                if (finalRangedDamage == 0) rangedDamageText = "Aucun dégâts.";
                if (finalRangedDamage + ActualWounds >= TotalWounds) rangedDamageText = "A terre.";

                damageTextView.setText(String.valueOf(progress));

                resultTextView.setText(String.format("Mélée: %s\nDistance: %s", meleeDamageText, rangedDamageText));
                rdoBreach1.setTag(finalMeleeDamage);
                rdoPierce1.setTag(finalRangedDamage);


            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        damageSeekBar.setProgress(0);

		
		new AlertDialog.Builder(context)
		.setTitle("Infliger des dégâts")
		.setView(baseView)
		.setNegativeButton(R.string.cancel, new OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();

            }
        })
		.setPositiveButton(R.string.rangeddefense, new OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                int Damage = (int) rdoPierce1.getTag();

                int Critical = (rdoCritical1.isChecked() ? 1 : 0) +
                        (rdoCritical2.isChecked() ? 2 : 0) +
                        (rdoCritical3.isChecked() ? 3 : 0) +
                        (rdoCritical4.isChecked() ? 4 : 0);

                applyDamage(Damage, Critical, context, new GenericEditor.IOnPopupClosed() {
                    @Override
                    public void OnClosed() {
                        if (onPopupClosed != null) onPopupClosed.OnClosed();
                        parentAdapter.notifyDataSetChanged();
                    }
                });


                dialog.dismiss();


            }
        }).setNeutralButton(R.string.meleedefense, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int Damage =  (int) rdoBreach1.getTag();

                int Critical = (rdoCritical1.isChecked() ? 1 : 0) +
                        (rdoCritical2.isChecked() ? 2 : 0) +
                        (rdoCritical3.isChecked() ? 3 : 0) +
                        (rdoCritical4.isChecked() ? 4 : 0);

                applyDamage(Damage, Critical, context, new GenericEditor.IOnPopupClosed() {
                    @Override
                    public void OnClosed() {
                        if (onPopupClosed != null) onPopupClosed.OnClosed();
                        parentAdapter.notifyDataSetChanged();
                    }
                });


                dialog.dismiss();

            }
        }).show();
		
				
	}

    private void applyDamage(final int damage, int critical, final Context context, final GenericEditor.IOnPopupClosed onPopupClosed) {


        if (critical > 0) {
            CriticalUI(context, critical, new GenericEditor.IOnPopupClosed() {
                @Override
                public void OnClosed() {
                    applyDamage(damage, 0, context, onPopupClosed);
                }
            });
            return;
        }


        int oldValue = getCount();

        ActualWounds += damage;



        if (getCount() == 0) {
            new AlertDialog.Builder(context)
            .setTitle("Infliger des dégâts")
            .setMessage("Eliminé")
            .setPositiveButton(R.string.ok, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (onPopupClosed != null) onPopupClosed.OnClosed();
                    dialog.dismiss();

                }
            })
            .show();


        }
        else if (oldValue != getCount()) {
            new AlertDialog.Builder(context)
            .setTitle("Infliger des dégâts")
            .setMessage(String.format("%d mort%s",
                    oldValue - getCount(),
                    oldValue - getCount() > 1 ? "s" : ""))
            .setPositiveButton(R.string.ok, new OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (onPopupClosed != null) onPopupClosed.OnClosed();
                    dialog.dismiss();

                }
            })
            .show();
        }


    }

    public void CriticalUI(Context context, GenericEditor.IOnPopupClosed onPopupClosed) {
        CriticalUI(context, 1, onPopupClosed);
    }

	public void CriticalUI(Context context, final int CriticalCount, final GenericEditor.IOnPopupClosed onPopupClosed) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        if (Base.Type == NPCTypes.Minion) {
            builder.setMessage(String.format(context.getString(R.string.critical_autokill_minion), CriticalCount, CriticalCount > 1 ? "s" : ""));
            builder.setTitle(R.string.weapon_critical);
            builder.setPositiveButton(R.string.ok, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActualWounds += Base.TotalWounds * CriticalCount;
                    if (onPopupClosed != null) onPopupClosed.OnClosed();
                }
            });
            builder.setNegativeButton(R.string.cancel, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            builder.show();
            return;

        }

        int _criticals = this.Criticals.size() * 10;
        if (CriticalCount > 1) _criticals += (CriticalCount - 1) * 10;
        final List<String> lst1 = new ArrayList<>();
        List<String> lst2 = new ArrayList<>();

        for (int i = _criticals; i <= 151; i++) {
            String crit = CriticalInjury.getCriticalName(i);
            if (!lst1.contains(crit))
            {
                lst1.add(crit);
                lst2.add(String.format("%s (%d): %s", crit, i, CriticalInjury.getCriticalDescription(i)));

            }
        }

        builder.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, lst2), new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                CriticalInjury criticalInjury = new CriticalInjury(CriticalInjury.CriticalIDs.values()[i]);
                Criticals.add(criticalInjury);
                if (onPopupClosed != null) onPopupClosed.OnClosed();
            }
        });

        builder.setNegativeButton(R.string.cancel, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });


        builder.show();
    }




	private int equippedItemID = -1;
	
	public Weapon getEquippedWeapon() {
		if (equippedItemID == -1)
			return Weapon.getFists();
		
		else return (Weapon) Base.Items.get(equippedItemID);
		
		
		
	}
	
	
	public void setEquippedWeapon(int WeaponID) {
		if (Base.Items.get(WeaponID).getClass() == Weapon.class)
			this.equippedItemID = WeaponID;
		
	}
	public String getStatus() {
		if (ActualWounds >= TotalWounds)
			return "Mort";
		
		String s = "";
		
		if (isProne) 
			s += "A terre ";
		if (isDefensive)
			s += "Posture défensive ";
		if (isCover)
			s += "A couvert ";
		
		if (getAimingBonus() > 0)
			s += String.format("Visée (%d) ", getAimingBonus());
		
		s += "\n"; 
		
		
		for (CriticalInjury injury : Criticals) {
			s += injury.toString() + "\n";
		}
		
		
		
		return s.substring(0, s.length() - 1);
	}
	public void setCover(boolean b) {
		isCover = b;
		
	}
	public void setProne(boolean b) {
		isProne = b;
		
	}
	public void setDefensive(boolean b) {
		isDefensive = b;
		
	}
	
	int committedDices = 0;
	
	public int getForceDices() {
		
		return getBase().ForceRating - committedDices;
	}

    public void setName(String name) {
        this.Name = name;
    }

    public boolean isAlly() {
        return ally;
    }

    public void setAlly(boolean ally) {
        this.ally = ally;
    }
}
