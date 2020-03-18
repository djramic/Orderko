package com.example.orderko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConsumerActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private BottomNavigationView bottomNavigationView;
    private ImageView avatar_imvw;
    private Drink drink;
    private List<Drink> drinks = new ArrayList<>();
    private String club_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);

        db.collection("Clubs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            String Club_name = "Bello";
                            for(QueryDocumentSnapshot document : task.getResult() ) {
                                if(Club_name.equals(document.get("Name"))) {
                                    club_id = document.getId();
                                    Log.w("firestoretest", "Nasao sam lokal.");
                                }
                                else {
                                    Log.w("firestoretest", "Lokal nije pronadjen.", task.getException());
                                }
                            }
                        }
                        else {
                            Log.w("firestoretest", "Error getting documents.", task.getException());
                        }
                        db.collection("Clubs/" + club_id + "/Drink")
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful()) {
                                            for (QueryDocumentSnapshot document : task.getResult()) {
                                                Log.d("test", document.getId() + " => " + document.getData());
                                                String name = document.getData().get("Name").toString();
                                                String category = document.getData().get("Category").toString();
                                                String bulk = document.getData().get("Bulk").toString();
                                                //Log.w("firestoretest", "Naziv pica: " + name + ", Kategorija pica: " + category + ", Kolicina pica: " + bulk);
                                                drink = new Drink(name,category,bulk);
                                                drinks.add(drink);

                                            }
                                        } else {
                                            Log.w("firestoretest", "Error getting documents.", task.getException());
                                        }
                                    }
                                });
                    }
                });





        avatar_imvw = findViewById(R.id.avatar_imvw);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        avatar_imvw.setImageResource(R.drawable.avatar);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contaner, new TableFragment())
                .commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Fragment selectedFragment = null;

                        switch (item.getItemId()){
                            case R.id.nav_table:
                                selectedFragment = new TableFragment();
                                break;
                            case R.id.nav_drink_list:
                                selectedFragment = new DrinkListFragment();
                                Bundle args = new Bundle();
                                args.putSerializable("DrinkList", (Serializable)drinks);
                                selectedFragment.setArguments(args);
                                break;
                            case R.id.nav_bill:
                                selectedFragment = new BillFragment();
                                break;

                        }

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_contaner, selectedFragment)
                                .commit();
                        return true;
                    }
                }
        );
    }
}
