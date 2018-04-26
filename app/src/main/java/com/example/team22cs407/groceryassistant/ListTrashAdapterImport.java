package com.example.team22cs407.groceryassistant;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SignInSon on 2/17/18.
 */

public class ListTrashAdapterImport extends RecyclerView.Adapter {
    private Context mContext;

    static List<Food> foods = HelperTool.sortByExpiration(MainActivity.db.getDatas());

    static List<ShoppingFood> shoppingFoods = MainActivity.db.getImportDatas();
    //List<Food> foods = MainActivity.db.getDatas();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.import_list_row, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ListViewHolder) holder).bindView(position);
        //((ListViewHolder) holder).mCheck.setTag(shoppingFoods.get(position));
        ((ListViewHolder) holder).mCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(mContext, foods.get(position).getFoodItem(), Toast.LENGTH_SHORT).show();
                //showPopupMenu(view, position);
                //CheckBox cb = (CheckBox) view;
                //ShoppingFood contact = (ShoppingFood) cb.getTag();

                //contact.setCheckBox(cb.isChecked());
                //shoppingFoods.get(position).setCheckBox(cb.isChecked());
                boolean item = shoppingFoods.get(position).getCheckBox();
                //Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
                if (item) {

                    shoppingFoods.get(position).setCheckBox(false);
                    //view.setSelected(false);
                    //closeInDays.setEnabled(false);
                    //notification_setting_time_msg.setEnabled(false);
                    //notificationSettingTime.setEnabled(false);

                } else {
                    shoppingFoods.get(position).setCheckBox(true);
                    //view.setSelected(true);
                }
                //ListAdapterImport.shoppingFoods = MainActivity.db.getImportDatas();
                //notifyDataSetChanged();
            }
        });

    }

    public void showPopupMenu(View view, final int position){
        //int itemPosition = view.getChildLayoutPosition(view);
        boolean item = shoppingFoods.get(position).getCheckBox();
        //Toast.makeText(mContext, item, Toast.LENGTH_LONG).show();
        if (item) {

            shoppingFoods.get(position).setCheckBox(false);
            //closeInDays.setEnabled(false);
            //notification_setting_time_msg.setEnabled(false);
            //notificationSettingTime.setEnabled(false);

        } else {
            shoppingFoods.get(position).setCheckBox(false);
        }
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        popupMenu.getMenuInflater().inflate(R.menu.action, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.modify:
                        showModificationDialog(position);
                        return true;
                    case R.id.delete:
                        showDeleteDialog(position);
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.show();
    }
    public void showModificationDialog(int position){

        ModificationDialogFragment dialog = new ModificationDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("item_name", foods.get(position).getFoodItem());
        dialog.setArguments(bundle);
        FragmentManager fragmentManager = ((Activity)mContext).getFragmentManager();
        dialog.show(fragmentManager, "ImportFragment");

    }

    public void showDeleteDialog(int position){

        DeleteDialogFragment dialog = new DeleteDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("item_name", foods.get(position).getFoodItem());
        bundle.putString("exp_date", foods.get(position).getExpirationDate());
        dialog.setArguments(bundle);
        FragmentManager fragmentManager = ((Activity)mContext).getFragmentManager();
        dialog.show(fragmentManager, "DeleteDialogFragment");

    }

    @Override
    public int getItemCount() {
        return shoppingFoods.size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mItemText;
        private CheckBox mCheck;
        private LinearLayout listLayout;

        public  ListViewHolder(final View itemView) {
            super(itemView);
            mItemText = (TextView) itemView.findViewById(R.id.itemText);
            mCheck = (CheckBox) itemView.findViewById(R.id.importCheckbox);
            listLayout = (LinearLayout) itemView.findViewById(R.id.import_list_layout);
        }


        public void bindView(int position) {
            String foodName = foods.get(position).getFoodItem();
            boolean check = shoppingFoods.get(position).getCheckBox();
            mItemText.setText(foodName);
            mCheck.setEnabled(true);
        }

        public void onClick(View view) {
        }
    }
}
