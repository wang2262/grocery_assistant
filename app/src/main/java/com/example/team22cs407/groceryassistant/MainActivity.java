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
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Calendar;
import android.support.v4.app.NotificationCompat;

public class MainActivity extends AppCompatActivity implements ModificationDialogFragment.ModificationDialogListener {

    static dataHelp db;

    private Timer timer;
    private CheckCloseExpiredTimerTask checkExpirationTimerTask;

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

        // for notification
        timer = new Timer();
        checkExpirationTimerTask = new CheckCloseExpiredTimerTask(this, 1);
        Date today = new Date();
        long period = 1000 * 60 * 60 * 24; // 24 hr in million second
        //long period = 3;
        long delayToFirstTime = getDelayToFirstTime();
        System.out.println("delay: " + delayToFirstTime);
        //timer.schedule(checkExpirationTimerTask, today, period);
        timer.schedule(checkExpirationTimerTask, delayToFirstTime, period);
    }
    // assuming the first time is 16:00 during the day
    public long getDelayToFirstTime() {
        Calendar scheduledTime = Calendar.getInstance();
        Date today = scheduledTime.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        System.out.println("NOW time: " + sdf.format(today));
        scheduledTime.set(Calendar.HOUR_OF_DAY, 21);
        scheduledTime.set(Calendar.MINUTE, 1);
        scheduledTime.set(Calendar.SECOND, 10);
        long timediff = scheduledTime.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        if (timediff > 0) {
            return timediff;
        }
        long dayInMillis = 24 * 60 * 60 * 1000; // 24 hr in million second
        return timediff + dayInMillis;
    }


    @Override
    public void onDialogPositiveClick(View view, int position) {
        System.out.println("position: " + position);   // 0 based

        EditText nameView = view.findViewById(R.id.item_name);
        EditText expirationView = view.findViewById(R.id.item_expiration);


        List<Food> data = ListAdapter.foods;
        String oldName = data.get(position).getFoodItem();
        String oldDate = data.get(position).getExpirationDate();
        if (oldDate == null)   //allow user adding expiration dates
            oldDate = "";

        String newName = nameView.getText().toString();
        String newDate = expirationView.getText().toString();
        /*
        System.out.println("new name: " + newName);
        System.out.println("new date: " + newDate);
       */
        if (emptyNameCheck(newName))
            return;
        if (newDate.isEmpty()) {   // allow user changing expiration date to null
            newDate = null;
        }
        /*
        System.out.println("before:");
        System.out.println(db.getData());
        */
        // update database
        if (!oldName.equals(newName) && !oldDate.equals(newDate)) {
            db.updateData(oldName, newName, newDate);
        } else if (!oldDate.equals(newDate)) {
            db.updateData(oldName, "", newDate);  // we just modify date
        } else if (!oldName.equals(newName)) {
            db.updateData(oldName, newName);
        } else {
            return;
        }

        /*
        System.out.println("after:");
        System.out.println(db.getData());
        */
        // update in-memory data
        ListAdapter.foods = HelperTool.sortByExpiration(MainActivity.db.getDatas());
        // reload the current fragment
        Fragment fragment = getFragmentManager().findFragmentById(R.id.frame_layout);
        getFragmentManager().beginTransaction().detach(fragment).attach(fragment).commit();
    }
    @Override
    public void onDialogNegativeClick(View view, int position){
        System.out.println("I AM IN NEGATIVE");
        System.out.println("position: " + position);

    }

    public boolean emptyNameCheck(String name) {
        if (name.isEmpty()) {
            //Toast.makeText(getApplicationContext(), "Item name can not be empty", Toast.LENGTH_SHORT);
            // TODO: give error message and stay on this dialog if possible
            return true;
        }
        return false;
    }

}
