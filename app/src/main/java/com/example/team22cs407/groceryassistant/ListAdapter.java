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

/**
 * Created by SignInSon on 2/17/18.
 */

public class ListAdapter extends RecyclerView.Adapter {
    private Context mContext;

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
                Toast.makeText(mContext, OurData.title[position], Toast.LENGTH_SHORT).show();
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
        bundle.putString("item_name", OurData.title[position]);
        bundle.putString("expiration_date", "01/12/2018");
        dialog.setArguments(bundle);
        FragmentManager fragmentManager = ((Activity)mContext).getFragmentManager();
        dialog.show(fragmentManager, "ModificationDialogFragment");

    }

    @Override
    public int getItemCount() {
        return OurData.title.length;
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mItemText;
        private LinearLayout listLayout;

        public  ListViewHolder(final View itemView) {
            super(itemView);
            mItemText = (TextView) itemView.findViewById(R.id.itemText);
            //itemView.setOnClickListener(this);

            listLayout = (LinearLayout) itemView.findViewById(R.id.list_layout);
            /*
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view){
                    int position = getAdapterPosition();
                    Toast.makeText(itemView.getContext(), OurData.title[position], Toast.LENGTH_SHORT).show();

                }
            });
            */

        }

        public void bindView(int position) {
            mItemText.setText(OurData.title[position]);
        }

        public void onClick(View view) {

        }
    }
}
