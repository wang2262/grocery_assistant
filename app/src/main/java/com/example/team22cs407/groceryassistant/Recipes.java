package com.example.team22cs407.groceryassistant;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Recipes.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Recipes#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recipes extends Fragment{

    //private OnFragmentInteractionListener mListener;
    private final int INGRE_LIMIT = 3;
    private int ingredients_count;

    public Recipes() {
        // Required empty public constructor
        ingredients_count = 0;
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
        View view = inflater.inflate(R.layout.fragment_recipes, container, false);
        SelectIngredientsFragment ingredientsFragment =  new SelectIngredientsFragment();
        getFragmentManager().beginTransaction().add(R.id.recipe_fragment_container, ingredientsFragment).commit();
        return view;
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
