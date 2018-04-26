package com.example.team22cs407.groceryassistant;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by yuanyuanji on 4/22/18.
 */

public class SelectIngredientsFragment extends Fragment implements SpoonacularAPI.OnRecipesReturnedInterface {

    public SelectIngredientsFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_ingredients, container, false);
        ListView foodList = view.findViewById(R.id.unexpiredItems);

        // get all the unexpired items on grocery list
        List<String> unexpiredItems = getUnexpiredNames();

        final ListAdapterStringCheckbox listAdapter = new ListAdapterStringCheckbox(getContext(), unexpiredItems);
        foodList.setAdapter(listAdapter);

        Button searchRecipes = view.findViewById(R.id.search_recipes_button);
        searchRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<String> checkedItems = listAdapter.getCheckedItems();
                System.out.println("Printing checked Items");
                for(String str : checkedItems) {
                    System.out.println(str);
                }
                callSpoonForRecipes(checkedItems);

            }
        });

        return view;
    }


    public void callSpoonForRecipes(List<String> checkedItems) {
        // send ingredients to next page
        if (checkedItems != null && checkedItems.size() > 0) {
            try {
                SpoonacularAPI s = new SpoonacularAPI(SelectIngredientsFragment.this);
                //s.getRes("285930");
                //String[] ingredients = {"potato", "tomato"};
                //String[] ingredients = (String[]) checkedItems.toArray();  // It doesn't work.
                String[] ingredients = new String[checkedItems.size()];
                for (int i= 0; i < checkedItems.size(); i++) {
                    ingredients[i] = checkedItems.get(i);
                }
                s.getRecipeByIngredients(ingredients);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public List<String> getUnexpiredNames(){
        List<Food> food = MainActivity.db.getDatas();
        List<String> unexpired = new ArrayList<>();
        Date today = new Date();  //TODO: Think about whether we should include grocery items with expiration date of today. (currently don't).

        for (Food item : food) {
            Date expirationDate = HelperTool.convert2Date(item.getExpirationDate());
            if (expirationDate != null && expirationDate.compareTo(today) > 0) {
                System.out.println("adding in unexpired: " + item.getFoodItem());
                unexpired.add(item.getFoodItem());
            }

        }
        return unexpired;
    }
    @Override
    public void onRecipesReturned(JSONArray recipes) {
        System.out.println("I am in fragments");
        System.out.println(recipes.toString());
        // convert JSONArray recipes to RecipeInFO class array
        if (recipes.length() > 0) {
            RecipeInfo[] recipeInfos = new RecipeInfo[recipes.length()];
            try {
                for (int i = 0; i < recipes.length(); i++) {
                    recipeInfos[i] = new RecipeInfo(recipes.getJSONObject(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // invoke another fragments
            RecipeListFragment recipeListFragment = new RecipeListFragment();
            Bundle bundle = new Bundle();
            bundle.putSerializable("recipe_info_array", recipeInfos);
            recipeListFragment.setArguments(bundle);
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.recipe_fragment_container, recipeListFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }
}
