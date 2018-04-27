package com.example.team22cs407.groceryassistant;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
    @NonNull
    public void onRecipeDetailsReturned(JSONObject recipeDetail) {
        if (recipeDetail != null) {
            try {
                String detailUrl = recipeDetail.getString("spoonacularSourceUrl");
                System.out.println(recipeDetail);
        		System.out.println("RecipeListURL:" + detailUrl);
        		RecipeDetailFragment detailFrag = new RecipeDetailFragment();
        		Bundle bundle = new Bundle();
        		bundle.putString("webView", detailUrl);
        		bundle.putString("AllInfo", recipeDetail.toString());
        		detailFrag.setArguments(bundle);
        		FragmentManager manager = getFragmentManager();
        		FragmentTransaction transaction = manager.beginTransaction();
        		transaction.replace(R.id.recipe_fragment_container, detailFrag);
        		transaction.addToBackStack("detailFrag");
        		transaction.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
