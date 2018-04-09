package com.example.team22cs407.groceryassistant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.DialogInterface;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.EditText;
import java.util.List;
import java.util.Objects;

import android.util.Log;

import android.widget.ImageButton;
import android.app.FragmentManager;


import android.support.annotation.NonNull;

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

        Button imageButton = view.findViewById(R.id.import_button);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImportFragment();
            }
        });

        String budget = "0.00";
        //budget = (retrive from db);
        Button budgetButton = view.findViewById(R.id.budget_button);
        budgetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showBudgetDialog();
            }
        });
        //budget = (retrive from db);
        budgetButton.setText("Budget: " + budget);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listRecyclerView);

        ListAdapterShopping listAdapter = new ListAdapterShopping();
        recyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

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
                        String name = editText1.getText().toString();
                        List<Food> foods = MainActivity.db.getDatasWithTable("ShoppingList");
                        boolean valid = true;
                        if (name != null) {
                            for (int i = 0; i < foods.size(); i++) {
                                if (foods.get(i).getFoodItem().equals(name)) {
                                    valid = false;
                                    showInsertFail(0);
                                    break;
                                }
                            }
                        }

                        if (valid) {
                            long row = MainActivity.db.insertDatas(name, "", "ShoppingList");
                            if (row < 0) {
                                showInsertFail(1);
                            }else {
                                // reload the current fragment
                                ListAdapterShopping.foods = HelperTool.sortByExpiration(MainActivity.db.getDatasWithTable("ShoppingList"));
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

    protected void showCheckoffDialog(@NonNull final String itemName) {
        Objects.requireNonNull(itemName);
        if (itemName.isEmpty()) return;
        LayoutInflater layoutInflater = LayoutInflater.from(ShoppingList.this.getActivity());
        View promptView = layoutInflater.inflate(R.layout.shopping_check_off, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ShoppingList.this.getActivity());
        alertDialogBuilder.setView(promptView);
        final EditText editText1 = promptView
                .findViewById(R.id.editTextDialogCost);
        final EditText editText2 = promptView
                .findViewById(R.id.editTextDialogEDateInput);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String date = editText2.getText().toString();
                        if (editText1 != null) {
                            double cost = Double.parseDouble(editText1.getText().toString());
                            /*
                            if (budget > 0) {
                                decrease budget by cost
                                if (budget now < 0) send notification
                            }
                            */
                        }
                        List<Food> foods = MainActivity.db.getDatasWithTable("GroceryList");
                        boolean valid = true;
                        for (int i = 0; i < foods.size(); i++) {
                            if (foods.get(i).getFoodItem().equals(itemName)) {
                                if (foods.get(i).getExpirationDate() == null && date.isEmpty()
                                        ||
                                        foods.get(i).getExpirationDate() != null && foods.get(i).getExpirationDate().equals(date)) {
                                    valid = false;
                                    showInsertFail(0);
                                    break;
                                }
                            }
                        }
                        if (valid) {
                            long row = MainActivity.db.insertDatas(itemName, date, "GroceryList");
                            //delete item from shopping list here

                            // reload the current fragment
                            ListAdapterShopping.foods = HelperTool.sortByExpiration(MainActivity.db.getDatasWithTable("ShoppingList"));
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
    public void showInsertFail(int type) {
        LayoutInflater layoutInflater = LayoutInflater.from(ShoppingList.this.getActivity());
        View promptView;
        if (type == 0) {
            promptView = layoutInflater.inflate(R.layout.duplicate_input_alert, null);
        } else if (type == 1) {
            //no name
            promptView = layoutInflater.inflate(R.layout.invalid_input_alert, null);
        } else promptView = layoutInflater.inflate(R.layout.empty_input_alert, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ShoppingList.this.getActivity());
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
    public void showBudgetDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(ShoppingList.this.getActivity());
        View promptView = layoutInflater.inflate(R.layout.input_budget, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                ShoppingList.this.getActivity());
        alertDialogBuilder.setView(promptView);
        final EditText editText1 = promptView
                .findViewById(R.id.editTextDialogBudgetInput);
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        String b = editText1.getText().toString();
                        if(b != null || !b.isEmpty()) {
                            //modify budget in db
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

    public void showImportFragment(){
                ImportFragment dialog = new ImportFragment();
                FragmentManager fragmentManager = ShoppingList.this.getActivity().getFragmentManager();
                dialog.show(fragmentManager, "ImportFragment");
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