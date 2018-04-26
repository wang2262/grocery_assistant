package com.example.team22cs407.groceryassistant;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by yuanyuanji on 3/21/18.
 */

public class MultiDeleteFragment extends DialogFragment {

    //private NotificationSettingDialogListener notificationSettingDialogListener;


    // this listener only for positive click
    /*
    public interface NotificationSettingDialogListener{
         void cancelCurrentTimerTask();

         // create a new timer task with arguments and replace current one.
         void updateCurrentTimerTask(int hourOfDay, int minute);
    }
    */

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), AlertDialog.THEME_HOLO_LIGHT);
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.import_fragment, null);

        //onAttachToParentFragment(getTargetFragment());
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.slistRecyclerView);

        ListAdapterImport listAdapterImport = new ListAdapterImport();
        recyclerView.setAdapter(listAdapterImport);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        builder.setView(view)
                .setTitle("Grocery Items")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // how to get closeInDays and time to run a new TimerTask
                        boolean flag = true;
                        List<Integer> positions = new ArrayList<>();
                        List<ShoppingFood> listFood = ListAdapterImport.shoppingFoods;
                        List<Food> food = HelperTool.sortByExpiration(MainActivity.db.getDatas());
                        for(int go = 0; go < listFood.size(); go++) {
                            if(listFood.get(go).getCheckBox()) {
                                MainActivity.db.delete(food.get(go).getId());
                                positions.add(go);
                            }
                        }
                        for(int j = positions.size() - 1; j >= 0; j--) {
                            int k = positions.get(j);
                            ListAdapterImport.shoppingFoods.remove(k);
                            ListAdapterImport.foods.remove(k);
                            ListAdapter.foods.remove(k);
                        }
                        ListAdapter.foods = HelperTool.sortByExpiration(MainActivity.db.getDatas());
                        ListAdapterImport.foods = HelperTool.sortByExpiration(MainActivity.db.getDatas());
                        // not sure what this line does
                        //ListAdapterImport.shoppingFoods = MainActivity.db.getImportDatas();
                        // reload the current fragment
                        Fragment fragment = getFragmentManager().findFragmentById(R.id.frame_layout);
                        getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
                        // sending data back to myGrocery
                        //((MainActivity) getActivity()).cancelTimerTask();


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        System.out.println("I am in Negative: " + i);
                    }
                });

        //return view;
        return builder.create();
    }
}
