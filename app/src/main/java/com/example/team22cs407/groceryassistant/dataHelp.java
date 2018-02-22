package com.example.team22cs407.groceryassistant;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sam on 2/13/18.
 */

public class dataHelp {
    myDbHelper dataHelp;
    public dataHelp(Context context) {
        dataHelp = new myDbHelper(context);
    }

    public long insertData(String name, String date) {
        if(name != null && !name.isEmpty()) {
            SQLiteDatabase db = dataHelp.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(myDbHelper.NAME, name);
            if(date != null && !date.isEmpty())
                contentValues.put(myDbHelper.DATE, date);
            long id = db.insertWithOnConflict(myDbHelper.TABLE_NAME, null , contentValues, SQLiteDatabase.CONFLICT_ABORT);
            return id;
        } else return -1;

    }


    public String getData()
    {
        SQLiteDatabase db = dataHelp.getWritableDatabase();
        String[] columns = {myDbHelper.UID,myDbHelper.NAME,myDbHelper.DATE};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        while (cursor.moveToNext())
        {
            int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            String name =cursor.getString(cursor.getColumnIndex(myDbHelper.NAME));
            String  password =cursor.getString(cursor.getColumnIndex(myDbHelper.DATE));
            buffer.append(cid+ "   " + name + "   " + password +" \n");
        }
        return buffer.toString();
    }

    public List<Food> getDatas()
    {
        SQLiteDatabase db = dataHelp.getWritableDatabase();
        String[] columns = {myDbHelper.UID,myDbHelper.NAME,myDbHelper.DATE};
        Cursor cursor = db.query(myDbHelper.TABLE_NAME,columns,null,null,null,null,null);
        StringBuffer buffer= new StringBuffer();
        List<Food> foods = new ArrayList<>();
        while (cursor.moveToNext())
        {
            Food food = new Food();
            int cid =cursor.getInt(cursor.getColumnIndex(myDbHelper.UID));
            food.setFoodItem(cursor.getString(cursor.getColumnIndex(myDbHelper.NAME)));
            food.setExpirationDate(cursor.getString(cursor.getColumnIndex(myDbHelper.DATE)));
            foods.add(food);
        }
        return foods;
    }

    public  int delete(String uname)
    {
        SQLiteDatabase db = dataHelp.getWritableDatabase();
        String[] whereArgs ={uname};

        int count =db.delete(myDbHelper.TABLE_NAME ,myDbHelper.NAME+" = ?",whereArgs);
        return  count;
    }

    public int updateName(String oldName , String newName)
    {
        SQLiteDatabase db = dataHelp.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(myDbHelper.NAME,newName);
        String[] whereArgs= {oldName};
        int count =db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.NAME+" = ?",whereArgs );
        return count;
    }

    // the argument order is supposed to be oldName, newName, newDate, which either newName or newDate could be optional.
    public int updateData(String... args) {
        if (args.length <= 1 || args.length > 3) {
            return 0;
        }
        SQLiteDatabase db = dataHelp.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String[] whereArgs= {args[0]};
        if (!args[1].equals("")) {
            contentValues.put(myDbHelper.NAME, args[1]);
        }
        if (args.length > 2 && !args[2].equals("")) {
            contentValues.put(myDbHelper.DATE, args[2]);
        }
        int count = db.update(myDbHelper.TABLE_NAME,contentValues, myDbHelper.NAME+" = ?",whereArgs );
        return count;
    }

    static class myDbHelper extends SQLiteOpenHelper
    {

        private static final String DATABASE_NAME = "database";    // Database Name
        private static final String TABLE_NAME = "GroceryList";   // Table Name
        private static final int DATABASE_Version = 1;    // Database Version
        private static final String UID="_id";     // Column I (Primary Key)
        private static final String NAME = "Name";    //Column II
        private static final String DATE = "Date";    // Date that food expires
        private static final String CREATE_TABLE = "CREATE TABLE "+TABLE_NAME+
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+NAME+" VARCHAR(255), "+ DATE+" VARCHAR(225));";
        private static final String DROP_TABLE ="DROP TABLE IF EXISTS "+TABLE_NAME;
        private Context context;


        public myDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_Version);
            this.context=context;
        }

        public void onCreate(SQLiteDatabase db) {

            try {
                db.execSQL(CREATE_TABLE);
            } catch (Exception e) {
                //Message.message(context,""+e);
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            try {
               // Message.message(context,"OnUpgrade");
                db.execSQL(DROP_TABLE);
                onCreate(db);
            }catch (Exception e) {
               // Message.message(context,""+e);
            }
        }
    }
}
