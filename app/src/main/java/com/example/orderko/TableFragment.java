package com.example.orderko;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseDatabase database;
    private DatabaseReference tableRef, tableDelRef;
    private NumberPicker numberPicker;
    private ImageButton pick_table_imbt;
    private TextView table_num_txvw;
    private User user;
    private ArrayList<Table> tables = new ArrayList<>();
    private FirebaseAuth mAuth;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_table, container, false);

       user = User.getInstance();
       myDb = new DatabaseHelper(getActivity());
       numberPicker = v.findViewById(R.id.numberPicker);



       mAuth = FirebaseAuth.getInstance();
       numberPicker.setMinValue(0);
       numberPicker.setMaxValue(10);

       database = FirebaseDatabase.getInstance();
       tableRef = database.getReference("/bello/tables");

        tableRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tables.clear();

                for (DataSnapshot data : dataSnapshot.getChildren()) {
                     Table t = data.getValue(Table.class);
                     Log.d("tables","U tables: ");
                     tables.add(t);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("databasetest", "Failed to read value.");
            }
        });

        pick_table_imbt = v.findViewById(R.id.pick_table_imbt);
        table_num_txvw = v.findViewById(R.id.table_num_txvw);


       if(user.getTable() != null){
           table_num_txvw.setText(user.getTable());
       }

       pick_table_imbt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                ArrayList<String> table_search = new ArrayList<>();
                int table_nubmer = numberPicker.getValue();


                if(tables.size() > 0) {

                    if(user.getTable() != null) {
                        leave_table();
                    }

                    for (Table t : tables) {
                        String table_numb = t.getTable_number();
                        table_search.add(table_numb);
                    }
                    if (table_search.contains(String.valueOf(table_nubmer))) {
                        Toast.makeText(getContext(),"Sto je zauzet",Toast.LENGTH_LONG).show();
                    }
                    else {
                        take_table(String.valueOf(table_nubmer));
                    }




                }else {
                    take_table(String.valueOf(table_nubmer));
                }






           }
       });

        return v;
    }

    private void take_table(String table_number) {

        int random = new Random().nextInt((8999) + 1000);
        user.setPassword(String.valueOf(random));
        Table table = new Table(table_number, table_number, String.valueOf(random));
        tableRef.child(table_number).setValue(table);
        table_num_txvw.setText(table_number);
        user.setTable(table_number);
       // TableUser tableUser = new TableUser(mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getEmail());
        //tableRef.child(id).child("users").child(mAuth.getCurrentUser().getUid()).setValue(tableUser);
    }

    private void leave_table() {
        Log.d("leve","napustam stoo" + user.getTable() );
        String table = user.getTable();
        tableDelRef = FirebaseDatabase.getInstance().getReference().child("bello").child("tables").child(table);
        tableDelRef.removeValue();

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //updateUi();
    }

    private void updateUi() {

    }
}
