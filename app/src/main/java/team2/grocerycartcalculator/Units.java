package team2.grocerycartcalculator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Charles on 11/30/2017.
 */

public class Units {

    //SOLIDS
    private static String lbs = "lb.s";
    private static String milligrams = "milligrams";
    private static String grams = "grams";
    private static String kilograms = "kilograms";
    private static ArrayList<String> SolidList = new ArrayList<String>(Arrays.asList(lbs, milligrams, grams, kilograms));
    //LIQUIDS
    private static String liters = "liters";
    private static String quarts = "quarts";
    private static String gallons = "gallons";
    private static ArrayList<String> liquidList = new ArrayList<String>(Arrays.asList(liters, quarts, gallons));

    public static ArrayList<String> getSolidList() {
        return SolidList;
    }

    public static ArrayList<String> getLiquidList() {
        return liquidList;
    }
    public static String getDefaultSolid(){
        return lbs;
    }
    public static String getDefaultLiquid(){
        return liters;
    }
}
