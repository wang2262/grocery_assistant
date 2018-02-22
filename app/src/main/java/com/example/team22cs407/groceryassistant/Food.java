package com.example.team22cs407.groceryassistant;

/**
 * Created by SignInSon on 2/19/18.
 */

public class Food {
    private String foodItem;
    private String expirationDate;

    public Food() {
        super();
    }

    public Food(String foodItem, String expirationDate) {
        this.foodItem = foodItem;
        this.expirationDate = expirationDate;
    }

    public String getFoodItem() {
        return foodItem;
    }
    public void setFoodItem(String foodItem) {
        this.foodItem = foodItem;
    }

    public String getExpirationDate() {
        return expirationDate;
    }
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

}
