package com.example.team22cs407.groceryassistant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

/**
 * Created by yuanyuanji on 3/3/18.
 */

public class CheckCloseExpiredTimerTask extends TimerTask {
    private  int count = 0;
    private int closeInDays = 2;

    @Override
    public void run() {
        // check whether expiration date close to 2 days to expire
        System.out.println("I am in CheckExpirationTimerTask. count: " + count++);
        List<Food> foodList = MainActivity.db.getDatas();
        List<Food> foodToNotify = new ArrayList<>();
        for (Food item : foodList) {
            if (item.getExpirationDate() != null && !item.getExpirationDate().isEmpty() && checkCloseExpired(item.getExpirationDate(), closeInDays)) {
                foodToNotify.add(item);
            }
        }
        // the below are for testing purpose
        System.out.println("printing foodToNotify:");
        for (Food item : foodToNotify) {
            System.out.println(item.getFoodItem() + ", " + item.getExpirationDate());
        }
    }

    /**
     * check whether today is N days close to the expiration date, where N is the parameter closeInDays.
     */
    public boolean checkCloseExpired(String expirationDate, int closeInDays ) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date expiration = sdf.parse(expirationDate);
            Date today = new Date();
            long diff = expiration.getTime() - today.getTime();

            if (diff > 0) {
                long diffDays = diff / (1000 * 60 * 60 * 24);
                if (diffDays  + 1 == closeInDays) { // The reason we add 1 day is that we want the time of expiration date accounted as 11:59 pm not 00:00 am.
                    return true;
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
