package com.example.orderko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConsumerActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private BottomNavigationView bottomNavigationView;
    private Drink drink;
    private List<Drink> drinks = new ArrayList<>();
    private String club_id;
    private User user;
    private UserDatabaseHelper userDb;
    private TextView user_bill_txtv;
    private TextView user_last_bill;
    private Button leave_club_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);
        user = User.getInstance();
        userDb = new UserDatabaseHelper(ConsumerActivity.this);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        user_bill_txtv = findViewById(R.id.user_all_bill);
        user_last_bill = findViewById(R.id.user_last_bill_txvw);
        leave_club_button = findViewById(R.id.leave_table_but);


        db.collection("Clubs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            String Club_name = user.getClub();
                            for(QueryDocumentSnapshot document : task.getResult() ) {
                                Log.w("firestoretest", "poredim " + user.getClub() + " sa " + document.get("Name"));
                                if(Club_name.equals(document.get("Name"))) {
                                    club_id = document.getId();
                                    Log.w("firestoretest", "Nasao sam lokal.");
                                }
                                else {
                                    Log.w("firestoretest", "Lokal nije pronadjen.", task.getException());
                                }
                            }
                        }
                        else {
                            Log.w("firestoretest", "Error getting documents.", task.getException());
                        }
                        db.collection("Clubs/" + club_id + "/Drink")
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
                                                //Log.w("firestoretest", "Naziv pica: " + name + ", Kategorija pica: " + category + ", Kolicina pica: " + bulk);
                                                drink = new Drink("0",name,category,bulk,"0", price);
                                                drinks.add(drink);

                                            }
                                        } else {
                                            Log.w("firestoretest", "Error getting documents.", task.getException());
                                        }
                                    }
                                });
                    }
                });

        Cursor cur = userDb.getData();
        while(cur.moveToNext()) {
            String table_number = cur.getString(3);
                user.setTable(table_number);
                user.setUserBill(cur.getString(2));
                user.setUserLastBill(cur.getString(4));
        }

        if(user.getUserBill() != null) {
            user_bill_txtv.setText(user.getUserBill());
        }
        else {
            user_bill_txtv.setText("0");
        }

        if(user.getUserLastBill() != null) {
            user_last_bill.setText(user.getUserLastBill());
        }
        else {
            user_last_bill.setText("0");
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contaner, new TableFragment())
                .commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        switch (item.getItemId()){
                            case R.id.nav_table:
                                selectedFragment = new TableFragment();
                                break;
                            case R.id.nav_drink_list:
                                selectedFragment = new DrinkListFragment();
                                Bundle args = new Bundle();
                                args.putSerializable("DrinkList", (Serializable)drinks);
                                args.putSerializable("Club", "bello");
                                selectedFragment.setArguments(args);
                                break;
                            case R.id.nav_bill:
                                selectedFragment = new BillFragment();
                                break;

                        }

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contaner, selectedFragment)
                                .commit();
                        return true;
                    }
                }
        );

        leave_club_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getTable() != null) {
                    leave_table();
                }
                user.setUserBill("0");
                user.setUserLastBill("0");
                user.setTable(null);
                user.setClub(null);

                userDb.insertData("0","0",null,"0",null);
                startActivity(new Intent(ConsumerActivity.this,MainActivity.class));
            }
        });


    }

    private void leave_table(){
        Log.d("leave","napustam stoo" + user.getTable() );
        String table = user.getTable();
        DatabaseReference tableDelRef = FirebaseDatabase.getInstance().getReference().child(user.getClub()).child("tables").child(table);
        tableDelRef.removeValue();

    }

    public void refreshFragment(){
        Cursor cur = userDb.getData();
        updateBill();
        Fragment selectedFragment = null;
        selectedFragment = new DrinkListFragment();
        Bundle args = new Bundle();
        args.putSerializable("DrinkList", (Serializable)drinks);
        args.putSerializable("Club", "bello");
        selectedFragment.setArguments(args);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contaner,selectedFragment)
                .commit();
    }

    public void updateBill() {
        user_bill_txtv.setText(user.getUserBill() + "din");
        user_last_bill.setText(user.getUserLastBill() + "din");
    }

}
