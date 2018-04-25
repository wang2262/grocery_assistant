package com.example.team22cs407.groceryassistant;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by SignInSon on 2/17/18.
 */

public class ListAdapterShopping extends RecyclerView.Adapter {
    private Context mContext;

    //static List<Food> foods = HelperTool.sortByExpiration(MainActivity.db.getDatasWithTable("ShoppingList"));
    static List<Food> foods = MainActivity.db.getDatasWithTable("ShoppingList");
    //List<Food> foods = MainActivity.db.getDatas();

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ListViewHolder) holder).bindView(position);
        ((ListViewHolder) holder).listLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(mContext, foods.get(position).getFoodItem(), Toast.LENGTH_SHORT).show();
                showPopupMenu(view, position);
            }
        });

    }

    public void showPopupMenu(View view, final int position){
        PopupMenu popupMenu = new PopupMenu(mContext, view);
        popupMenu.getMenuInflater().inflate(R.menu.shopping_action, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.add_quantity:
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

        AddQuantityDialogFragment dialog = new AddQuantityDialogFragment();
        Bundle bundle = new Bundle();
        //bundle.putInt("position", position);
        bundle.putString("item_name", foods.get(position).getFoodItem());
        dialog.setArguments(bundle);
        FragmentManager fragmentManager = ((Activity)mContext).getFragmentManager();
        dialog.show(fragmentManager, "AddQuantityDialogFragment");
    }

    public void showDeleteDialog(int position){

        ShoppingDeleteDialogFragment dialog = new ShoppingDeleteDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("item_name", foods.get(position).getFoodItem());
        //bundle.putString("exp_date", foods.get(position).getExpirationDate());
        dialog.setArguments(bundle);
        FragmentManager fragmentManager = ((Activity)mContext).getFragmentManager();
        dialog.show(fragmentManager, "ShoppingDeleteDialogFragment");

    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mItemText;
        private TextView mExpirationText;
        private LinearLayout listLayout;


        public  ListViewHolder(final View itemView) {
            super(itemView);
            mItemText = (TextView) itemView.findViewById(R.id.itemText);
            mExpirationText = (TextView) itemView.findViewById(R.id.expirationText);
            listLayout = (LinearLayout) itemView.findViewById(R.id.list_layout);

        }


        public void bindView(int position) {
            String foodName = foods.get(position).getFoodItem();
            String expirationDate = foods.get(position).getExpirationDate();
            mItemText.setText(foodName);
            mExpirationText.setText(expirationDate);
        }

        public void onClick(View view) {

        }
    }
}
