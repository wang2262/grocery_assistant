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

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by KateW on 4/25/2018.
 */

public class CheckOffDialogFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the layout inflater
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View promptView = layoutInflater.inflate(R.layout.shopping_check_off, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setView(promptView);
        final EditText editText1 = promptView
                .findViewById(R.id.editTextDialogCost);
        final EditText editText2 = promptView
                .findViewById(R.id.editTextDialogEDateInput);
        // get arguments for item_name
        Bundle args = getArguments();

        //final int position = args.getInt("position", 0);
        final String itemName = args.getString("item_name", "");

        // Inflate and set the layout for the dialog
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String c = editText1.getText().toString();
                        String date = editText2.getText().toString();
                            if (!c.isEmpty() && c.matches("\\d+(.\\d+)?")) {
                                String formatted_c = String.format("%.2f", Double.parseDouble(c));
                                double cost = Double.parseDouble(formatted_c);
                                double budget = Double.parseDouble(readBudget());
                                if (budget > 0) {
                                    if (cost > 0) budget -= cost;
                                    if (budget <= 0) {
                                        updateBudget("0.00");
                                        showNotification(3);
                                    } else updateBudget(String.format("%.2f",budget));
                                }
                            }
                            List<Food> foods = MainActivity.db.getDatasWithTable("GroceryList");
                            boolean valid = true;
                            for (int i = 0; i < foods.size(); i++) {
                                if (foods.get(i).getFoodItem().equals(itemName)) {
                                    if (foods.get(i).getExpirationDate() == null && date.isEmpty()
                                            ||
                                            foods.get(i).getExpirationDate() != null && foods.get(i).getExpirationDate().equals(date)) {
                                        valid = false;
                                        showNotification(0);
                                        break;
                                    }
                                }
                            }
                            if (valid) {
                                Log.d("VALID", date);
                                long row = MainActivity.db.insertDatas(itemName, date, "GroceryList");
                                ListAdapter.foods = HelperTool.sortByExpiration(MainActivity.db.getDatas());
                                ListAdapterImport.foods = HelperTool.sortByExpiration(MainActivity.db.getDatas());
                                ListAdapterImport.shoppingFoods = MainActivity.db.getImportDatas();
                                //delete item from shopping list here
                                MainActivity.db.deleteShopping(itemName, "ShoppingList");
                                // reload the current fragment
                                ListAdapterShopping.foods = MainActivity.db.getDatasWithTable("ShoppingList");
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
        return alert;
    }

    public String readBudget(){
        byte[] b = new byte[1];
        String budget = "";
        FileInputStream inputStream;
        try {
            inputStream = getActivity().openFileInput("budgetData");
            StringBuilder sb = new StringBuilder();
            while (inputStream.read(b) != -1) {
                sb.append(new String(b));
                b = new byte[1];
            }
            inputStream.close();
            budget = sb.toString();
        } catch (Exception a) {
            a.printStackTrace();
        }
        return budget;
    }

    public void updateBudget(String newbudget){
        FileOutputStream outputStream;
        try {
            outputStream = getActivity().openFileOutput("budgetData", Context.MODE_PRIVATE);
            outputStream.write(newbudget.getBytes());
            outputStream.close();
        } catch (Exception a) {
            a.printStackTrace();
        }
    }

    public void showNotification(int type) {
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();

        View promptView = layoutInflater.inflate(R.layout.reached_budget, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
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
}
