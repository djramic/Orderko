package com.example.orderko;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderDialogClass extends Dialog{

    private Activity c;
    private Dialog d;
    private Button confirm;
    private Button cancel;
    private DatabaseHelper myDb;
    private User user;
    private ListView list_view;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> orders_list = new ArrayList<>();
    private TextView sum_txtv;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private DatabaseReference tableRef;
    private UserDatabaseHelper userDb;
    private OrdersDatabaseHelper ordersDb;

    public OrderDialogClass(Activity a ) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order_confirm_dialog);

        user = User.getInstance();
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, orders_list);
        myDb = new DatabaseHelper(getContext());
        ordersDb = new OrdersDatabaseHelper(getContext());
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference( user.getClub() + "/orders");
        tableRef = database.getReference( user.getClub() + "/tables");
        userDb = new UserDatabaseHelper(getContext());

        confirm = (Button) findViewById(R.id.dialog_confirm_but);
        list_view = (ListView) findViewById(R.id.dialog_order_list);
        sum_txtv = (TextView) findViewById(R.id.dialog_sum_txtv);
        cancel = findViewById(R.id.dialog_cancel_but);

        orders_list.clear();
        int sum = 0;
        Cursor res = myDb.getOrder();
        while (res.moveToNext()) {
            Order order = new Order("x", res.getString(1), res.getString(2), res.getString(4), res.getString(3), user.getTable(), res.getString(5));
            int quantitiy = Integer.parseInt(order.getQuantity());
            int price = Integer.parseInt(order.getPrice());
            sum = sum + (quantitiy * price);
            orders_list.add(order.getQuantity() +" x " + order.getName() + " = " + String.valueOf(price * quantitiy) + " din");
        }
        sum_txtv.setText("Ukupnuo: " + String.valueOf(sum) + " din");
        Log.d("dialogtest","order list " + orders_list.toString());

        if(list_view != null)
            list_view.setAdapter(adapter);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cursor res = myDb.getOrder();
                int sum = 0;
                while (res.moveToNext()) {
                    String id = myRef.push().getKey();
                    Order order = new Order(id, res.getString(1), res.getString(2), res.getString(4), res.getString(3), user.getTable(), res.getString(5));
                    myRef.child(id).setValue(order);
                    int quantitiy = Integer.parseInt(order.getQuantity());
                    int price = Integer.parseInt(order.getPrice());
                    sum = sum + (quantitiy * price);

                }

                Cursor userRes = userDb.getData();
                String user_bill = "0";
                while(userRes.moveToNext()) {
                    user_bill= userRes.getString(2);
                    Log.d("tablebill","Ukupni racun korisnika do sada" + user_bill);
                }
                int bill;
                if(user_bill != null) {
                    bill = Integer.parseInt(user_bill);
                }else{
                    bill = 0;
                }

                if(userDb.updateBill(String.valueOf(sum + bill))){
                    Log.d("tablebill","Uspesno sam stavio u db" + String.valueOf(sum + bill));
                }
                user.setUserBill(String.valueOf(bill + sum));
                userDb.updateLastBill(String.valueOf(bill + sum));

                String time_and_date = getTimeAndDate();
                res = myDb.getOrder();
                while(res.moveToNext()) {
                    boolean added = ordersDb.insertData(res.getString(1), res.getString(2), res.getString(3), res.getString(4)
                                        ,res.getString(5),user.getTable(),user.getClub(), time_and_date);
                    if(added){
                        Log.d("biltest","Stavio u orders db");
                    }else {
                        Log.d("biltest","Greska prilikom stavljanja u orders db");
                    }
                }


                Toast.makeText(getContext(),"Porudzbina je poslata", Toast.LENGTH_LONG).show();

                dismiss();

                ConsumerActivity act = (ConsumerActivity)c;
                act.refreshFragment();


            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    private String getTimeAndDate() {
        Date currentTime = Calendar.getInstance().getTime();
        String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        String time = String.valueOf(currentTime.getDate()) + "." + String.valueOf(currentTime.getMonth() + 1) + "." + year +
                                     "   " + currentTime.getHours()+ ":" + currentTime.getMinutes() + ":" +  currentTime.getSeconds();
        Log.d("biltest","Trenutno vreme: " + time);

        return time;
    }


}
