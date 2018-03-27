package com.example.team22cs407.groceryassistant;

import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.EditText;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyGrocery.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyGrocery#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyGrocery extends Fragment implements NotificationSettingDialogFragment.NotificationSettingDialogListener{


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

        ImageButton imageButton = view.findViewById(R.id.notification_setting_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showNotificationSettingDialog();
            }
        });


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listRecyclerView);

        ListAdapter listAdapter = new ListAdapter();
        recyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

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
                        List<Food> foods = MainActivity.db.getDatas();
                        String name = editText1.getText().toString();
                        String date = editText2.getText().toString();
                        boolean valid = true;
                        for (int i = 0; i < foods.size(); i++) {
                            if (foods.get(i).getFoodItem().equals(name)) {
                                if (foods.get(i).getExpirationDate() == null && date.isEmpty()
                                        ||
                                        foods.get(i).getExpirationDate() != null && foods.get(i).getExpirationDate().equals(date)) {
                                    valid = false;
                                    //duplicate
                                    showDBFailMessage(2);
                                    break;
                                }
                            }
                        }
                        if (valid) {
                            long row = MainActivity.db.insertData(editText1.getText().toString(), editText2.getText().toString());
                            if (row < 0) {
                                if (date != null && !date.isEmpty()) {
                                    //no name
                                    showDBFailMessage(1);
                                }else {
                                    //empty row
                                    showDBFailMessage(0);
                                }
                            } else {

                                //Log.d("DATA", MainActivity.db.getData());
                                ListAdapter.foods = HelperTool.sortByExpiration(MainActivity.db.getDatas());
                                // reload the current fragment
                                Fragment fragment = getFragmentManager().findFragmentById(R.id.frame_layout);
                                getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                            }
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

    public void showDBFailMessage(int type){
        LayoutInflater layoutInflater = LayoutInflater.from(MyGrocery.this.getActivity());
        View promptView;
        if (type == 0) {
            //empty
             promptView = layoutInflater.inflate(R.layout.empty_input_alert, null);
        } else if (type == 1) {
            //no name
            promptView = layoutInflater.inflate(R.layout.invalid_input_alert, null);
        } else {
            //duplicate
            promptView = layoutInflater.inflate(R.layout.duplicate_input_alert, null);
        }
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MyGrocery.this.getActivity());
        alertDialogBuilder.setView(promptView);
        alertDialogBuilder
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    public void showNotificationSettingDialog(){
        NotificationSettingDialogFragment dialog = new NotificationSettingDialogFragment();
        FragmentManager fragmentManager = MyGrocery.this.getActivity().getFragmentManager();
        dialog.setTargetFragment(this, 0);
        dialog.show(fragmentManager, "NotificationSettingDialogFragment");
    }

    @Override
    public void cancelCurrentTimerTask() {
        System.out.println("I am in my Grocery");
    }

    @Override
    public void updateCurrentTimerTask(int hourOfDay, int minute) {

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
