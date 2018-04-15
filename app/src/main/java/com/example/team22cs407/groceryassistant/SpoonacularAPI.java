package com.example.team22cs407.groceryassistant;

/**
 * Created by yuanyuanji on 4/8/18.
 */

import android.os.AsyncTask;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class SpoonacularAPI {
    private final static String X_Mashape_Key = "U19lNQuw25msh8MhwobUmfwgYEr9p1zhfcGjsn1AiU3QYpMzcQ";
    private final static String X_Mashape_Host = "spoonacular-recipe-food-nutrition-v1.p.mashape.com";
    public static String baseUrl = "https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/";

    public JSONObject clickedRecipe;

    public SpoonacularAPI() {
        this.clickedRecipe = new JSONObject();
    }

    public void getRes(String recipeId) throws Exception {

        new GetRecipeInfo().execute(recipeId);
    }

    public  void getRecipeByIngredients(String... ingredients) throws Exception {
        new SearchRecipeByIngredients().execute(ingredients);
    }


    class SearchRecipeByIngredients extends AsyncTask<String, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... ingredients) {
            try {
                // default number is 5 in spoonaular API
                //URL url = new URL("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/findByIngredients?ingredients=potato&number=3");
                String startUrl = baseUrl + "findByIngredients?";
                String finalUrl;
                finalUrl = addQueryParams(startUrl, "ingredients", ingredients);
                finalUrl += '&';
                finalUrl = addQueryParams(finalUrl, "number", "3");
                System.out.println("urlStr:" + finalUrl);

                
                URL url = new URL(finalUrl);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("X-Mashape-Key", X_Mashape_Key);
                con.setRequestProperty("X-Mashape-Host", X_Mashape_Host);
                //con.setConnectTimeout(5000);
                //con.disconnect();
                /*
                StringBuilder queryStr = new StringBuilder("ingredients=" + "potato&" + "number=" + "3");
                con.setDoOutput(true);
                DataOutputStream out = new DataOutputStream(con.getOutputStream());
                out.writeBytes(queryStr.toString());
                out.flush();
                out.close();
                */

                // int responseCode = con.getResponseCode();
                // System.out.println("Response Code : " + responseCode);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                    response.append('\n');
                }
                in.close();

                //clickedRecipe = new JSONObject(response.toString());
                //print result
                System.out.println(response.toString());
                //System.out.println(clickedRecipe.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }


    }

    public String addQueryParams(String startUrl, String name, String... values) {
        if (values.length < 0) {
            return startUrl;
        }
        StringBuilder res = new StringBuilder(startUrl + name + "=");
        if (values.length  > 1) {
            for(int i = 0 ; i < values.length; i++) {
                res.append(values[i]);
                if (i != values.length -1) {
                    res.append("%2C+");
                }
            }
            return res.toString();

        } else {

            res.append(values[0]);
            return res.toString();
        }
    }

    // calling get recipe information function on API
    class GetRecipeInfo extends AsyncTask<String, Void, Void> {


        protected void onPreExecute() {
            //display progress dialog.

        }
        /*
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
        */
        @Override
        protected Void doInBackground(String... recipeIds) {

                  /*

        HttpResponse<JsonNode> response = Unirest.get("https://spoonacular-recipe-food-nutrition-v1.p.mashape.com/recipes/986374/information")
                .header("X-Mashape-Key", "U19lNQuw25msh8MhwobUmfwgYEr9p1zhfcGjsn1AiU3QYpMzcQ")
                .header("X-Mashape-Host", "spoonacular-recipe-food-nutrition-v1.p.mashape.com")
                .asJson();
        JsonNode res = response.getBody();

        System.out.println(res.toString());
        */
            try {
                URL url = new URL(baseUrl + recipeIds[0] + "/information");
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setDoInput(true);
                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("X-Mashape-Key", X_Mashape_Key);
                con.setRequestProperty("X-Mashape-Host",  X_Mashape_Host);
                //con.setConnectTimeout(5000);
                //con.disconnect();

               // int responseCode = con.getResponseCode();
               // System.out.println("Response Code : " + responseCode);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                    response.append('\n');
                }
                in.close();

                clickedRecipe = new JSONObject(response.toString());
                //print result
                //System.out.println(response.toString());
                System.out.println(clickedRecipe.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        protected void onPostExecute(Void result) {
            // dismiss progress dialog and update ui
        }
    }


    //public static JSONObject


}
