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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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
    private FirebaseFirestore db = FirebaseFirestore.getInstance();



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drink_list, container, false);

        Log.d("drinklist","Krejiram drink fregment");
        expandingList = v.findViewById(R.id.expanding_list_main);
        button = v.findViewById(R.id.order_but);

        user = User.getInstance();
        myDb = new DatabaseHelper(getActivity());

        if(drinks.size() == 0)
        {
            Log.d("drinklist","preuzimam novu listu");
            getDrinkList();
        }
        else {
            createDrinkList();
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void clearDrinkList(){
        Log.d("drinklist","cistim listu pica");
        drinks.clear();
    }

    public void createDrinkList() {
        Log.d("drinklist","cistim bazu sa picima....");
        myDb.clearTable();
        expandingList.removeAllViews();
        for(Drink drink : drinks) {
            boolean isInserted = myDb.insertData(drink.getName(),drink.getCategory(),drink.getBulk(),"0",drink.getPrice());
            if(!isInserted){
                Log.d("databasetest","Eroor while adding");
            }
        }

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

    public void getDrinkList() {
        db.collection("Clubs/" + user.getClubId() + "/Drink")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d("test", document.getId() + " => " + document.getData());
                                String name = document.getData().get("Name").toString();
                                String category = document.getData().get("Category").toString();
                                String bulk = document.getData().get("Bulk").toString();
                                String price = document.getData().get("Price").toString();
                                Log.w("firestoretest", "Naziv pica: " + name + ", Kategorija pica: " + category + ", Kolicina pica: " + bulk);
                                Drink drink = new Drink("0",name,category,bulk,"0", price);
                                drinks.add(drink);

                            }
                            createDrinkList();

                        } else {
                            Log.w("firestoretest", "Error getting documents.", task.getException());
                        }
                    }
                });
    }
}
