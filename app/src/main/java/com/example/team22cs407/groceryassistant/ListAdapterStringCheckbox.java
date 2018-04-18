package com.example.team22cs407.groceryassistant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.List;

/**
 * Created by yuanyuanji on 4/17/18.
 */

public class ListAdapterStringCheckbox extends ArrayAdapter<String>{
    private final static int INGRE_LIMIT = 1; // change to 5 later
    private int ingre_count = 0;

    public ListAdapterStringCheckbox (Context context, List<String> list) {
        super(context, 0, list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.select_ingredients_row, parent, false);
        }
        TextView ingredient_name = (TextView) convertView.findViewById(R.id.ingredient_name);
        final CheckBox ingredient_checkbox = (CheckBox) convertView.findViewById(R.id.ingredient_checkbox);
        RelativeLayout ingredient_row = (RelativeLayout) convertView.findViewById(R.id.select_ingredients_row);

        ingredient_row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredient_checkbox.performClick();
            }
        });

        ingredient_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                if (isChecked) {
                    ingre_count++;
                } else {
                    ingre_count--;
                }
                if (ingre_count > INGRE_LIMIT) {
                    String msg = "Ingredients Limit is " + INGRE_LIMIT;
                    Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                    ingre_count--;
                    compoundButton.setChecked(false);
                }

            }
        });

        ingredient_name.setText(getItem(position));
        ingredient_checkbox.setChecked(false);

        return convertView;
    }
}
