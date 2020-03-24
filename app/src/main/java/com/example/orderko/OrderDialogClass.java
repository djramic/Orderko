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

import java.util.ArrayList;

public class OrderDialogClass extends Dialog{

    private Activity c;
    private Dialog d;
    private Button confirm;
    private DatabaseHelper myDb;
    private User user;
    public ListView list_view;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> orders_list = new ArrayList<>();

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

        confirm = (Button) findViewById(R.id.dialog_confirm_but);
        list_view = (ListView) findViewById(R.id.dialog_order_list);

        user = User.getInstance();
        orders_list.clear();
        int sum = 0;
        Cursor res = myDb.getOrder();
        while (res.moveToNext()) {
            Order order = new Order("", res.getString(1), res.getString(2), res.getString(4), res.getString(3), user.getTable(), res.getString(5));
            int quantitiy = Integer.parseInt(order.getQuantity());
            int price = Integer.parseInt(order.getPrice());
            sum = sum + (quantitiy * price);
            orders_list.add(order.getQuantity() +" x " + order.getName() + " = " + String.valueOf(price * quantitiy) + " din");
        }
        orders_list.add("UKUPNO: " + String.valueOf(sum) + " din");
        Log.d("dialogtest","order list " + orders_list.toString());
        if(list_view != null)
            list_view.setAdapter(adapter);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }


}
