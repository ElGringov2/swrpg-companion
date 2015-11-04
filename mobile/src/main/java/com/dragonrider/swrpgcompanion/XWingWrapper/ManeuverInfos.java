package com.dragonrider.swrpgcompanion.XWingWrapper;

import com.dragonrider.swrpgcompanion.R;

import java.util.HashMap;

/**
 * Created by mge637 on 30/10/2015. g
 */
public class ManeuverInfos {




    private static HashMap<String, int[][]> ManeuverMap;


   static {

        ManeuverMap = new HashMap<>();

        ManeuverMap.put("all", new int[][] {{0, 0, 3, 0, 0, 0}, {1,1,1,1,1,3}, {1,1,1,1,1,3}, {1,1,1,1,1,3}, {0,0,1,0,0,3}, {0,0,1,0,0,3}});

        ManeuverMap.put("X-Wing", new int[][] {{0, 0, 0, 0, 0, 0}, {0, 2, 2, 2, 0, 0}, {1, 1, 2, 1, 1, 0}, {1, 1, 1, 1, 1, 0}, {0, 0, 1, 0, 0, 3}});
        ManeuverMap.put("Y-Wing", new int[][] {{0, 0, 0, 0, 0, 0}, {0, 1, 2, 1, 0, 0}, {1, 1, 2, 1, 1, 0}, {3, 1, 1, 1, 3, 0}, {0, 0, 3, 0, 0, 3}});
        ManeuverMap.put("A-Wing", new int[][] {{0, 0, 0, 0, 0, 0}, {1, 0, 0, 0, 1, 0}, {2, 2, 2, 2, 2, 0}, {1, 1, 2, 1, 1, 3}, {0, 0, 2, 0, 0, 0}, {0, 0, 2, 0, 0, 3}});
        ManeuverMap.put("YT-1300", new int[][] {{0, 0, 0, 0, 0, 0}, {1, 2, 2, 2, 1, 0}, {1, 1, 2, 1, 1, 0}, {0, 1, 1, 1, 0, 3}, {0, 0, 1, 0, 0, 3}});
        ManeuverMap.put("TIE Fighter", new int[][] {{0, 0, 0, 0, 0, 0}, {1, 0, 0, 0, 1, 0}, {1, 2, 2, 2, 1, 0}, {1, 1, 2, 1, 1, 3}, {0, 0, 1, 0, 0, 3}, {0, 0, 1, 0, 0, 0}});
        ManeuverMap.put("TIE Advanced", new int[][] {{0, 0, 0, 0, 0, 0}, {0, 2, 0, 2, 0, 0}, {1, 1, 2, 1, 1, 0}, {1, 1, 2, 1, 1, 0}, {0, 0, 1, 0, 0, 3}, {0, 0, 1, 0, 0, 0}});
        ManeuverMap.put("TIE Interceptor", new int[][] {{0, 0, 0, 0, 0, 0}, {1, 0, 0, 0, 1, 0}, {2, 2, 2, 2, 2, 0}, {1, 1, 2, 1, 1, 3}, {0, 0, 2, 0, 0, 0}, {0, 0, 1, 0, 0, 3}});
        ManeuverMap.put("Firespray-31", new int[][] {{0, 0, 0, 0, 0, 0}, {0, 2, 2, 2, 0, 0}, {1, 1, 2, 1, 1, 0}, {1, 1, 1, 1, 1, 3}, {0, 0, 1, 0, 0, 3}});
        ManeuverMap.put("HWK-290", new int[][] {{0, 0, 0, 0, 0}, {0, 2, 2, 2, 0}, {1, 1, 2, 1, 1}, {0, 3, 1, 3, 0}, {0, 0, 3, 0, 0}});
        ManeuverMap.put("Lambda-Class Shuttle", new int[][] {{0, 0, 3, 0, 0}, {0, 2, 2, 2, 0}, {3, 1, 2, 1, 3}, {0, 3, 1, 3, 0}});
        ManeuverMap.put("B-Wing", new int[][] {{0, 0, 0, 0, 0, 0}, {3, 2, 2, 2, 3, 0}, {1, 1, 2, 1, 1, 3}, {0, 3, 1, 3, 0, 0}, {0, 0, 3, 0, 0, 0}});
        ManeuverMap.put("TIE Bomber", new int[][] {{0, 0, 0, 0, 0, 0}, {0, 1, 2, 1, 0, 0}, {3, 2, 2, 2, 3, 0}, {1, 1, 2, 1, 1, 0}, {0, 0, 1, 0, 0, 0}, {0, 0, 0, 0, 0, 3}});
        ManeuverMap.put("GR-75 Medium Transport", new int[][] {{0, 0, 0, 0, 0, 0}, {0, 1, 1, 1, 0, 0}, {0, 1, 1, 1, 0, 0}, {0, 0, 1, 0, 0, 0}, {0, 0, 1, 0, 0, 0}});
        ManeuverMap.put("Z-95 Headhunter", new int[][] {{0, 0, 0, 0, 0, 0}, {0, 1, 2, 1, 0, 0}, {1, 2, 2, 2, 1, 0}, {1, 1, 1, 1, 1, 3}, {0, 0, 1, 0, 0, 0}});
        ManeuverMap.put("TIE Defender", new int[][] {{0, 0, 0, 0, 0, 0}, {3, 1, 0, 1, 3, 0}, {3, 1, 2, 1, 3, 0}, {1, 1, 2, 1, 1, 0}, {0, 0, 2, 0, 0, 1}, {0, 0, 2, 0, 0, 0}});
        ManeuverMap.put("E-Wing", new int[][] {{0, 0, 0, 0, 0, 0}, {0, 1, 2, 1, 0, 0}, {1, 2, 2, 2, 1, 0}, {1, 1, 2, 1, 1, 3}, {0, 0, 1, 0, 0, 3}, {0, 0, 1, 0, 0, 0}});
        ManeuverMap.put("TIE Phantom", new int[][] {{0, 0, 0, 0, 0, 0}, {1, 0, 0, 0, 1, 0}, {1, 2, 2, 2, 1, 0}, {1, 1, 2, 1, 1, 3}, {0, 0, 1, 0, 0, 3}});
        ManeuverMap.put("CR90 Corvette", new int[][] {{0, 0, 0, 0, 0, 0}, {0, 1, 0, 1, 0, 0}, {0, 1, 1, 1, 0, 0}, {0, 0, 1, 0, 0, 0}, {0, 0, 1, 0, 0, 0}});
        ManeuverMap.put("YT-2400", new int[][] {{0, 0, 0, 0, 0, 0}, {1, 2, 2, 2, 1, 0}, {1, 1, 2, 1, 1, 0}, {1, 1, 1, 1, 1, 0}, {0, 0, 1, 0, 0, 3}});
        ManeuverMap.put("VT-49 Decimator", new int[][] {{0, 0, 0, 0, 0, 0}, {0, 1, 1, 1, 0, 0}, {1, 2, 2, 2, 1, 0}, {1, 1, 2, 1, 1, 0}, {0, 0, 1, 0, 0, 0}});
        ManeuverMap.put("StarViper", new int[][] {{0, 0, 0, 0, 0, 0, 0, 0}, {1, 2, 2, 2, 1, 0, 0, 0}, {1, 1, 2, 1, 1, 0, 0, 0}, {0, 1, 2, 1, 0, 0, 3, 3}, {0, 0, 1, 0, 0, 0, 0, 0}});
        ManeuverMap.put("M3-A Interceptor", new int[][] {{0, 0, 0, 0, 0, 0}, {1, 2, 0, 2, 1, 0}, {1, 2, 2, 2, 1, 0}, {0, 1, 2, 1, 0, 3}, {0, 0, 1, 0, 0, 0}, {0, 0, 0, 0, 0, 3}});
        ManeuverMap.put("Aggressor", new int[][] {{0, 0, 0, 0, 0, 0, 0, 0}, {1, 2, 2, 2, 1, 0, 0, 0}, {1, 2, 2, 2, 1, 0, 0, 0}, {0, 2, 2, 2, 0, 0, 3, 3}, {0, 0, 0, 0, 0, 3, 0, 0}});
        ManeuverMap.put("Raider-class Corvette", new int[][] {{0, 0, 0, 0, 0, 0}, {0, 1, 1, 1, 0, 0}, {0, 1, 1, 1, 0, 0}, {0, 0, 1, 0, 0, 0}, {0, 0, 1, 0, 0, 0}});
        ManeuverMap.put("YV-666", new int[][] {{0, 0, 3, 0, 0, 0}, {0, 2, 2, 2, 0, 0}, {3, 1, 2, 1, 3, 0}, {1, 1, 2, 1, 1, 0}, {0, 0, 1, 0, 0, 0}});
        ManeuverMap.put("Kihraxz Fighter", new int[][] {{0, 0, 0, 0, 0, 0}, {1, 2, 0, 2, 1, 0}, {1, 2, 2, 2, 1, 0}, {0, 1, 1, 1, 0, 0}, {0, 0, 1, 0, 0, 3}, {0, 0, 0, 0, 0, 3}});
        ManeuverMap.put("K-Wing", new int[][] {{0, 0, 0, 0, 0, 0}, {0, 2, 2, 2, 0, 0}, {1, 1, 2, 1, 1, 0}, {0, 1, 1, 1, 0, 0}});
        ManeuverMap.put("TIE Punisher", new int[][] {{0, 0, 0, 0, 0, 0}, {0, 2, 2, 2, 0, 0}, {3, 1, 2, 1, 3, 0}, {1, 1, 1, 1, 1, 0}, {0, 0, 0, 0, 0, 3}});
        ManeuverMap.put("T-70 X-Wing", new int[][] {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, {0, 2, 2, 2, 0, 0, 0, 0, 0, 0}, {1, 1, 2, 1, 1, 0, 0, 0, 0, 0}, {1, 1, 2, 1, 1, 0, 0, 0, 3, 3}, {0, 0, 1, 0, 0, 3, 0, 0, 0, 0}});
        ManeuverMap.put("TIE/fo Fighter", new int[][] {{0, 0, 0, 0, 0, 0, 0, 0}, {1, 0, 0, 0, 1, 0, 0, 0}, {2, 2, 2, 2, 2, 0, 3, 3}, {1, 1, 2, 1, 1, 0, 0, 0}, {0, 0, 1, 0, 0, 3, 0, 0}, {0, 0, 1, 0, 0, 0, 0, 0}});

    }

    public static int[][] getManeuvers(String name) {

        if (ManeuverMap.containsKey(name))
            return ManeuverMap.get(name);
        else
            return ManeuverMap.get("all");


    }



    public static int GetManeuverRessource(int Maneuver, int difficulty) {

        if (Maneuver == 0 && difficulty == 0)
            return R.drawable.man_turnleft0;
        if (Maneuver == 0 && difficulty == 1)
            return R.drawable.man_turnleft1;
        if (Maneuver == 0 && difficulty == 2)
            return R.drawable.man_turnleft2;

        if (Maneuver == 1 && difficulty == 0)
            return R.drawable.man_bankleft0;
        if (Maneuver == 1 && difficulty == 1)
            return R.drawable.man_bankleft1;
        if (Maneuver == 1 && difficulty == 2)
            return R.drawable.man_bankleft2;

        if (Maneuver == 2 && difficulty == 0)
            return R.drawable.man_straight0;
        if (Maneuver == 2 && difficulty == 1)
            return R.drawable.man_straight1;
        if (Maneuver == 2 && difficulty == 2)
            return R.drawable.man_straight2;

        if (Maneuver == 3 && difficulty == 0)
            return R.drawable.man_bankright0;
        if (Maneuver == 3 && difficulty == 1)
            return R.drawable.man_bankright1;
        if (Maneuver == 3 && difficulty == 2)
            return R.drawable.man_bankright2;

        if (Maneuver == 4 && difficulty == 0)
            return R.drawable.man_turnright0;
        if (Maneuver == 4 && difficulty == 1)
            return R.drawable.man_turnright1;
        if (Maneuver == 4 && difficulty == 2)
            return R.drawable.man_turnright2;

        if (Maneuver == 5)
            return R.drawable.man_koiogram2;


        return 0;
    }



}
