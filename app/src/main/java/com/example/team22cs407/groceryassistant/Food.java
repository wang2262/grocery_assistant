package com.example.team22cs407.groceryassistant;

/**
 * Created by SignInSon on 2/19/18.
 */

public class Food {
    private int id;
    private String foodItem;
    private String expirationDate;
    private boolean checkBox;

    public Food() {
        super();
    }

    public Food(int id, String foodItem, String expirationDate, boolean checkBox) {
        this.id = id;
        this.foodItem = foodItem;
        this.expirationDate = expirationDate;
        this.checkBox = checkBox;
    }
    public int getId() {
        return id;
    }
    public String getFoodItem() {
        return foodItem;
    }
    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }
    public boolean getCheckBox() { return checkBox;}
    public void setCheckBox(boolean checkBox) {
        this.checkBox = checkBox;
    }
    public void setId(int uid) {this.id = uid;}

    public String getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

}
