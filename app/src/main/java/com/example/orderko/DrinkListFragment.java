package com.example.orderko;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrinkListFragment extends Fragment {
    private ExpandableListView listView;
    private HashMap<String, List<Drink>> listHash;
    private List<Drink> drinks= new ArrayList<>();
    private DatabaseHelper myDb;
    private Button button;
    private ExpandingList expandingList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drink_list, container, false);

        expandingList = v.findViewById(R.id.expanding_list_main);

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


        button = v.findViewById(R.id.order_but);


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
        initData();
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



        ArrayList<String> categorys = new ArrayList<>();

        for(Drink d : drinks_adapter) {
            if(!categorys.contains(d.getCategory())){
                categorys.add(d.getCategory());
            }
        }
        Log.d("listtest",categorys.toString());

        for(String ctg : categorys) {
            ExpandingItem item = expandingList.createNewItem(R.layout.expanding_layout);
            ((TextView) item.findViewById(R.id.title)).setText(ctg);
            Cursor rez = myDb.getDrinksOf(ctg);
            item.createSubItems(rez.getCount());
            int i = 0;
            while(rez.moveToNext()) {

                View v = item.getSubItemView(i);
                final String id = rez.getString(0);
                ((TextView) v.findViewById(R.id.sub_title)).setText(rez.getString(1));
                ((TextView) v.findViewById(R.id.sub_bulk)).setText(rez.getString(3));
                final EditText sub_quantity = v.findViewById(R.id.sub_quantity);

                ((ImageButton) v.findViewById(R.id.sub_add_but)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int sub_quant = Integer.parseInt(sub_quantity.getText().toString());
                        myDb.updateQuantity(Integer.parseInt(id),String.valueOf(sub_quant+1));
                        sub_quantity.setText(String.valueOf(sub_quant+1));
                    }
                });

                ((ImageButton)v.findViewById(R.id.sub_remove_but)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final int sub_quant = Integer.parseInt(sub_quantity.getText().toString());
                        if(sub_quant > 0) {
                            myDb.updateQuantity(Integer.parseInt(id),String.valueOf(sub_quant-1));
                            sub_quantity.setText(String.valueOf(sub_quant-1));
                        }
                    }
                });

                i++;
            }

        }

    }
}
