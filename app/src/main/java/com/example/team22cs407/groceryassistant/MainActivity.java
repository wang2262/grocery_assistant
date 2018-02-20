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
        System.out.println("I AM IN POSITIVE");
        System.out.println("position: " + position);

        EditText nameView = view.findViewById(R.id.item_name);
        System.out.println("name: " + nameView.getText());

        EditText expirationView = view.findViewById(R.id.item_expiration);
        System.out.println("expirationDate: " + expirationView.getText());

        // compare old name with new name, and old date with new date
        String oldName = OurData.title[position];
        String newName = nameView.getText().toString();
        System.out.println(db.getData());

        if (!oldName.equals(newName)) {
            db.updateData(oldName, newName);
        }
        System.out.println("after---------------");
        System.out.println(db.getData());

    }
    @Override
    public void onDialogNegativeClick(View view, int position){
        System.out.println("I AM IN NEGATIVE");
        System.out.println("position: " + position);

    }

}
