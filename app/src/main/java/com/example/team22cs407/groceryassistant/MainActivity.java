package com.example.team22cs407.groceryassistant;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.database.sqlite.*;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
    SQLiteDatabase db;

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = MyGrocery.newInstance();
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.frame_layout, selectedFragment);
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
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        db = openOrCreateDatabase("grocery", MODE_PRIVATE, null);
    }

}
