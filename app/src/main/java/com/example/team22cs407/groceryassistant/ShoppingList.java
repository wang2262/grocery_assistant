package com.example.team22cs407.groceryassistant;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.content.DialogInterface;
import android.widget.Button;
import android.app.AlertDialog;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Objects;


import android.app.FragmentManager;
import com.google.android.gms.vision.barcode.Barcode;


import android.support.annotation.NonNull;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;

import static android.widget.GridLayout.VERTICAL;

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
    TextView barcodeResult;

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

        String budget = readBudget();
        Button budgetButton = view.findViewById(R.id.budget_button);
        budgetButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showBudgetDialog(v);
            }
        });
        budgetButton.setText("Budget: " + budget);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listRecyclerView);
        DividerItemDecoration itemDecor = new DividerItemDecoration(recyclerView.getContext(), VERTICAL);
        recyclerView.addItemDecoration(itemDecor);

        ListAdapterShopping listAdapter = new ListAdapterShopping();
        recyclerView.setAdapter(listAdapter);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        Button barcodeButton = view.findViewById(R.id.scan_code);
        barcodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanBarcode(view);
            }
        });
        //barcodeResult = (TextView) view.findViewById(R.id.barcode_result);

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
                                    showNotification(0);
                                    break;
                                }
                            }
                        }

                        if (valid) {
                            long row = MainActivity.db.insertDatas(name, "", "ShoppingList");
                            if (row < 0) {
                                showNotification(1);
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

    public void showNotification(int type) {
        LayoutInflater layoutInflater = LayoutInflater.from(ShoppingList.this.getActivity());
        View promptView;
        if (type == 0) {
            promptView = layoutInflater.inflate(R.layout.duplicate_input_alert, null);
        } else if (type == 1) {
            //no name
            promptView = layoutInflater.inflate(R.layout.invalid_input_alert, null);
        } else if (type == 2) {
            promptView = layoutInflater.inflate(R.layout.empty_input_alert, null);
        } else {
            promptView = layoutInflater.inflate(R.layout.reached_budget, null);
        }

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
    public void showBudgetDialog(final View view){
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
                        if(editText1 != null && !b.isEmpty() && b.matches("\\d+(.\\d+)?")) {
                            String formatted_b = String.format("%.2f", Double.parseDouble(b));
                            updateBudget(formatted_b);
                            Button button = view.findViewById(R.id.budget_button);
                            button.setText("Budget: " + readBudget());
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

    public void showImportFragment(){
                ImportFragment dialog = new ImportFragment();
                FragmentManager fragmentManager = ShoppingList.this.getActivity().getFragmentManager();
                dialog.show(fragmentManager, "ImportFragment");
    }

    public void scanBarcode(View view) {
        Intent intent;
        intent = new Intent(getContext(), BarcodeCaptureActivity.class);
       // intent.putExtra("falsetrue", true);
        getActivity().startActivityForResult(intent, 0);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 0) {
            if(resultCode == CommonStatusCodes.SUCCESS) {
                if(data!=null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    Toast.makeText(getContext(), barcode.displayValue, Toast.LENGTH_SHORT).show();
                    Log.d("display value", barcode.displayValue);
                    barcodeResult.setText("Barcode Number: " + barcode.displayValue);
                }
            }
        }
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