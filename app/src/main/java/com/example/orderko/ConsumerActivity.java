package com.example.orderko;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

public class ConsumerActivity extends AppCompatActivity {

    private ImageView avatar_imvw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer);

        avatar_imvw = findViewById(R.id.avatar_imvw);
        avatar_imvw.setImageResource(R.drawable.avatar);
    }
}
