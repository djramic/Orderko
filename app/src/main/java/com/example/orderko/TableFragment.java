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
    private TextView table_taken_txtv;
    private EditText password_edtx;
    private ImageButton confirm_password;
    private ImageButton leave_table_but;
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
       table_taken_txtv = v.findViewById(R.id.tableb_table_taken_txtv);
       password_edtx = v.findViewById(R.id.tablef_table_password_edtx);
       confirm_password = v.findViewById(R.id.tablef_confiram_pass_imbt);
       leave_table_but = v.findViewById(R.id.tablef_leave_table_but);
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
                user.setTableBill(getTableBill(user.getTable()));
                user.setUsersNum(getUsersNumber(user.getTable()));
                update();
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
               int table_num = numberPicker.getValue();
               if(user.getTable() != null) {
                   leave_table();
               }
               user.setUsersNum(getUsersNumber(String.valueOf(table_num)));
               take_table(String.valueOf(String.valueOf(table_num)),user.getUsersNum());

           }
       });

        leave_table_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getTable() != null) {
                    leave_table();
                }
            }
        });

        return v;
    }

    private void take_table(String table_number, String table_users) {
        Log.d("addtable","Zauzimam sto: " + table_number);
        user.setTableBill(getTableBill(table_number));
        int table_usrs = Integer.parseInt(table_users);
        int random = new Random().nextInt((8999) + 1000);
        user.setPassword(String.valueOf(random));
        Table table = new Table(table_number, table_number, String.valueOf(random),String.valueOf(table_usrs+1),user.getTableBill());
        tableRef.child(table_number).setValue(table);
        table_num_txvw.setTextSize(60);
        table_num_txvw.setText(table_number);
        user.setTable(table_number);
        userDb.insertData("0","0",table_number,"0",user.getClub(), user.getTableBill());
        user.setUserBill("0");
        user.setUserLastBill("0");
        Log.d("tablebill","Stavljam u user.tableBill: " + getTableBill(table_number) );
        user.setTableBill(getTableBill(table_number));
        ((ConsumerActivity) getActivity()).updateBill();
        Toast.makeText(getContext(),"Sifra za pristup stolu: " + String.valueOf(random),Toast.LENGTH_LONG).show();
    }

    private void leave_table() {
        Log.d("tables", "Vratio sam korisnika :" + user.getUsersNum());
        user.setUserBill("0");
        user.setUserLastBill("0");
        int usrs_numb = Integer.parseInt(user.getUsersNum());
        if(user.getUsersNum().equals("0") || user.getUsersNum().equals("1")) { // TO - DO
            tableDelRef = FirebaseDatabase.getInstance().getReference().child(user.getClub()).child("tables").child(user.getTable());
            tableDelRef.removeValue();
            table_num_txvw.setTextSize(40);
            table_num_txvw.setText("Nemaš sto");
            user.setTable(null);
            userDb.clearTable();
            ((ConsumerActivity) getActivity()).updateBill();
        }else {
            Log.d("leve", "napustam stoo" + user.getTable());
            String table = user.getTable();
            tableDelRef = FirebaseDatabase.getInstance().getReference().child(user.getClub()).child("tables").child(table);
            tableDelRef.removeValue();
            Table t = new Table(user.getTable(), user.getTable(), user.getPassword(),String.valueOf(usrs_numb -1),getTableBill(user.getTable()));
            tableRef.child(user.getTable()).setValue(t);
            user.setTable(null);
            userDb.clearTable();
            table_num_txvw.setTextSize(40);
            table_num_txvw.setText("Nemaš sto");
            ((ConsumerActivity) getActivity()).updateBill();
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

    private void update(){
        if(user.getUserBill() != null) {
            user_bill_txtv.setText(user.getUserBill() + "din");
        }
        if(user.getTableBill() != null) {
            table_bill_txtv.setText(user.getTableBill() + "din");
        }
    }

    private String getTableBill(String table_numb) {
        ArrayList<String> table_search = new ArrayList<>();
        ArrayList<String> table_bill_list = new ArrayList<>();

        for (Table t : tables) {
            table_search.add(t.getTable_number());
            table_bill_list.add(t.getTable_bill());
        }

        if (table_search.contains(table_numb)) {
            return table_bill_list.get(table_search.indexOf(table_numb));
        } else {
            return "0";
        }
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




}
