package com.example.hf;

import android.content.Intent;
import android.os.Bundle;
//import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class menuPage extends AppCompatActivity {

    private TextView mTextView;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    Button exerButton;
    Button playListButton;
    Button logOutbutt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);

        exerButton = findViewById(R.id.workexer);

        playListButton = findViewById(R.id.playList);
        logOutbutt = findViewById(R.id.logoutBtn);


        exerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(menuPage.this, pageExercise.class);
                startActivity(intent2);
            }
        });
        playListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(menuPage.this, SpotifyPlayer.class);
                startActivity(intent2);
            }
        });

        logOutbutt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//              firebase
                FirebaseAuth.getInstance().signOut();
//               closes the application
                finishAffinity();
                Log.i("menupage","sign out button is pressed");
            }
        });





//        mTextView = (TextView) findViewById(R.id.text);
//        // Enables Always-on
//        setAmbientEnabled();
    }

}