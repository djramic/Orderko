package com.example.orderko;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryListAdapter extends BaseAdapter {
    Context context;
    private ArrayList<HistoryCard> cards;

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
        String club = cards.get(position).getClub();

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(R.layout.history_card,parent,false);

        TextView club_txtv = convertView.findViewById(R.id.history_card_club_txtv);
        club_txtv.setText(club);

        return convertView;
    }
}
