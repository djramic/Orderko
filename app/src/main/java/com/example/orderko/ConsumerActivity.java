package com.example.orderko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.data.DataBufferObserver;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public class ConsumerActivity extends AppCompatActivity{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private BottomNavigationView bottomNavigationView;
    private Drink drink;
    private List<Drink> drinks = new ArrayList<>();
    private String club_id;
    private User user;
    private UserDatabaseHelper userDb;
    private TextView user_bill_txtv;
    private Button leave_club_button;
    private TextView table_bill_txtv;
    private FirebaseDatabase database;
    private DatabaseReference tableRef;
    private TableFragment table_fragment;
    private DrinkListFragment drinkListFragment;
    private BillFragment billFragment;
    private TextView club_info;
    private OrdersDatabaseHelper ordersDb;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);
        user = User.getInstance();
        userDb = new UserDatabaseHelper(ConsumerActivity.this);
        database = FirebaseDatabase.getInstance();
        tableRef = database.getReference(user.getClub() + "/tables");

        ordersDb = new OrdersDatabaseHelper(this);
        table_fragment = new TableFragment();
        drinkListFragment = new DrinkListFragment();
        billFragment = new BillFragment();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        user_bill_txtv = findViewById(R.id.user_all_bill);
        leave_club_button = findViewById(R.id.leave_table_but);
        table_bill_txtv = findViewById(R.id.table_bill);
        club_info = findViewById(R.id.club_info_txtv);

        Cursor cur = userDb.getData();
        while(cur.moveToNext()) {
            String table_number = cur.getString(3);
                user.setTable(table_number);
                user.setUserBill(cur.getString(2));
                user.setUserLastBill(cur.getString(4));
        }

        updateBill();
        drinkListFragment.clearDrinkList();

        if(user.getUserBill() != null) {
            user_bill_txtv.setText(user.getUserBill());
        }
        else {
            user_bill_txtv.setText("0");
        }

        /*if(user.getUserLastBill() != null) {
            user_last_bill.setText(user.getUserLastBill());
        }
        else {
            user_last_bill.setText("0");
        }*/

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contaner, new TableFragment())
                .commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        switch (item.getItemId()){
                            case R.id.nav_table:
                                selectedFragment = table_fragment;
                                break;
                            case R.id.nav_drink_list:
                                selectedFragment = drinkListFragment;
                                break;
                            case R.id.nav_bill:
                                selectedFragment = billFragment;
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

                user.setUserBill("0");
                user.setUserLastBill("0");
                user.setTable(null);
                user.setClub(null);

                userDb.insertData("0","0",null,"0",null, "0");
                ordersDb.clearTable();
                startActivity(new Intent(ConsumerActivity.this,MainActivity.class));
            }
        });


    }

    public void refreshFragment(){
        Cursor cur = userDb.getData();
        updateBill();
        Fragment selectedFragment = null;
        selectedFragment = new DrinkListFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contaner,selectedFragment)
                .commit();
    }

    public void updateBill() {
        if(user.getUserBill() != null) {
            user_bill_txtv.setText(user.getUserBill() + "din");
        }
        if(user.getTableBill() != null) {
            table_bill_txtv.setText(user.getTableBill() + "din");
        }

        club_info.setText("Club " + user.getClub());
        if(user.getTable() != null) {
            club_info.setText("Club " + user.getClub() + " sto broj: " + user.getTable());;
        }

    }




}
