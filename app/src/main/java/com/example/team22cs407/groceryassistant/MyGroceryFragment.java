package com.example.team22cs407.groceryassistant;


import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.Fragment;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by yuanyuanji on 2/6/18.
 */

public class MyGroceryFragment extends Fragment {
    private  static String[] GROCERY_LIST = new String[] {"apple", "milk", "eggs"};
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.my_grocery_fragment, container, false);

        ListView listView = view.findViewById(R.id.grocery_list);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, GROCERY_LIST);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getActivity(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
                ModificationDialogFragment dialog = new ModificationDialogFragment();
                FragmentManager fragmentManager = getFragmentManager();
                //dialog.show(fragmentManager, "ModificationDialogFragment");  // If ModificationDialogFragment extends android.support.v4.fragment, it will work!!
            }
        });


        return view;
    }
}
