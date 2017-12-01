package team2.grocerycartcalculator;

import team2.grocerycartcalculator.db.Food;

/**
 * Created by Charles on 11/28/2017.
 */

public class List_Item {
    // [Food name][Quantity of Item][Dropdown list of units][Price]
    String name;
    double quantity;
    String units;
    double price;
    double totalPrice;
    int listID;
    Food food;

    List_Item(Food food, double quantity, int groceryListID){
        this.food = food;
        name = food.getName();
        this.quantity = quantity;
        price = food.getPrice();
        totalPrice = quantity * price;
        units = "";
        listID = groceryListID;
    }

    String getName(){
        return name;
    }

    String getQuantity() {
        return Double.toString(quantity);
    }

    String getPrice() {
        double priceFloat = price/100;
        String priceString = Double.toString(priceFloat);
        return priceString;
    }

    public int getListID() {
        return listID;
    }

    public Food getFood() {
        return food;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }
}
