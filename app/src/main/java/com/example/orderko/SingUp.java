package com.example.orderko;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SingUp extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button sing_up_but;
    private EditText name_edtx, email_edtx, password_edtx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sing_up);
        mAuth = FirebaseAuth.getInstance();

        name_edtx = findViewById(R.id.sing_up_name_edtx);
        email_edtx = findViewById(R.id.sing_up_email_edtx);
        password_edtx = findViewById(R.id.sing_up_password);
        sing_up_but = findViewById(R.id.sing_up_sing_up_button);

        sing_up_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email  = email_edtx.getText().toString();
                String password = password_edtx.getText().toString();
                Log.d("singup","email: " + email + " password: " + password);

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Toast.makeText(getApplicationContext(),"Nalog je uspesno napravljen",Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(SingUp.this,ConsumerActivity.class));
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Greska, pokušajte ponovo",Toast.LENGTH_LONG).show();

                                }
                            }
                        });
            }
        });
    }
}
