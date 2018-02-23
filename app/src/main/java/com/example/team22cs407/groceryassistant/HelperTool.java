package com.example.team22cs407.groceryassistant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


/**
 * Created by yuanyuanji on 2/21/18.
 */

public class HelperTool {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    public static List<Food> sortByExpiration(List<Food> foodList) {

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 10);
        final Date longest = cal.getTime();

        Collections.sort(foodList, new Comparator<Food>() {

            @Override
            public int compare(Food f1, Food f2) {
                try {
                    Date date1 = longest;
                    Date date2 = longest;
                    if (f1.getExpirationDate() != null && !f1.getExpirationDate().isEmpty())
                        date1 = sdf.parse(f1.getExpirationDate());

                    if (f2.getExpirationDate() != null && !f2.getExpirationDate().isEmpty())
                        date2 = sdf.parse(f2.getExpirationDate());

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
