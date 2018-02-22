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

public class ListAdapter extends RecyclerView.Adapter {
    private Context mContext;

    List<Food> foods = MainActivity.db.getDatas();

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
        popupMenu.getMenuInflater().inflate(R.menu.action, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.modify:
                        Log.d("in OnMenuItemClick", "I am in modify");
                        showModificationDialog(position);
                        return true;
                    case R.id.delete:
                        Log.d("in OnMenuItemClick", "I am in delete");
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

        DeleteDialogFragment dialog = new DeleteDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putString("item_name", foods.get(position).getFoodItem());
        dialog.setArguments(bundle);
        FragmentManager fragmentManager = ((Activity)mContext).getFragmentManager();
        dialog.show(fragmentManager, "DeleteDialogFragment");

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
