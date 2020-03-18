package com.example.orderko;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private HashMap<String, List<Drink>> listHash;
    private List<Drink> drinks= new ArrayList<>();
    private DatabaseHelper myDb;
    private Button button;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drink_list, container, false);

        myDb = new DatabaseHelper(getActivity());
        drinks = (List<Drink>)getArguments().get("DrinkList");
        Log.d("firestoretest",drinks.toString());
        myDb.clearTable();
        for(Drink drink : drinks) {
            boolean isInserted = myDb.insertData(drink.getName(),drink.getCategory(),drink.getBulk(),"0");
            if(!isInserted){
                Log.d("databasetest","Eroor while adding");
            }
        }

        initData();
        listView = v.findViewById(R.id.expandable_list);
        button = v.findViewById(R.id.order_but);
        listAdapter = new ExpandableListAdapter(getActivity(),listaDataHeader,listHash);
        listView.setAdapter(listAdapter);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuffer buffer = new StringBuffer();
                Cursor res = myDb.getOrder();
                while (res.moveToNext()) {
                    buffer.append("ID :" + res.getString(0) + "\n");
                    buffer.append("Drink :" + res.getString(1) + "\n");
                    buffer.append("Category :" + res.getString(2) + "\n");
                    buffer.append("Bulk :" + res.getString(3) + "\n");
                    buffer.append("Quantity :" + res.getString(4) + "\n\n");

                }
                Log.d("databasetest" , buffer.toString());


            }
        });

        return v;
    }

    private void initData() {
        Cursor res = myDb.getAllData();
        List<Drink> drinks_adapter= new ArrayList<>();
        Drink drink;
        while(res.moveToNext()) {
            drink = new Drink(res.getString(0),res.getString(1),res.getString(2),res.getString(3),
                    res.getString(4));
            drinks_adapter.add(drink);
        }

        ArrayList<String> checkList = new ArrayList<>();
        listaDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        for(Drink d : drinks_adapter) {
            if(!checkList.contains(d.getCategory())){
                listaDataHeader.add(d.getCategory());
                checkList.add(d.getCategory());
            }
        }
        Log.d("listtest",listaDataHeader.toString());

        int i = 0;
        for(String category : checkList){
            List<Drink> drink_add = new ArrayList<>();
            for(Drink drink_check : drinks_adapter) {
                if(category.equals(drink_check.getCategory()))
                {
                    //Log.d("listtest", "Usao sam ovde zato sto je " + category + " isti kao " + drink_check.getCategory());
                    drink_add.add(drink_check);
                }
            }
            listHash.put(listaDataHeader.get(i), drink_add);
            i++;
        }

    }
}
