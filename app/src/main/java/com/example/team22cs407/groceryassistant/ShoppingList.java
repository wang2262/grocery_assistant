package com.example.team22cs407.groceryassistant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.DialogInterface;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.EditText;
import java.util.List;
import android.util.Log;
/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShoppingList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShoppingList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingList extends Fragment {

    //private OnFragmentInteractionListener mListener;

    public ShoppingList() {
        // Required empty public constructor
    }

    public static ShoppingList newInstance() {
        ShoppingList fragment = new ShoppingList();
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
        View view = inflater.inflate(R.layout.fragment_shopping_list, container, false);
        Button button = view.findViewById(R.id.add_button);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showInputDialog();

            }
        });

        return view;
    }
    protected void showInputDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(ShoppingList.this.getActivity());
        View promptView = layoutInflater.inflate(R.layout.input_dialog2_shoppinglist, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ShoppingList.this.getActivity());
        alertDialogBuilder.setView(promptView);
        final EditText editText1 = promptView
                .findViewById(R.id.editTextDialogItemNameInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        List<Food> foods = MainActivity.db.getDatas();
                        String name = editText1.getText().toString();
                        boolean valid = true;
                        /*
                        for (int i = 0; i < foods.size(); i++) {
                            //check for duplicate, set valid = false if found
                        }
                        */
                        if (valid) {
                            Log.d("DATA", name);
                            //call db add function
                            //long row = MainActivity.db.insertData(editText1.getText().toString(), editText2.getText().toString());
                            ListAdapter.foods = HelperTool.sortByExpiration(MainActivity.db.getDatas());
                            // reload the current fragment
                            Fragment fragment = getFragmentManager().findFragmentById(R.id.frame_layout);
                            getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                        }
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
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