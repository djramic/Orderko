package com.example.orderko;

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
    private UserDatabaseHelper userDb;
    private FirebaseDatabase database;
    private DatabaseReference tableRef, tableDelRef;
    private NumberPicker numberPicker;
    private ImageButton pick_table_imbt;
    private TextView table_num_txvw;
    private User user;
    private ArrayList<Table> tables = new ArrayList<>();
    private FirebaseAuth mAuth;
    private TextView table_taken_txtv;
    private EditText password_edtx;
    private ImageButton confirm_password;
    private ImageButton leave_table_but;
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
       table_taken_txtv = v.findViewById(R.id.tableb_table_taken_txtv);
       password_edtx = v.findViewById(R.id.tablef_table_password_edtx);
       confirm_password = v.findViewById(R.id.tablef_confiram_pass_imbt);
       leave_table_but = v.findViewById(R.id.tablef_leave_table_but);


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
                     tables.add(t);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("databasetest", "Failed to read value.");
            }
        });

        if(user.getTable() != null){
            table_num_txvw.setText(user.getTable());
        }

       pick_table_imbt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                ArrayList<String> table_search = new ArrayList<>();
                int table_nubmer = numberPicker.getValue();

                if(tables.size() > 0) {


                    for (Table t : tables) {
                        String table_numb = t.getTable_number();
                        table_search.add(table_numb);
                    }
                    if (table_search.contains(String.valueOf(table_nubmer))) {
                        taken_visibility(View.VISIBLE);


                    }
                    else {
                        if(user.getTable() != null) {
                            leave_table();
                        }
                        taken_visibility(View.INVISIBLE);
                        take_table(String.valueOf(table_nubmer));
                    }


                }else {
                    if(user.getTable() != null) {
                        leave_table();
                    }
                    taken_visibility(View.INVISIBLE);
                    take_table(String.valueOf(table_nubmer));
                }

                confirm_password.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int table_n = numberPicker.getValue();
                        String table_num = String.valueOf(table_n);

                        for(Table t : tables){
                            if(t.getTable_number().equals(table_num)) {
                                //Log.d("tables","Nasao sto "+ table_num +  " i " + t.getTable_number());
                                if(t.getPassword().equals(password_edtx.getText().toString())){
                                    if(user.getTable() != null) {
                                        leave_table();
                                    }
                                    //Log.d("tables","Nasao sto "+ table_num +  " i " + t.getTable_number());
                                    take_table(String.valueOf(table_num));

                                }
                                else{
                                    Toast.makeText(getActivity(),"Pogrešna šifra", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    }
                });


           }
       });

        leave_table_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taken_visibility(View.INVISIBLE);
                if(user.getTable() != null) {
                    leave_table();
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
        userDb.insertData("0",mAuth.getCurrentUser().getEmail(),table_number);
        TableUser tableUser = new TableUser(mAuth.getCurrentUser().getUid(),mAuth.getCurrentUser().getEmail());
        Toast.makeText(getContext(),"Sifra za pristup stolu: " + String.valueOf(random),Toast.LENGTH_LONG).show();
        //tableRef.child(id).child("users").child(mAuth.getCurrentUser().getUid()).setValue(tableUser);
    }

    private void leave_table() {
        Log.d("leve","napustam stoo" + user.getTable() );
        String table = user.getTable();
        tableDelRef = FirebaseDatabase.getInstance().getReference().child("bello").child("tables").child(table);
        tableDelRef.removeValue();
        user.setTable(null);
        userDb.clearTable();
        table_num_txvw.setText("Nemaš sto");
        Log.d("tables","usao sam u leave_table,  user ima vrednost:" + user.getTable() );

    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        Log.d("tables","usao sam u onStart,  user ima vrednost:" + user.getTable() );

        if(user.getTable() != null) {
            table_num_txvw.setText(user.getTable());
        }
        else {
            table_num_txvw.setText("Nemas sto");
        }



        //updateUi();
    }

    private void taken_visibility(int visible){
        table_taken_txtv.setVisibility(visible);
        password_edtx.setVisibility(visible);
        confirm_password.setVisibility(visible);
    }

    private void updateUi() {

    }
}
