package com.example.team22cs407.groceryassistant;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyGrocery.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyGrocery#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyGrocery extends Fragment {

    //private OnFragmentInteractionListener mListener;
    //MainActivity main = (MainActivity)getActivity();
    MainActivity.dataHelp dataHelp = new MainActivity.dataHelp(getContext());
    public MyGrocery() {
        // Required empty public constructor
    }

    public static MyGrocery newInstance() {
        MyGrocery fragment = new MyGrocery();
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
        return inflater.inflate(R.layout.fragment_my_grocery, container, false);
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
