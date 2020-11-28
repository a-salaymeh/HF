package com.example.hf;

import android.content.Intent;
import android.os.Bundle;
//import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class menuPage extends AppCompatActivity {

    private TextView mTextView;
    Button exerButton;
    Button playListButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_page);

        exerButton = findViewById(R.id.workexer);

        playListButton = findViewById(R.id.playList);


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




//        mTextView = (TextView) findViewById(R.id.text);
//        // Enables Always-on
//        setAmbientEnabled();
    }
}