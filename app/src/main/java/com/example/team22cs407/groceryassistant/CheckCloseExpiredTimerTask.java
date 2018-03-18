package com.example.team22cs407.groceryassistant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by yuanyuanji on 3/3/18.
 */

public class CheckCloseExpiredTimerTask extends TimerTask {
    private Context parentContext;  // Main activity which call run.
    private  int count = 0;
    private int closeInDays = 1;
    private String CHANNEL_ID = "1";
    private int notificationId = 1;
    private int notificationTime = 16;  // 16:00 pm every day

    public CheckCloseExpiredTimerTask(Context parentContext){
        this.parentContext = parentContext;
        this.closeInDays = 1;
        this.notificationTime = 16;
    }

    public int getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(int notificationTime) {
        this.notificationTime = notificationTime;
    }

    public int getCloseInDays() {

        return closeInDays;
    }

    public void setCloseInDays(int closeInDays) {
        this.closeInDays = closeInDays;
    }

    public CheckCloseExpiredTimerTask(Context parentContext, int closeInDays, int notificationTime) {
        this.parentContext = parentContext;
        this.closeInDays = closeInDays;
        this.notificationTime = notificationTime;
    }

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
        String notificationMsg = constructNotificationMessage(foodToNotify);

        if (foodToNotify.size() > 0) {
            Intent intent = new Intent(parentContext, MainActivity.class);
            // the below is to resume the activity instead of restarting the activity.
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(parentContext, 0, intent, 0);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(parentContext, CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                    .setContentTitle("Grocery Assistant Notification")
                    .setContentText(notificationMsg)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(notificationMsg))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true);
            
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(parentContext);
            notificationManager.notify(notificationId, mBuilder.build());
        }
    }

    public String constructNotificationMessage(List<Food> foodToNotify) {
        StringBuilder sb = new StringBuilder();
        int size = foodToNotify.size();
        for(int i = 0; i < size; i++) {
            sb.append(foodToNotify.get(i).getFoodItem() + ", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        // insert substring "and"
        if (size > 1) {
            int index = sb.lastIndexOf(",");
            sb.replace(index, index + 1, " and");
            sb.append(" are ");
        }
        else {
            sb.append(" is ");
        }
        sb.append("about to expire on " + foodToNotify.get(0).getExpirationDate() + ".");
        System.out.println(sb.toString());
        return sb.toString();
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
