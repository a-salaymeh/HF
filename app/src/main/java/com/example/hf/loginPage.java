package com.example.hf;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class loginPage extends AppCompatActivity {

    private FirebaseAuth mAuth;
    EditText loginUsername, loginPassword;
    Button loginButton, registerButton;
    private FirebaseAuth userCheck;
    private FirebaseUser currentUser;
    private static final int RC_SIGN_IN = 1001;
    private GoogleSignInClient mGoogleSignInClient;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        loginUsername = findViewById(R.id.etLoginUsername);
        loginPassword = findViewById(R.id.etLoginPassword);
        loginButton = findViewById(R.id.btnLogin);
        registerButton = findViewById(R.id.btnLoginRegister);
        String username = loginUsername.getText().toString();
        Bundle user = new Bundle();
        user.putString("username", username);

        if (mAuth.getCurrentUser() != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }

        loginButton.setOnClickListener(v -> {
            if(v==loginButton){
                if (username.isEmpty() && loginPassword.getText().toString().isEmpty()) {
                    Toast.makeText(loginPage.this,"empty username and password",Toast.LENGTH_LONG).show();
                }else{
                    signIn(user);
                }
            }
        });

        registerButton.setOnClickListener(v -> {
            if(v==registerButton){
                startActivity(new Intent(this, register.class));
                finish();
            }
        });

        findViewById(R.id.google_login_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInToGoogle();
            }
        });

        configureGoogleClient();
    }


    public void signIn(Bundle user){
        mAuth.signInWithEmailAndPassword(loginUsername.getText().toString(), loginPassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    currentUser = mAuth.getCurrentUser();
                    finish();
                    Intent intent = new Intent(loginPage.this, MainActivity.class);
                    intent.putExtras(user);
                    startActivity(intent);
                }else{
                    Toast.makeText(loginPage.this, "couldn't login", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


//    gooogle authentication
    private void configureGoogleClient() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    public void signInToGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("loginPage", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
                Toast.makeText(loginPage.this, "Logged In", Toast.LENGTH_LONG).show();

            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("loginPage", "Google sign in failed", e);
                Toast.makeText(loginPage.this, "Error has happened check your Password or Email", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("loginPage", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("loginPage", "signInWithCredential:failure", task.getException());
                            Toast.makeText(loginPage.this, "didnt not pass the authentication", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            Log.d("LoginPage", "Currently Signed in: " + currentUser.getEmail());
            Toast.makeText(loginPage.this, "currently logged in: "+currentUser.getEmail(), Toast.LENGTH_LONG).show();
        }
    }


}