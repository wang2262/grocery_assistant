package com.example.team22cs407.groceryassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    /*
                    Intent mygrocery = new Intent(MainActivity.this, MainActivity.class);
                    startActivity(mygrocery);
                     */
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    MyGroceryFragment myGroceryFragment = new MyGroceryFragment();
                    fragmentTransaction.add(R.id.fragment_container, myGroceryFragment);
                    fragmentTransaction.commit();

                    return true;
                case R.id.navigation_dashboard:

                    Intent shoppinglist = new Intent(MainActivity.this, ShoppingList.class);
                    startActivity(shoppinglist);

                    /*
                    Intent feedIntent = new Intent(MainActivity.this, MyGroceryListActivity.class);
                    startActivity( feedIntent );
                    */
                    return true;
                case R.id.navigation_notifications:
                    Intent recipes = new Intent(MainActivity.this, Recipes.class);
                    startActivity(recipes);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextMessage = findViewById(R.id.message);
        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
