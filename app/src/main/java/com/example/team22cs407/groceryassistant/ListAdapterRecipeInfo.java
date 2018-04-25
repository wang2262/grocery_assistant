package com.example.team22cs407.groceryassistant;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by yuanyuanji on 4/22/18.
 */

public class ListAdapterRecipeInfo extends ArrayAdapter<RecipeInfo> {
    private  Context context;
    private List<RecipeInfo> recipeInfos;

    public ListAdapterRecipeInfo (Context context, List<RecipeInfo> recipeInfos) {
        super(context,0, recipeInfos); // Notes: without passing list of items, the constructor does not get called.
        this.context = context;
        this.recipeInfos = recipeInfos;
        

    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.recipe_info_row, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.recipe_title);
        ImageView imageView = convertView.findViewById(R.id.recipe_image);

        RecipeInfo item = recipeInfos.get(position);
        textView.setText(item.getTitle());

        Picasso.with(context)
                .load(item.getImage())
                .into(imageView);
        /*
        try {
            URL url = new URL(item.getImage());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageView.setImageBitmap(bmp);

        } catch (Exception e) {
            e.printStackTrace();
        }
        */
        return convertView;
    }
}
