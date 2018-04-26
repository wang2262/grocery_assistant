package com.example.team22cs407.groceryassistant;

import android.widget.CheckBox;

/**
 * Created by SignInSon on 2/19/18.
 */

public class ShoppingFood {
    private int uid;
    private String foodItem;
    private boolean checkBox;

    public ShoppingFood() {
        super();
    }

    public ShoppingFood(int uid, String foodItem, boolean checkBox) {
        this.uid = uid;
        this.foodItem = foodItem;
        this.checkBox = checkBox;
    }

    public String getFoodItem() {
        return foodItem;
    }
    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }

    public boolean getCheckBox() {
        return checkBox;
    }
    public void setCheckBox(boolean checkBox) {
        this.checkBox = checkBox;
    }

    public int getUid(){return uid;}
    public void setUid(int uid){this.uid = uid;}
}
