package com.example.orderko;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrinkListFragment extends Fragment {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private DatabaseHelper myDb;
    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listaDataHeader;
    private HashMap<String, List<String>> listHash;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drink_list, container, false);

        myDb = new DatabaseHelper(getActivity());
        initData();
        listView = v.findViewById(R.id.expandable_list);
        listAdapter = new ExpandableListAdapter(getActivity(),listaDataHeader,listHash);
        listView.setAdapter(listAdapter);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                return true;
            }
        });

        return v;
    }

    private void initData() {

        Cursor res = myDb.getAllData();
        if(res.getCount() == 0){
            Log.d("database","Baza podataka je prazna!");
        }



        StringBuffer buffer = new StringBuffer();
        while (res.moveToNext()) {
            buffer.append("ID :" + res.getString(0) + "\n");
            buffer.append("Drink :" + res.getString(1) + "\n");
            buffer.append("Category :" + res.getString(2) + "\n");
            buffer.append("Bulk :" + res.getString(3) + "\n");
            buffer.append("Quantity :" + res.getString(4) + "\n\n");

        }

        Log.d("database", buffer.toString());



        listaDataHeader = new ArrayList<>();

        listaDataHeader.add("Pivo");
        listaDataHeader.add("Zestina");
        listaDataHeader.add("Sokovi");

        List<String> piva = new ArrayList<>();
        piva.add("jelen");
        piva.add("lav");
        piva.add("zajacarac");

        List<String> zestina = new ArrayList<>();
        zestina.add("vinjak");
        zestina.add("rakija");
        zestina.add("konjak");

        List<String> sokovi = new ArrayList<>();
        sokovi.add("kola");
        sokovi.add("sprajt");
        sokovi.add("jabuka");

        listHash = new HashMap<>();
        listHash.put(listaDataHeader.get(0), piva);
        listHash.put(listaDataHeader.get(1), zestina);
        listHash.put(listaDataHeader.get(2), sokovi);

    }
}
