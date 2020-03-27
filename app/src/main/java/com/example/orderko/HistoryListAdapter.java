package com.example.orderko;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class HistoryListAdapter extends BaseAdapter {
    Context context;
    private ArrayList<HistoryCard> cards;
    private ArrayAdapter<String> adapter;

    public HistoryListAdapter(Context context, ArrayList<HistoryCard> cards) {
        this.context = context;
        this.cards = cards;
    }

    @Override
    public int getCount() {
        return cards.size();
    }

    @Override
    public Object getItem(int position) {
        return cards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.history_card,parent,false);

        TextView club_txtv = convertView.findViewById(R.id.history_card_club_txtv);
        TextView date_txtv = convertView.findViewById(R.id.history_card_date_txtv);
        LinearLayout order_list = convertView.findViewById(R.id.history_card_list_view);
        TextView sum_txtv = convertView.findViewById(R.id.history_card_sum_price);

        String sum = cards.get(position).getSum();
        String club = cards.get(position).getClub();
        String date = cards.get(position).getDate_and_time();

        for(int i = 0; i < cards.get(position).getOrdersList().size(); i++ ){
            View vi = inflater.inflate(R.layout.history_list_view, null);
            TextView textView = vi.findViewById(R.id.history_list_txtv);
            textView.setText(cards.get(position).getOrdersList().get(i));
            order_list.addView(vi);
        }

        date_txtv.setText(date);
        club_txtv.setText(club);
        sum_txtv.setText(sum);

        return convertView;
    }
}
