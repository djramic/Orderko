package com.example.orderko;

import android.animation.IntArrayEvaluator;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Collections;

public class BillFragment extends Fragment {
    private OrdersDatabaseHelper ordersDb;
    private ArrayList<HistoryOrder> h_orders = new ArrayList<>();
    private ArrayList<HistoryCard> h_cards = new ArrayList<>();
    private ListView history_card_list;
    private HistoryListAdapter h_adapter;
    private Button clear_history_but;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_bill, container, false);
        ordersDb = new OrdersDatabaseHelper(getContext());
        h_adapter = new HistoryListAdapter(getContext(),h_cards);

        history_card_list = v.findViewById(R.id.orders_history_list);
        clear_history_but = v.findViewById(R.id.bill_clear_history_but);

        h_orders.clear();
        h_cards.clear();

        Log.d("biltest", "Usao sam fregment bill");
        Cursor res = ordersDb.getAllData();
        StringBuffer asd = new StringBuffer();
        while (res.moveToNext()) {
            /*asd.append("ID :" + res.getString(0) + "\n");
            asd.append("Drink :" + res.getString(1) + "\n");
            asd.append("Category :" + res.getString(2) + "\n");
            asd.append("Bulk :" + res.getString(3) + "\n");
            asd.append("Quantity :" + res.getString(4) + "\n");
            asd.append("PRICE :" + res.getString(5) + "\n");
            asd.append("TABLE_NUMBER :" + res.getString(6) + "\n");
            asd.append("CLUB :" + res.getString(7) + "\n");
            asd.append("TIME_AND_DATE :" + res.getString(8) + "\n\n");*/
            HistoryOrder h_order = new HistoryOrder(res.getString(1),res.getString(2),res.getString(3),
                                            res.getString(4),res.getString(5),res.getString(6),
                                            res.getString(7), res.getString(8));
            h_orders.add(h_order);

        }

        ArrayList<String> dates = getDates();
        Collections.reverse(dates);
        for(String date : dates) {
            res = ordersDb.getDataOfDate(date);
            ArrayList<String> card_orders = new ArrayList<>();
            String club="none";
            String time = "none";
            int sum = 0;
            while(res.moveToNext()) {
                int quantity = Integer.parseInt(res.getString(4));
                int price = Integer.parseInt(res.getString(5));
                String order = res.getString(4) + " x " + res.getString(1) + " = " + String.valueOf(quantity * price);
                club = res.getString(7);
                time = res.getString(8);
                card_orders.add(order);
                sum = sum + (quantity * price);
            }

            HistoryCard h_card = new HistoryCard(card_orders,time,club,String.valueOf(sum));
            h_cards.add(h_card);
        }
        history_card_list.setAdapter(h_adapter);

        clear_history_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ordersDb.clearTable();
                h_cards.clear();
                history_card_list.setAdapter(h_adapter);
            }
        });


        return v;
    }

    private ArrayList<String> getDates() {
        ArrayList<String> dates = new ArrayList<>();
        for(HistoryOrder historyOrder : h_orders){
            if(!dates.contains(historyOrder.getTime_date())){
                dates.add(historyOrder.getTime_date());
            }
        }
        return dates;
    }


}
