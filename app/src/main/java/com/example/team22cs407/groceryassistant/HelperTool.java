package com.example.team22cs407.groceryassistant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * Created by yuanyuanji on 2/21/18.
 */

public class HelperTool {


    public static List<Food> sortByExpiration(List<Food> foodList) {

        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Collections.sort(foodList, new Comparator<Food>() {

            @Override
            public int compare(Food f1, Food f2) {
                try {
                    Date date1 = sdf.parse(f1.getExpirationDate());

                    Date date2 = sdf.parse(f2.getExpirationDate());

                    return date1.compareTo(date2);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return -999;
            }
        });

        return foodList;
    }
}
