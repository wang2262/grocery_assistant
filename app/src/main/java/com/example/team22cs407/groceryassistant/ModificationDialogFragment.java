package com.example.team22cs407.groceryassistant;

//import android.app.AlertDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by yuanyuanji on 2/11/18.
 */

public class ModificationDialogFragment extends DialogFragment {
    // Use this instance of the interface to deliver action events
    private ModificationDialogListener mListener;
    private View fragmentView;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        /*
        builder.setMessage(R.string.modification_dialog_message)
                .setPositiveButton(R.string.modification_dialog_save, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // FIRE ZE MISSILES!
                    }
                })
                .setNegativeButton(R.string.modification_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
          */
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.dialog_modification, null);

        // get arguments for item_name, expiration date
        String item_name = "";
        String expiration_date = "mm/dd/yyyy";
        Bundle args = getArguments();
        if (args != null) {
           item_name = args.getString("item_name", "");
           expiration_date = args.getString("expiration_date", "mm/dd/yyyy");
        }
        EditText nameView = view.findViewById(R.id.item_name);
        nameView.setHint(item_name);
        EditText expirationView = view.findViewById(R.id.item_expiration);
        expirationView.setHint(expiration_date);

        // Inflate and set the layout for the dialog
        builder.setView(view)
                .setTitle("Modification")
                // Add action buttons
                .setPositiveButton(R.string.modification_dialog_save, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Send the positive button event back to the host activity
                        mListener.onDialogPositiveClick(view);
                    }
                })
                .setNegativeButton(R.string.modification_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(view);
                    }
                });
        /*
        to do :
        phase 1:
        1. pass text view result back to activity. (Done)
        2. show item original name and expiration date on EditText. (Done)
        3. make text view have title on the left (Done)
        4. have an icon on the top and buttons to make it pretty.
        phase 2:
        1. save data to database.
         */
        // Create the AlertDialog object and return it
        return builder.create();
    }

    public interface ModificationDialogListener {
        public void onDialogPositiveClick(View view);
        public void onDialogNegativeClick(View view);
    }


    // Override the Fragment.onAttach() method to instantiate the ModificationDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {

            mListener = (ModificationDialogListener) context;
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
