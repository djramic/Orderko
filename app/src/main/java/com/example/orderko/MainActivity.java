package com.example.orderko;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatAutoCompleteTextView;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.protobuf.DescriptorProtos;

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

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clubs);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);

        choose_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clubs.contains(autoCompleteTextView.getText().toString())) {
                    user.setClub(autoCompleteTextView.getText().toString());
                    startActivity(new Intent(MainActivity.this, ConsumerActivity.class));
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
