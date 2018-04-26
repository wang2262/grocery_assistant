package com.example.team22cs407.groceryassistant;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;



public class RecipeDetailFragment extends Fragment {


    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static RecipeDetailFragment newInstance(String url) {
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        Bundle args = new Bundle();
        args.putString("webUrl", url);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        System.out.println("inside webcreate function");
        View v = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        WebView wView = (WebView)v.findViewById(R.id.web_view);
        wView.getSettings().setJavaScriptEnabled(true);
        wView.setWebViewClient(new WebViewClient());
        System.out.println("args:"+ getArguments());
        wView.loadUrl(getArguments().getString("webView"));
        return v;
    }


}
