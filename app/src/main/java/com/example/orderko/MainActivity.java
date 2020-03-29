package com.example.orderko;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.DescriptorProtos;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    private ArrayList clubs = new ArrayList();
    private ArrayList<String> clubs_id = new ArrayList();
    private AutoCompleteTextView autoCompleteTextView;
    private Button choose_but;
    private User user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        choose_but = findViewById(R.id.main_activity_choose_but);
        user = User.getInstance();
        initData();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, clubs);
        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.setAdapter(adapter);

        choose_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clubs.contains(autoCompleteTextView.getText().toString())) {
                    int index = clubs.indexOf(autoCompleteTextView.getText().toString());
                    user.setClubId(clubs_id.get(index));

                    startActivity(new Intent(MainActivity.this, ConsumerActivity.class));
                }
            }
        });
    }

    private void initData(){
        db.collection("Clubs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            String Club_name = user.getClub();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String club = document.get("Name").toString();
                                String club_id = document.getId();
                                clubs.add(club);
                                clubs_id.add(club_id);
                            }

                        } else {
                            Log.w("firestoretest", "Error getting documents.", task.getException());
                        }
                    }
                });
    }




}
