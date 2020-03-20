package com.example.orderko;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TableFragment extends Fragment {
    private DatabaseHelper myDb;
    private NumberPicker numberPicker;
    private ImageButton pick_table_imbt;
    private TextView table_num_txvw;
    private User user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_table, container, false);

       user = User.getInstance();
       myDb = new DatabaseHelper(getActivity());
       numberPicker = v.findViewById(R.id.numberPicker);
       pick_table_imbt = v.findViewById(R.id.pick_table_imbt);
       table_num_txvw = v.findViewById(R.id.table_num_txvw);
       numberPicker.setMinValue(0);
       numberPicker.setMaxValue(10);

       if(user.getTable() != null){
           table_num_txvw.setText(user.getTable());
       }

       pick_table_imbt.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                int table_nubmer = numberPicker.getValue();
                table_num_txvw.setText(String.valueOf(table_nubmer));
                user.setTable(String.valueOf(table_nubmer));


           }
       });

        return v;
    }
}
