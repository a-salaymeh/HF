package com.example.hf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class loginPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText loginUsername, loginPassword;
    Button loginButton, registerButton;
    private FirebaseAuth userCheck;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        loginUsername = findViewById(R.id.etLoginUsername);
        loginPassword = findViewById(R.id.etLoginPassword);
        loginButton = findViewById(R.id.btnLogin);
        registerButton = findViewById(R.id.btnLoginRegister);

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        loginButton.setOnClickListener(v -> {
            if(v==loginButton){
                signIn();
            }
        });
    }
    public void signIn(){
        mAuth.signInWithEmailAndPassword(loginUsername.getText().toString(), loginPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    currentUser = mAuth.getCurrentUser();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }else{
                    Toast.makeText(loginPage.this, "couldn't login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
//    public void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        updateUI(currentUser);
//    }


}