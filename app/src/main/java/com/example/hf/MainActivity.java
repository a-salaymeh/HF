package com.example.hf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    Button menuButton;
    TextView userText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        menuButton = findViewById(R.id.menuButton);
        userText = findViewById(R.id.UsernameText);
        Intent intent2  = getIntent();
        String username = intent2.getStringExtra("username");
        Log.e("mainpage", username);
//        userText.setText(username);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, menuPage.class);
                startActivity(intent);
            }
        });


    }
}