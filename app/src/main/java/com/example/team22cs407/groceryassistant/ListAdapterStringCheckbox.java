package com.example.team22cs407.groceryassistant;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuanyuanji on 4/17/18.
 */

public class ListAdapterStringCheckbox extends ArrayAdapter<String>{
    private final static int INGRE_LIMIT = 3; // change to 5 later  // the limit of selected ingredients
    private int ingre_count = 0;
    private List<String> checkedItems;

    public ListAdapterStringCheckbox (Context context, List<String> list) {
        super(context, 0, list);
        checkedItems = new ArrayList<>();
    }

    public List<String> getCheckedItems(){
        return checkedItems;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
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
                    if (ingre_count < INGRE_LIMIT) {
                        ingre_count++;
                        checkedItems.add(getItem(position));
                    } else {
                        String msg = "Ingredients Limit is " + INGRE_LIMIT;
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        compoundButton.setChecked(false);
                    }

                } else {
                    ingre_count--;
                    checkedItems.remove(getItem(position));
                }

            }
        });

        ingredient_name.setText(getItem(position));
        ingredient_checkbox.setChecked(false);

        return convertView;
    }
}
