package com.example.orderko;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DrinkListFragment extends Fragment {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listaDataHeader;
    private HashMap<String, List<String>> listHash;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_drink_list, container, false);

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
