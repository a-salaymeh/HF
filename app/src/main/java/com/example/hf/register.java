package com.example.hf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    EditText email, password;
    Button buttonRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        email =findViewById(R.id.etRegisterEmail);
        password = findViewById(R.id.etRegisterPassword);
        buttonRegister = findViewById(R.id.btnRegister);

        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });


    }

    public void signup(){
        mAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "New user registration: " + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            Toast.makeText(register.this, "couldn't register", Toast.LENGTH_SHORT).show();
                        } else {
                            register.this.startActivity(new Intent(register.this, MainActivity.class));
                            register.this.finish();
                        }
                    }
                });
    }
}