package com.example.orderko;

import android.app.Activity;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
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
    private UserDatabaseHelper userDb;

    public OrderDialogClass(Activity a ) {
        super(a);
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order_confirm_dialog);

        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, orders_list);
        myDb = new DatabaseHelper(getContext());
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference( "bello/orders");
        userDb = new UserDatabaseHelper(getContext());

        confirm = (Button) findViewById(R.id.dialog_confirm_but);
        list_view = (ListView) findViewById(R.id.dialog_order_list);
        sum_txtv = (TextView) findViewById(R.id.dialog_sum_txtv);
        cancel = findViewById(R.id.dialog_cancel_but);



        user = User.getInstance();
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

                user.setUserLastBill(String.valueOf(sum));
                userDb.updateLastBill(String.valueOf(sum));
                Cursor userRes = userDb.getData();
                String user_bill = "4";
                while(userRes.moveToNext()) {
                    user_bill= userRes.getString(2);
                    Log.d("userbill","Ukupni racun korisnika do sada" + user_bill);
                }
                int bill = Integer.parseInt(user_bill);
                if(userDb.updateBill(String.valueOf(sum + bill))){
                    Log.d("userbill","Uspesno sam stavio u db" + String.valueOf(sum + bill));
                }
                user.setUserBill(String.valueOf(sum + bill));


                Cursor read = userDb.getData();
                StringBuffer buffer = new StringBuffer();
                while (read.moveToNext()) {
                    Log.d("userdb", read.getString(0) +" "+ read.getString(1) +" "+ read.getString(2)+" "+ read.getString(3));
                }

                ConsumerActivity act = (ConsumerActivity)c;
                act.refreshFragment();


                Toast.makeText(getContext(),"Porudzbina je poslata", Toast.LENGTH_LONG).show();

                dismiss();


            }

        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }


}
