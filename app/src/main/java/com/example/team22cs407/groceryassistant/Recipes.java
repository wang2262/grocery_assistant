package com.example.team22cs407.groceryassistant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Recipes.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Recipes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recipes extends Fragment {

    //private OnFragmentInteractionListener mListener;

    public Recipes() {
        // Required empty public constructor
    }


    public static Recipes newInstance() {
        Recipes fragment = new Recipes();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        try {

            SpoonacularAPI s =  new SpoonacularAPI();
            //s.getRes("285930");
            String[] ingredients = {"potato", "tomato"};
            s.getRecipeByIngredients(ingredients);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recipes, container, false);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
