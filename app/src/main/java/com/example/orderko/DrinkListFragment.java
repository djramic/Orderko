package com.example.orderko;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrinkListFragment extends Fragment {
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listaDataHeader;
    private HashMap<String, List<String>> listHash;
    private List<Drink> drinks= new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drink_list, container, false);

        drinks = (List<Drink>)getArguments().get("DrinkList");
        Log.d("firestoretest",drinks.toString());

        initData();
        listView = v.findViewById(R.id.expandable_list);
        listAdapter = new ExpandableListAdapter(getActivity(),listaDataHeader,listHash);
        listView.setAdapter(listAdapter);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                return true;
            }
        });

        return v;
    }

    private void initData() {

        ArrayList<String> checkList = new ArrayList<>();
        listaDataHeader = new ArrayList<>();

        for(Drink drink : drinks) {
            if(!checkList.contains(drink.getCategory())){
                listaDataHeader.add(drink.getCategory());
                checkList.add(drink.getCategory());
            }
        }
        Log.d("listtest",listaDataHeader.toString());


        listHash = new HashMap<>();

        int i = 0;
        for(String category : checkList){
            List<String> drink_add = new ArrayList<>();
            for(Drink drink_check : drinks) {
                if(category.equals(drink_check.getCategory()))
                {
                    //Log.d("listtest", "Usao sam ovde zato sto je " + category + " isti kao " + drink_check.getCategory());
                    drink_add.add(drink_check.getName());
                }
            }
            listHash.put(listaDataHeader.get(i), drink_add);
            i++;
        }


    }
}
