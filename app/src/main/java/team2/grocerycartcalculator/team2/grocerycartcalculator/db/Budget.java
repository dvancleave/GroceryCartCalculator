package team2.grocerycartcalculator.team2.grocerycartcalculator.db;

import java.util.Calendar;

/**
 * Holds data for individual monthly budgets
 * Protected fields are for DB use only
 */

public class Budget {

    // Table column names (indicated by '_' suffix)
    protected static String _YEAR = "year";
    protected static String _MONTH = "month";
    protected static String _TOTAL = "total";
    protected static String _SPENT = "spent";

    // Fields
    private int year; // budget is for this year
    private int month; // budget is for this month (1 = Jan., 2 = Feb., etc.)
    private int total; // money (in cents) that has been allocated for this month in total
    private int spent; // money (in cents) that has been spent this month so far

    // Constructors
    public Budget(int year, int month, int total, int spent) {
        this.year = year;
        this.month = month;
        this.total = total;
        this.spent = spent;
    }
    public Budget(Calendar month, int total, int spent) {
        this.year = month.get(Calendar.YEAR);
        this.month = month.get(Calendar.MONTH) + 1;
        this.total = total;
        this.spent = spent;
    }

    // Getters
    public int getYear() {
        return year;
    }
    public int getMonth() {
        return month;
    }
    public int getTotal() {
        return total;
    }
    public int getSpent() {
        return spent;
    }

    // Setters
    public void setYear(int year) {
        this.year = year;
    }
    public void setMonth(int month) {
        this.month = month;
    }
    public void setTotal(int total) {
        this.total = total;
    }
    public void setSpent(int spent) {
        this.spent = spent;
    }

}
