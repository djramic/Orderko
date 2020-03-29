package com.example.orderko;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TableFragment extends Fragment {
    private DatabaseHelper myDb;
    private UserDatabaseHelper userDb;
    private FirebaseDatabase database;
    private DatabaseReference tableRef, tableDelRef;
    private NumberPicker numberPicker;
    private ImageButton pick_table_imbt;
    private TextView table_num_txvw;
    private User user;
    private ArrayList<Table> tables = new ArrayList<>();
    private TextView  user_bill_txtv;
    private TextView table_bill_txtv;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_table, container, false);

        Log.d("tables","usao sam u onCreate");
       user = User.getInstance();
       myDb = new DatabaseHelper(getActivity());
       userDb = new UserDatabaseHelper(getActivity());
       numberPicker = v.findViewById(R.id.numberPicker);
       pick_table_imbt = v.findViewById(R.id.pick_table_imbt);
       table_num_txvw = v.findViewById(R.id.table_num_txvw);
       user_bill_txtv = getActivity().findViewById(R.id.user_all_bill);
       table_bill_txtv = getActivity().findViewById(R.id.table_bill);


       numberPicker.setMinValue(0);
       numberPicker.setMaxValue(10);

       database = FirebaseDatabase.getInstance();
       tableRef = database.getReference(user.getClub() + "/tables");

        tableRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tables.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                     Table t = data.getValue(Table.class);
                     tables.add(t);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("databasetest", "Failed to read value.");
            }
        });

        if(user.getTable() != null){
            table_num_txvw.setTextSize(60);
            table_num_txvw.setText(user.getTable());
        }

       pick_table_imbt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               String table_numb = String.valueOf(numberPicker.getValue());
               user.setTable(table_numb);
               userDb.insertData("0",user.getUserBill(),user.getTable(),user.getUserLastBill(),user.getClub(),"0");
               update();
           }
       });



        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("tables","usao sam u onStart,  user ima vrednost:" + user.getTable() );

        if(user.getTable() != null) {
            table_num_txvw.setTextSize(60);
            table_num_txvw.setText(user.getTable());
        }
        else {
            table_num_txvw.setTextSize(40);
            table_num_txvw.setText("Nemas sto");
        }


    }

    private void update(){
        if(user.getTable() != null){
            table_num_txvw.setText(user.getTable());
        }
    }




}
