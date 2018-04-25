package com.example.team22cs407.groceryassistant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by yuanyuanji on 4/22/18.
 */

public class RecipeInfo implements Serializable {
    private int id;
    private String title;
    private String image; // image url
    private String imageType;
    private int usedIngredientCount;
    private int missedIngredientCount;
    private int likes;

    public RecipeInfo(int id, String title, String image) {
        this.id = id;
        this.title = title;
        this.image = image;
    }

    public RecipeInfo(JSONObject recipeInfo) {
        try {
            id = recipeInfo.getInt("id");
            title = recipeInfo.getString("title");
            image = recipeInfo.getString("image");
            imageType = recipeInfo.getString("imageType");
            usedIngredientCount = recipeInfo.getInt("usedIngredientCount");
            missedIngredientCount  = recipeInfo.getInt("missedIngredientCount");
            likes = recipeInfo.getInt("likes");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
