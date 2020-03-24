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
                String users_numbs = "0";

                if(tables.size() > 0) {

                    for (Table t : tables) {
                        String table_numb = t.getTable_number();
                        table_search.add(table_numb);
                    }
                    if (table_search.contains(String.valueOf(table_nubmer))) {
                        if(user.getTable() != null) {
                            if (!user.getTable().equals(String.valueOf(table_nubmer))) {   // KAD BEZ STOLA UDJEM U ZAUZET CRASH
                                taken_visibility(View.VISIBLE);
                                Toast.makeText(getActivity(), "Sto je već zauzet", Toast.LENGTH_LONG).show();
                            }
                        }else{
                            taken_visibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "Sto je već zauzet", Toast.LENGTH_LONG).show();
                        }

                    }
                    else {
                        if(user.getTable() != null) {
                            leave_table(getUsersNumber(user.getTable()));
                        }
                        taken_visibility(View.INVISIBLE);
                        users_numbs = getUsersNumber(String.valueOf(table_nubmer));
                        take_table(String.valueOf(table_nubmer),users_numbs);
                    }


                }else {
                    if(user.getTable() != null) {
                        leave_table(getUsersNumber(user.getTable()));
                    }
                    taken_visibility(View.INVISIBLE);
                    users_numbs = getUsersNumber(String.valueOf(table_nubmer));
                    take_table(String.valueOf(table_nubmer),users_numbs);
                }

                confirm_password.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int table_n = numberPicker.getValue();
                        String table_num = String.valueOf(table_n);
                        String password = password_edtx.getText().toString();

                        for(Table t : tables){
                            if(t.getTable_number().equals(table_num)) {
                                //Log.d("tables","Nasao sto "+ table_num +  " i " + t.getTable_number());
                                if(t.getPassword().equals(password)){
                                    if(user.getTable() != null) {
                                        leave_table(getUsersNumber(user.getTable()));
                                    }
                                    //Log.d("tables","Nasao sto "+ table_num +  " i " + t.getTable_number());
                                    String users_numbs = getUsersNumber(String.valueOf(String.valueOf(table_num)));
                                    take_table(String.valueOf(table_num),users_numbs);

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
                    leave_table(getUsersNumber(user.getTable()));
                }
            }
        });

        return v;
    }

    private void take_table(String table_number, String table_users) {

        int table_usrs = Integer.parseInt(table_users);
        int random = new Random().nextInt((8999) + 1000);
        user.setPassword(String.valueOf(random));
        Table table = new Table(table_number, table_number, String.valueOf(random),String.valueOf(table_usrs+1));
        tableRef.child(table_number).setValue(table);
        table_num_txvw.setText(table_number);
        user.setTable(table_number);
        userDb.insertData("0","mAuth.getCurrentUser().getEmail()",table_number);
        Toast.makeText(getContext(),"Sifra za pristup stolu: " + String.valueOf(random),Toast.LENGTH_LONG).show();
        //tableRef.child(id).child("users").child(mAuth.getCurrentUser().getUid()).setValue(tableUser);
    }

    private void leave_table(String users_numb) {
        Log.d("tables", "Vratio sam korisnika :" + users_numb);
        int usrs_numb = Integer.parseInt(users_numb);
        if(users_numb.equals("0") || users_numb.equals("1")) { // TO - DO
            tableDelRef = FirebaseDatabase.getInstance().getReference().child("bello").child("tables").child(user.getTable());
            tableDelRef.removeValue();
            table_num_txvw.setText("Nemaš sto");
            user.setTable(null);
            userDb.clearTable();
        }else {
            Log.d("leve", "napustam stoo" + user.getTable());
            String table = user.getTable();
            tableDelRef = FirebaseDatabase.getInstance().getReference().child("bello").child("tables").child(table);
            tableDelRef.removeValue();
            Table t = new Table(user.getTable(), user.getTable(), user.getPassword(),String.valueOf(usrs_numb -1));
            tableRef.child(user.getTable()).setValue(t);
            user.setTable(null);
            userDb.clearTable();
            table_num_txvw.setText("Nemaš sto");
            Log.d("tables", "usao sam u leave_table,  user ima vrednost:" + user.getTable());
        }
    }

    private String getUsersNumber(String table_numb){
        ArrayList<String> table_search = new ArrayList<>();
        ArrayList<String> users_numbs_list = new ArrayList<>();

        for (Table t : tables) {
            table_search.add(t.getTable_number());
            users_numbs_list.add(t.getUsers_num());
        }

        if (table_search.contains(table_numb)) {
            return users_numbs_list.get( table_search.indexOf(table_numb));
        } else {
            return "0";
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("tables","usao sam u onStart,  user ima vrednost:" + user.getTable() );

        if(user.getTable() != null) {
            table_num_txvw.setText(user.getTable());
        }
        else {
            table_num_txvw.setText("Nemas sto");
        }


    }

    private void taken_visibility(int visible){
        table_taken_txtv.setVisibility(visible);
        password_edtx.setVisibility(visible);
        confirm_password.setVisibility(visible);
    }


}
