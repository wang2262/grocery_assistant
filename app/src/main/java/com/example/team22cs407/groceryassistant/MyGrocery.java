package com.example.team22cs407.groceryassistant;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.EditText;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyGrocery.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyGrocery#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyGrocery extends Fragment {
    /*Setting up database to write */
    MainActivity.dataHelp dataHelp = new MainActivity.dataHelp(getContext());
    //SQLiteDatabase db = dataHelp.getWritableDatabase();

    ContentValues values = new ContentValues();
    //values.put()

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
        View view = inflater.inflate(R.layout.fragment_my_grocery, container, false);
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
        //return inflater.inflate(R.layout.fragment_my_grocery, container, false);
    }
    protected void showInputDialog() {
        LayoutInflater layoutInflater = LayoutInflater.from(MyGrocery.this.getActivity());
        View promptView = layoutInflater.inflate(R.layout.input_dialog1, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MyGrocery.this.getActivity());
        alertDialogBuilder.setView(promptView);
        final EditText editText1 = promptView
                .findViewById(R.id.editTextDialogItemNameInput);
        final EditText editText2 = promptView
                .findViewById(R.id.editTextDialogEDateInput);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //Toast.makeText(getActivity().getApplicationContext(),"OK CLICKED",1000).show();
                        Log.d("ITEMNAME", editText1.getText().toString());
                        Log.d("EDATE", editText2.getText().toString());
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
