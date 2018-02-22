package com.example.team22cs407.groceryassistant;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;


/**
 * Created by Sam on 2/20/18.
 */

public class DeleteDialogFragment extends DialogFragment {

    private ModificationDialogFragment.ModificationDialogListener mListener;
    private View fragmentView;

    @Override
    public Dialog onCreateDialog(Bundle bundle) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_delete, null);

        // get arguments for item_name, expiration date
        String expiration_date = "mm/dd/yyyy";

        Bundle args = getArguments();

        final int position = args.getInt("position", 0);
        final String item_name = args.getString("item_name", "");


        // Inflate and set the layout for the dialog
        builder.setView(view)
                .setTitle("Delete")
                // Add action buttons
                .setMessage("Are you sure you want to delete " + item_name + "?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                       // mListener.onDialogPositiveClick(view, position);
                        MainActivity.db.delete(item_name);
                        
                        // reload the current fragment
                        Fragment fragment = getFragmentManager().findFragmentById(R.id.frame_layout);
                        getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                       mListener.onDialogNegativeClick(view, position);
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public interface ModificationDialogListener {
        public void onDialogPositiveClick(View view, int position);
        public void onDialogNegativeClick(View view, int position);
    }


    // Override the Fragment.onAttach() method to instantiate the ModificationDialogListener
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

    public View getFragmentView() {
        return  fragmentView;
    }
    public void setFragmentView(View view){
        fragmentView = view;
    }
}

