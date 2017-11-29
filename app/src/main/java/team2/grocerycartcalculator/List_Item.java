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

    void List_Item(Food food, int quantity){
        name = food.getName();
        this.quantity = quantity;
        price = food.getPrice();
        units = "";
    }

    String getName(){
        return name;
    }

    int getQuantity() {
        return quantity;
    }

    float getPrice() {
        return price;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
