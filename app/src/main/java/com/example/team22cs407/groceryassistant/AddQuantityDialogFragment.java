package com.example.team22cs407.groceryassistant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by KateW on 4/23/2018.
 */

public class AddQuantityDialogFragment extends DialogFragment {
    private ModificationDialogFragment.ModificationDialogListener mListener;
    private View fragmentView;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        //, AlertDialog.THEME_HOLO_LIGHT);

        // Get the layout inflater
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View promptView = layoutInflater.inflate(R.layout.add_quantity_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);
        final EditText editText1 = promptView
                .findViewById(R.id.quantity);
        // get arguments for item_name
        Bundle args = getArguments();

        //final int position = args.getInt("position", 0);
        final String item_name = args.getString("item_name", "");

        // Inflate and set the layout for the dialog
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String quantity = editText1.getText().toString();
                        Log.d("QUAN", quantity);
                        MainActivity.db.addQuantity(item_name, quantity);
                        ListAdapterShopping.foods = MainActivity.db.getDatasWithTable("ShoppingList");
                        Fragment fragment = getFragmentManager().findFragmentById(R.id.frame_layout);
                        getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                    }
                })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        return alert;
    }


    // Override the Fragment.onAttach() method to instantiate the ModificationDialogListener
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

            mListener = (ModificationDialogFragment.ModificationDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement ModificationDialogListener");
        }
    }
*/
}
