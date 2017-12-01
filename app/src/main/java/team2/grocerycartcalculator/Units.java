package team2.grocerycartcalculator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Charles on 11/30/2017.
 */
public class Units {

    //SOLIDS
    public static final String pounds = "lbs";
    public static final String ounces = "oz";
    //private static String milligrams = "milligrams";
    public static final String grams = "g";
    public static final String kilograms = "kg";
    private static ArrayList<String> SolidList = new ArrayList<String>(Arrays.asList(pounds, kilograms, grams, ounces));
    //LIQUIDS
    public static final String liters = "L";
    public static final String milliliters = "mL";
    public static final String quarts = "qt";
    public static final String gallons = "gal";
    public static final String pints = "pt";
    public static final String cups = "c";
    public static final String fluidOunces = "fl oz";
    private static ArrayList<String> liquidList = new ArrayList<String>(Arrays.asList(gallons, liters, milliliters, quarts, pints, cups, fluidOunces));

    // Mass conversion rates
    public static double CONV_POUND = 1.0;
    public static double CONV_KILO = 2.205;
    public static double CONV_GRAM = .002205;
    public static double CONV_OUNCE = .0625;

    // Liquid conversion rates
    private static double CONV_GALLON = 1.0;
    private static double CONV_QUART = .25;
    private static double CONV_PINT = .125;
    private static double CONV_CUP = .0625;
    private static double CONV_FL_OUNCE = .003906;
    private static double CONV_LITER = 0.2642;
    private static double CONV_MILLILITER = 0.0002642;

    public static ArrayList<String> getSolidList() {
        return SolidList;
    }

    public static ArrayList<String> getLiquidList() {
        return liquidList;
    }
    public static String getDefaultSolid(){
        return pounds;
    }
    public static String getDefaultLiquid(){
        return liters;
    }
    public static double getUnitConv(String unitString)
    {
        double ret;
        switch(unitString)
        {
            case (Units.pounds):
                ret = CONV_POUND;
                break;
            case (Units.kilograms):
                ret = CONV_KILO;
                break;
            case (Units.grams):
                ret = CONV_GRAM;
                break;
            case (Units.ounces):
                ret = CONV_OUNCE;
                break;
            case (Units.gallons):
                ret = CONV_GALLON;
                break;
            case (Units.quarts):
                ret = CONV_QUART;
                break;
            case (Units.pints):
                ret = CONV_PINT;
                break;
            case (Units.cups):
                ret = CONV_CUP;
                break;
            case (Units.fluidOunces):
                ret = CONV_FL_OUNCE;
                break;
            case (Units.liters):
                ret = CONV_LITER;
                break;
            case (Units.milliliters):
                ret = CONV_MILLILITER;
                break;
            default:
                ret = 1;
        }
        return ret;
    }
}
