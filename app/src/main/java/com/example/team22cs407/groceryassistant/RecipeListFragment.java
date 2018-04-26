package com.example.team22cs407.groceryassistant;

import android.app.Fragment;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by yuanyuanji on 4/22/18.
 */

public class RecipeListFragment extends Fragment implements SpoonacularAPI.OnRecipeDetailsInterface{

    List<RecipeInfo> recipeInfos;

    public RecipeListFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipe_list, container, false);
        ImageButton back = view.findViewById(R.id.back_to_ingredients);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getFragmentManager().popBackStack();
            }
        });

        Bundle args = getArguments();
        RecipeInfo[] recipes = (RecipeInfo[]) args.getSerializable("recipe_info_array");
        if (recipes != null) {
            recipeInfos = Arrays.asList(recipes);
            /* testing purpose
            for (RecipeInfo recipeInfo : recipeInfos) {
                System.out.println("id : " + recipeInfo.getId());
            }
            */
            ListView listView = view.findViewById(R.id.recipe_info_list);
            ListAdapterRecipeInfo recipeList = new ListAdapterRecipeInfo(getContext(), recipeInfos);
            listView.setAdapter(recipeList);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    RecipeInfo item = recipeInfos.get(i);
                    System.out.println("I am clicking " + item.getTitle());
                    // get Recipe details for this recipe
                    getRecipeDetails(item.getId());

                }
            });
        }

        return view;
    }
    // TODO : back button back to select ingredients page
    // TODO: if the recipe-info  page wait a little bit long time, it will crash. problem with serialible

    public void getRecipeDetails(int recipeId) {
        SpoonacularAPI spoon = new SpoonacularAPI(this);
        try {
            spoon.getRecipeInfo(String.valueOf(recipeId));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRecipeDetailsReturned(JSONObject recipeDetail) {
        if (recipeDetail != null) {
            try {
                String detailUrl = recipeDetail.getString("spoonacularSourceUrl");
                //TODO: HERE invoking the fragment of recipe detail and pass the url to load web page inside of app.
                System.out.println(recipeDetail);
                parseFood(recipeDetail);



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void parseFood(JSONObject recipeDetail) {
        try {
            //String foodItems = recipeDetail.getString("extendedIngredients");
            List<String> list = new ArrayList<String>();
            List<String> toAddList = new ArrayList<String>();
            JSONArray array = recipeDetail.getJSONArray("extendedIngredients");
            List<Food> shoppingFoods = MainActivity.db.getDatasWithTable("ShoppingList");
            List<Food> groceryFoods = MainActivity.db.getDatasWithTable("GroceryList");
            for(int i = 0 ; i < array.length() ; i++){
                list.add(array.getJSONObject(i).getString("name"));
            }
            for(int j = 0; j < list.size(); j++) {
                boolean addToList = true;
                for(int gf = 0; gf < groceryFoods.size(); gf++) {
                    if(groceryFoods.get(gf).getFoodItem().toLowerCase().equals(list.get(j).toLowerCase())) {
                        addToList = false;
                    }
                }
                for(int sf = 0; sf < shoppingFoods.size(); sf++) {
                    if(shoppingFoods.get(sf).getFoodItem().toLowerCase().equals(list.get(j).toLowerCase())) {
                        addToList = false;
                    }
                }
                if(addToList) {
                    toAddList.add(list.get(j));
                    System.out.println("Item name: " + list.get(j) + "\n");
                }
            }
            for(int add = 0; add < toAddList.size(); add++) {
                MainActivity.db.insertDatas(toAddList.get(add), "", "ShoppingList");
            }
            ListAdapterShopping.foods = HelperTool.sortByExpiration(MainActivity.db.getDatasWithTable("ShoppingList"));
            ListAdapterImport.shoppingFoods = MainActivity.db.getImportDatas();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
