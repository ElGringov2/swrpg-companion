package com.dragonrider.swrpgcompanion.Classes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;

import com.dragonrider.swrpgcompanion.GroundFightActivities.GroundFighterAdapter;
import com.dragonrider.swrpgcompanion.R;
import com.dragonrider.swrpgcompanion.GroundFightActivities.GroundFightAttackResolver;
import com.dragonrider.swrpgcompanion.GroundFightActivities.GroundFightForceResolver;
import com.dragonrider.swrpgcompanion.XWingWrapper.InitiativePopup;


public class GroundFightScene {
	
	public static GroundFighterAdapter MainAdapter;

    // Un contexte
    private static Context actualContext;

    //Liste temporaire des joueurs pour eviter doublons
    public static List<NPC> Players;
	
	public static List<GroundFighter> Fighters = new ArrayList<>();
	
	public static boolean isFoesSurprised = false;
	
	public static List<String> PlayerNames = new ArrayList<>();


	private static InitiativeAdapter<GroundFighter> initiativeAdapter;

	public static List<GroundFighter> getFighters() {
		    return MainAdapter.getFighters();
	}

    public static boolean getIsFighting() {
		return !(MainAdapter == null || MainAdapter.getFighters() == null || MainAdapter.getFighters().size() == 0);
	}



	public static void RefreshContext(Context context) {
		MainAdapter = new GroundFighterAdapter();
		MainAdapter.setFighters(Fighters);
        initiativeAdapter = new InitiativeAdapter<>(context);
        initiativeAdapter.updateData(Fighters);

        actualContext = context;


		
	}
	
	public static void Clear() {

        Players = XmlImport.ImportPCs();
        PlayerNames.clear();
        Fighters = new ArrayList<>();


    }


	public static void Initialize(Context context) {

        Clear();


        RefreshContext(context);

	
	}
	


	public static InitiativeAdapter<GroundFighter> getInitiativeAdapter() {

		return initiativeAdapter;
	}

	public interface INotifyFighterListChanged{
        void RunWhenFighterListChanged();
    }
    public static INotifyFighterListChanged FighterListChanged;

    public static void AddFighter(final GroundFighter groundFighter) {

        if (groundFighter.isPlayer) {

            InitiativePopup.Show(actualContext, groundFighter.Name, new InitiativePopup.IOnValidateInitiative() {
                @Override
                public void OnValidate(int Triumph, int Success, int Advantage) {
                    groundFighter.setMainInitiative(Triumph, Success, Advantage);
                }
            });
            PlayerNames.add(groundFighter.getFullName());
            Players.remove(groundFighter.getBase());

            for(GroundFighter fighter : Fighters) {
                if (fighter.isPlayer || fighter.getBase().Type != NPC.NPCTypes.Minion)
                    continue;

                fighter.setPlayerCount(PlayerNames.size());

            }
            groundFighter.setAlly(true);

        }
        else {
            int skillID = Skill.Skills.cool.ordinal();
            if (isFoesSurprised)
                skillID = Skill.Skills.vigilance.ordinal();
            groundFighter.setInitiativeRoll(groundFighter.GetSkillRollResult(skillID));
        }



        int iCount = 1;
        for (GroundFighter anotherFighter : Fighters) {

            if (anotherFighter.Name.equals(groundFighter.Name)) {
                anotherFighter.setColor(iCount);
                iCount++;
            }
        }

        if (iCount != 1)
            groundFighter.setColor(iCount);


        Fighters.add(groundFighter);
        ReorderFighters();

        MainAdapter.notifyDataSetChanged();
        initiativeAdapter.updateData(Fighters);


    }
	
	private static void AddFighter(NPC baseNPC) {



        GroundFighter fighter = new GroundFighter(PlayerNames.size());
        fighter.setBase(baseNPC);

        AddFighter(fighter);



	}
	
	
	public static void AddFighter(NPC baseNPC, int Count) {

	   	for (int i = 0; i < Count; i++)
            AddFighter(baseNPC);

	}
	
	
	public static void AddFighter(String NPCName, int Count) {
		
	   	 Database db = new Database(App.getContext());
	   	 NPC base = db.GetNPCbyName(NPCName);

        AddFighter(base, Count);
       
	}
	

	public static void ReorderFighters() {


        Collections.sort(Fighters, new Comparator<GroundFighter>() {
            @Override
            public int compare(GroundFighter lhs, GroundFighter rhs) {
                int compareTo = ((Integer)lhs.getMainInitiative().Triumph).compareTo(rhs.getMainInitiative().Triumph);
                if (compareTo != 0)
                    return -compareTo;
                compareTo = ((Integer)lhs.getMainInitiative().Success).compareTo(rhs.getMainInitiative().Success);
                if (compareTo != 0)
                    return -compareTo;
                compareTo = -((Integer)lhs.getMainInitiative().Advantages).compareTo(rhs.getMainInitiative().Advantages);
                if (compareTo != 0)
                    return -compareTo;

                if (lhs.isPlayer && rhs.isPlayer) return 0;
                if (!lhs.isPlayer && !rhs.isPlayer) return 0;
                if (lhs.isPlayer) return -1;
                return 1;

            }
        });


        MainAdapter.notifyDataSetChanged();
        initiativeAdapter.updateData(Fighters);


	}


	public static void setPlayed(int position, String Action, String Maneuver) {
		
		GroundFighter fighter = Fighters.get(position);

		fighter.setDefensive(false);
		
		if (!fighter.isPlayer) {
			applyManeuvers(position, Maneuver);
			applyActions(position, Action);
		}
		
		fighter.Played = initiativeAdapter.Play(fighter);
		
		

		
		MainAdapter.notifyDataSetChanged();
        FighterListChanged.RunWhenFighterListChanged();
        initiativeAdapter.updateData(Fighters);

	}
	
	private static void applyManeuvers(int Position, String maneuver) {
		GroundFighter fighter = Fighters.get(Position);
		
		if (maneuver.startsWith(App.getContext().getString(R.string.action_equipweapon))) {
			String wpName = maneuver.replace(App.getContext().getString(R.string.action_equipweapon) + " ", "");
			int iCount = 0;
			for (Item itm : fighter.getBase().Items) {
				if (itm.Name.equals(wpName)) {
					fighter.setEquippedWeapon(iCount);
					break;
				}
				iCount++;
			}
			
		}
		else if (maneuver.equals(App.getContext().getString(R.string.action_aim_plusoneboost))) {
			fighter.increaseAimingBonus();
		}
		else if (maneuver.equals(App.getContext().getString(R.string.action_cover_desc))) {
			fighter.setCover(!fighter.isCover);
		}
		else if (maneuver.equals(App.getContext().getString(R.string.action_drop_prone))) {
			fighter.setProne(!fighter.isProne);
		}
		else if (maneuver.equals(App.getContext().getString(R.string.action_defensivestance_desc))) {
			fighter.setDefensive(!fighter.isDefensive);
		}
		
		fighter.setLastManeuver(maneuver);
		
	}
	
	private static void applyActions(int Position, String action) {
		GroundFighter fighter = Fighters.get(Position);

        Context context = actualContext;
		
		if (action.startsWith(context.getString(R.string.action_attack_on))) {
			String targetName = action.replace(context.getString(R.string.action_attack_on), "").trim();

            if (PlayerNames.contains(targetName))
			    GroundFightAttackResolver.Resolve(context, Position, targetName);
            else
                for (GroundFighter f : Fighters)
                    if (f.getFullName().equals(targetName))
                        GroundFightAttackResolver.ResolveVersusNPC(context, Position, Fighters.indexOf(f));



			
		}
		if (action.equals(actualContext.getString(R.string.action_force_power)))
		{
			GroundFightForceResolver.Resolve(actualContext, Position);
			
		}
		
		fighter.setLastAction(action);
		
	}

	public static void NextRound() {
		for (final GroundFighter fighter : Fighters) {
            fighter.Played = -1;
            if (fighter.getCount() == 0) {
                new AlertDialog.Builder(actualContext)
                        .setTitle(R.string.remove_dead_player)
                        .setMessage(actualContext.getString(R.string.remove_dead_player_question, fighter.getFullName()))
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (fighter.isPlayer)
                                    GroundFightScene.Players.add(fighter.getBase());

                                Fighters.remove(fighter);
                                initiativeAdapter.updateData(Fighters);
                                MainAdapter.notifyDataSetChanged();
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

        initiativeAdapter.updateData(Fighters);
        MainAdapter.notifyDataSetChanged();
		
	}
/*    public static void AddFighterRange(List<NPC> EncounterFighters) {
        AddFighterRange(EncounterFighters, false);
    }*/
	public static void AddFighterRange(List<NPC> EncounterFighters, boolean Silently) {
		for (NPC npc : EncounterFighters) {
			android.util.Log.i("hopla", "Ajout de " + npc.Name);
	   		
			AddFighter(npc);
		}


        if (!Silently) {
            MainAdapter.notifyDataSetChanged();
            FighterListChanged.RunWhenFighterListChanged();
        }
	}
	

}
