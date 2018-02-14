package com.example.team22cs407.groceryassistant;

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
import android.database.sqlite.*;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase db;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

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

    public static class dataHelp extends SQLiteOpenHelper {
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "grocery.db";


        public dataHelp(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE grocery (name TEXT, date TEXT)");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS grocery");
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

    }
}
