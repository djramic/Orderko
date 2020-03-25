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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.diegodobelo.expandingview.ExpandingItem;
import com.diegodobelo.expandingview.ExpandingList;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrinkListFragment extends Fragment {
    private List<Drink> drinks= new ArrayList<>();
    private String club;
    private DatabaseHelper myDb;
    private Button button;
    private ExpandingList expandingList;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference tableRef;
    private User user;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drink_list, container, false);

        expandingList = v.findViewById(R.id.expanding_list_main);
        user = User.getInstance();

        Log.d("usertable","Imas sto:" +  user.getTable());

        myDb = new DatabaseHelper(getActivity());
        drinks = (List<Drink>)getArguments().get("DrinkList");
        club = (String)getArguments().get("Club");
        Log.d("databasetest","Klub koji trazim: " + club);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference(club + "/orders");
        tableRef = database.getReference(club + "/tables");


        //Log.d("firestoretest",drinks.toString());
        myDb.clearTable();
        for(Drink drink : drinks) {
            boolean isInserted = myDb.insertData(drink.getName(),drink.getCategory(),drink.getBulk(),"0",drink.getPrice());
            if(!isInserted){
                Log.d("databasetest","Eroor while adding");
            }
        }
        initData();


        button = v.findViewById(R.id.order_but);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*

                    StringBuffer buffer = new StringBuffer();
                    Cursor res = myDb.getOrder();
                    while (res.moveToNext()) {
                    buffer.append("ID :" + res.getString(0) + "\n");
                    buffer.append("Drink :" + res.getString(1) + "\n");
                    buffer.append("Category :" + res.getString(2) + "\n");
                    buffer.append("Bulk :" + res.getString(3) + "\n");
                    buffer.append("Quantity :" + res.getString(4) + "\n\n");
                        String id = myRef.push().getKey();
                        Order order = new Order(id, res.getString(1),res.getString(2), res.getString(4),res.getString(3),user.getTable());
                        myRef.child(id).setValue(order);
                    }
                    Log.d("databasetest" , buffer.toString());
                    getFragmentManager().beginTransaction().detach(DrinkListFragment.this).attach(DrinkListFragment.this).commit();

                }else {
                    Toast.makeText(getActivity(),"Niste izabrali nijedan sto", Toast.LENGTH_LONG).show();
                }*/
                if(user.getTable() != null) {
                    OrderDialogClass cdd = new OrderDialogClass(getActivity());
                    cdd.show();
                }else{
                    Toast.makeText(getContext(),"Moraš prvo izabrati sto", Toast.LENGTH_LONG).show();
                }

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
                    res.getString(4), res.getString(5));
            drinks_adapter.add(drink);
        }



        ArrayList<String> categorys = new ArrayList<>();

        for(Drink d : drinks_adapter) {
            if(!categorys.contains(d.getCategory())){
                categorys.add(d.getCategory());
            }
        }
        //Log.d("listtest",categorys.toString());

        for(String ctg : categorys) {
            ExpandingItem item = expandingList.createNewItem(R.layout.expanding_layout);
            ((TextView) item.findViewById(R.id.title)).setText(ctg);
            Cursor rez = myDb.getDrinksOf(ctg);
            item.createSubItems(rez.getCount());

            if(ctg.equals("Pivo")) {
                Log.d("colortest","usao sam ovde");
                item.setIndicatorColorRes(R.color.yellow);
                item.setIndicatorIconRes(R.drawable.beer_drink);
            }

            if(ctg.equals("Vino")) {
                Log.d("colortest","usao sam ovde");
                item.setIndicatorColorRes(R.color.red);
                item.setIndicatorIconRes(R.drawable.vine_dink);
            }

            if(ctg.equals("Zestina")) {
                Log.d("colortest","usao sam ovde");
                item.setIndicatorColorRes(R.color.white);
                item.setIndicatorIconRes(R.drawable.alcohol_drink);
            }

            if(ctg.equals("Sok")) {
                Log.d("colortest","usao sam ovde");
                item.setIndicatorColorRes(R.color.orange);
                item.setIndicatorIconRes(R.drawable.soda_dink);
            }

            int i = 0;
            while(rez.moveToNext()) {

                View v = item.getSubItemView(i);
                final String id = rez.getString(0);
                ((TextView) v.findViewById(R.id.sub_title)).setText(rez.getString(1));
                ((TextView) v.findViewById(R.id.sub_bulk)).setText(rez.getString(3) + "l");
                ((TextView) v.findViewById(R.id.sub_price_txtv)).setText(rez.getString(5) + "din");

                final TextView sub_quantity = v.findViewById(R.id.sub_quantity);

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
