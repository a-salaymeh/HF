package com.example.hf;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;


public class MainActivity extends AppCompatActivity implements SensorEventListener, StepListener{
    private Button menuButton;
    private Button dailyButton;
    private ProgressBar progressBar;
    private SensorManager sensorManager;
    private Sensor accel;
    private TextView userDistance;
    private int numSteps;
    private int GOAL_STEP = 100;
    private StepDetector simpleStepDetector;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.disProgressBar);
        progressBar.setProgress(0);
        userDistance = findViewById(R.id.userDistance );
        menuButton = findViewById(R.id.menuButton);



        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        numSteps = 0;
        sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);


        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, menuPage.class);
                startActivity(intent);
            }
        });




    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }
    @Override
    public void step(long timeNs) {
        numSteps++;
        Log.i ("timeNs",  timeNs +"-------------------------------") ;
        Log.i ("numSteps",  numSteps +"-------------------------------") ;

        float val = (float) numSteps/GOAL_STEP;
        progressBar.setProgress((int) Math.floor(100.0*(val)));
        userDistance.setText("@string/userDistance" + numSteps);

    }
}