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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_shopping, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ListViewHolder) holder).bindView(position);
        ((ListViewHolder) holder).cbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = foods.get(position).getCheckBox();
                if (checked) {
                    foods.get(position).setCheckBox(false);
                } else {
                    foods.get(position).setCheckBox(true);
                    String itemName = foods.get(position).getFoodItem();
                    //Fragment fragment = ((Activity)mContext).getFragmentManager().findFragmentByTag("ShoppingList");
                    ShoppingList list = new ShoppingList();
                    System.out.println("itemName: " + itemName);
                    list.showCheckoffDialog(itemName);
                    Log.d("function, ", "checkoffDialog");
                }
            }
        });
        ((ListViewHolder) holder).mItemText.setOnClickListener(new View.OnClickListener() {
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
                   // case R.id.modify:
                     //   showModificationDialog(position);
                       // return true;
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
        bundle.putString("expiration_date", foods.get(position).getExpirationDate());
        dialog.setArguments(bundle);
        FragmentManager fragmentManager = ((Activity)mContext).getFragmentManager();
        dialog.show(fragmentManager, "ModificationDialogFragment");

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
        private CheckBox cbox;


        public  ListViewHolder(final View itemView) {
            super(itemView);
            mItemText = (TextView) itemView.findViewById(R.id.itemText);
            mExpirationText = (TextView) itemView.findViewById(R.id.expirationText);
            listLayout = (LinearLayout) itemView.findViewById(R.id.list_layout);
            cbox = (CheckBox) itemView.findViewById(R.id.shoppingCheckBox);

        }


        public void bindView(int position) {
            String foodName = foods.get(position).getFoodItem();
            String expirationDate = foods.get(position).getExpirationDate();
            mItemText.setText(foodName);
            mExpirationText.setText(expirationDate);
            cbox.setEnabled(true);
        }

        public void onClick(View view) {

        }
    }
}
