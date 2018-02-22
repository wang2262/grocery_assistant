package com.example.team22cs407.groceryassistant;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.database.sqlite.*;
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity implements ModificationDialogFragment.ModificationDialogListener {

    static dataHelp db;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment;
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    selectedFragment = new MyGrocery();
                    transaction.replace(R.id.frame_layout, selectedFragment);
                    break;
                case R.id.navigation_dashboard:
                    selectedFragment = new ShoppingList();
                    transaction.replace(R.id.frame_layout, selectedFragment);
                    break;
                case R.id.navigation_notifications:
                    selectedFragment = new Recipes();
                    transaction.replace(R.id.frame_layout, selectedFragment);
                    break;
            }
            transaction.commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new dataHelp(this);
        setContentView(R.layout.activity_main);
        Fragment default_fragment = new MyGrocery();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, default_fragment);
        transaction.commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

    }

    @Override
    public void onDialogPositiveClick(View view, int position) {
        System.out.println("position: " + position);   // 0 based

        EditText nameView = view.findViewById(R.id.item_name);
        EditText expirationView = view.findViewById(R.id.item_expiration);


        // compare old name with new name, and old date with new date  // is the position equals UID
        List<Food> data = ListAdapter.foods;
        String oldName = data.get(position).getFoodItem();
        String oldDate = data.get(position).getExpirationDate();

        String newName = nameView.getText().toString();
        String newDate = expirationView.getText().toString();

        System.out.println("before:");
        System.out.println(db.getData());


        if (!oldName.equals(newName) && !oldDate.equals(newDate)) {
            db.updateData(oldName, newName, newDate);
        } else if (!oldDate.equals(newDate)) {
            db.updateData(oldName, "", newDate);  // we just modify date
        } else if (!oldName.equals(newName)) {
            db.updateData(oldName, newName);
        } else {
            return;
        }

        System.out.println("after:");
        System.out.println(db.getData());
        // reload the current fragment
        //Fragment fragment = getFragmentManager().findFragmentById(R.id.frame_layout);
        //getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
        Fragment fragment = new MyGrocery();
        getFragmentManager().beginTransaction().replace(R.id.frame_layout, fragment).commit();
    }
    @Override
    public void onDialogNegativeClick(View view, int position){
        System.out.println("I AM IN NEGATIVE");
        System.out.println("position: " + position);

    }


}
