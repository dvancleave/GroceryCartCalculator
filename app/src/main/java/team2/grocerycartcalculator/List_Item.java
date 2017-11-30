package team2.grocerycartcalculator;

import team2.grocerycartcalculator.db.Food;

/**
 * Created by Charles on 11/28/2017.
 */

public class List_Item {
    // [Food name][Quantity of Item][Dropdown list of units][Price]
    String name;
    int quantity;
    String units;
    float price;
    int listID;

    List_Item(Food food, int quantity, int groceryListID){
        name = food.getName();
        this.quantity = quantity;
        price = food.getPrice();
        units = "";
        listID = groceryListID;
    }

    String getName(){
        return name;
    }

    String getQuantity() {
        return Integer.toString(quantity);
    }

    String getPrice() {
        float priceFloat = price/100;
        String priceString = Float.toString(priceFloat);
        return priceString;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
