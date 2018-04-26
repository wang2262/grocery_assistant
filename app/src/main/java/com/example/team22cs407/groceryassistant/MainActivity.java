package com.example.team22cs407.groceryassistant;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.List;
import java.util.Timer;


public class MainActivity extends AppCompatActivity implements ModificationDialogFragment.ModificationDialogListener {

    static dataHelp db;
    // we have to set timer and the timer task static, because we could not access the current MainActivity instance in NotificationSettingDialogFragment.
    private Timer timer;
    private CheckCloseExpiredTimerTask checkExpirationTimerTask;  // TO DO: deal with this warning later

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
        File budgetData = new File(this.getFilesDir(), "budgetData");
        if (!budgetData.exists()) {
            try {
                budgetData.createNewFile();
                FileOutputStream outputStream;
                String fileContents = "0.00";
                try {
                    outputStream = this.openFileOutput("budgetData", Context.MODE_PRIVATE);
                    outputStream.write(fileContents.getBytes());
                    outputStream.close();
                } catch (Exception a) {
                    a.printStackTrace();
                }
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }


        setContentView(R.layout.activity_main);
        Fragment default_fragment = new MyGrocery();
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.frame_layout, default_fragment);
        transaction.commit();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // for notification
        timer = new Timer();
        scheduleTask(new CheckCloseExpiredTimerTask(this));
    }

    public void scheduleTask(CheckCloseExpiredTimerTask timerTask) {
        if (timer == null || timerTask == null) {
            return;
        }
        checkExpirationTimerTask = timerTask;
        Date today = new Date();
        long period = 1000 * 60 * 60 * 24; // 24 hr in million second
        //long period = 3;
        //long delayToFirstTime = getDelayToFirstTime(timerTask.getNotificationTime());
        long delayToFirstTime = checkExpirationTimerTask.getDelayToFirstTime(); // change 21 to notificationTime
        System.out.println("delay: " + delayToFirstTime);
        //timer.schedule(timerTask, today, period);
        timer.schedule(timerTask, delayToFirstTime, period);
    }

    public void cancelTimerTask(){
        if (checkExpirationTimerTask != null) {
            checkExpirationTimerTask.cancel();
        }
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

        if (emptyNameCheck(newName))
            return;
        if (newDate.isEmpty()) {   // allow user changing expiration date to null
            newDate = null;
        }
        String uid = Integer.toString(data.get(position).getId());
        // update database
        if (!oldName.equals(newName) && !oldDate.equals(newDate)) {
            db.updateData(uid, newName, newDate);
        } else if (!oldDate.equals(newDate)) {
            db.updateData(uid, "", newDate);  // we just modify date
        } else if (!oldName.equals(newName)) {
            db.updateData(uid, newName);
        } else {
            return;
        }

        // update in-memory data
        ListAdapter.foods = HelperTool.sortByExpiration(MainActivity.db.getDatas());
        ListAdapterImport.foods = HelperTool.sortByExpiration(MainActivity.db.getDatas());
        ListAdapterImport.shoppingFoods = MainActivity.db.getImportDatas();
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
