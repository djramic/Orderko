package com.example.orderko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ConsumerActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ImageView avatar_imvw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);

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
