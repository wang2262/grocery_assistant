package com.example.team22cs407.groceryassistant;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.*;

import java.util.ArrayList;
import java.util.List;


public class RecipeDetailFragment extends Fragment {

    public JSONObject details;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    public static RecipeDetailFragment newInstance(String url) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putString("webUrl", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("inside webcreate function");
        View v = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        WebView wView = (WebView)v.findViewById(R.id.web_view);
        wView.getSettings().setJavaScriptEnabled(true);
        wView.setWebViewClient(new WebViewClient());
        System.out.println("args:"+ getArguments());
        wView.loadUrl(getArguments().getString("webView"));
        Button backButton = v.findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getFragmentManager();
                for (int entry = 0; entry < fm.getBackStackEntryCount(); entry++) {
                    System.out.println("Fragment: " + fm.getBackStackEntryAt(entry).getName());
                }
                getFragmentManager().popBackStack("detailFrag", FragmentManager.POP_BACK_STACK_INCLUSIVE);
                //getFragmentManager().popBackStack("detailFrag", POP_BACK_STACK_INCLUSIVE);
            }
        });
        try {
            details = new JSONObject(getArguments().getString("AllInfo"));
            Button importButton = v.findViewById(R.id.import_button);
            importButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    parseFood(details);
                    AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();
                    alertDialog.setTitle("Import Success");
                    alertDialog.setMessage("The ingredients were successfully imported to the Shopping List.");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return v;
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
