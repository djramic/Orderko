package com.example.orderko;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList clubs = new ArrayList();
    private AutoCompleteTextView autoCompleteTextView;
    private Button choose_but;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        choose_but = findViewById(R.id.main_activity_choose_but);
        user = User.getInstance();
        initData();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,clubs);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);

        choose_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(clubs.contains(autoCompleteTextView.getText().toString())){
                    user.setClub(autoCompleteTextView.getText().toString());
                    startActivity(new Intent(MainActivity.this,ConsumerActivity.class));
                }
            }
        });

    }

    private void initData(){
        clubs.add("Bello");
        clubs.add("Rama bar");
        clubs.add("Polemika");
    }

}
