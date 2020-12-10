package com.example.hf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements SensorEventListener, StepListener, NavigationView.OnNavigationItemSelectedListener{
    private Button menuButton;
    private Button dailyButton;
    private ProgressBar progressBar;
    private SensorManager sensorManager;
    private Sensor accel;
    private TextView userDistance;
    private int numSteps;
    private int GOAL_STEP = 100;
    private StepDetector simpleStepDetector;
    private Button submitBtn;
    private ProgressBar foodBar;
    private ProgressBar waterBar;
    private EditText waterTxt;
    private EditText foodTxt;
    FirebaseFirestore db;
    String waterMl;
    String foodCal;
    SQLiteDatabase db2;
    dataBaseHelper dbHelper;



    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dbHelper = new dataBaseHelper(this);

        db2 = dbHelper.getWritableDatabase();

        db = FirebaseFirestore.getInstance();

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

//      submit
        submitBtn = findViewById(R.id.waternfoodbutton);
//        bar
        foodBar = findViewById(R.id.foodProgressBar);
        waterBar = findViewById(R.id.waterProgressBar);
//        text
        waterTxt = findViewById(R.id.userWater);
        foodTxt = findViewById(R.id.userF);



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                waterMl = waterTxt.getText().toString();
                foodCal = foodTxt.getText().toString();

                Map<String, Object> user = new HashMap<>();
                user.put("calories_intake", foodCal);
                user.put("water_intake", waterMl);


                db.collection("health_table")
                        .add(user)
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                Log.d("mainactivity", "DocumentSnapshot added with ID: " + documentReference.getId());
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("mainactivity", "error adding documents");
                            }
                        });



                ContentValues values = new ContentValues();
                values.put(dataBaseHelper.KEY_MESSAGE,  foodCal);
                db2.insert(dataBaseHelper.TABLE_NAME, null, values);


                waterTxt.setText("");
                foodTxt.setText("");
            }
        });


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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
        userDistance.setText(getString(R.string.steps) + numSteps);

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        //to prevent current item select over and over
        if (item.isChecked()){
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }

        if (id == R.id.nav_home) {
            // Handle the camera action
               startActivity(new Intent(getApplicationContext(), MainActivity.class));



        } else if (id == R.id.nav_Workout) {
            startActivity(new Intent(getApplicationContext(), pageExercise.class));
        } else if (id == R.id.nav_Player) {
            startActivity(new Intent(getApplicationContext(), SpotifyPlayer.class));
        } else if (id == R.id.nav_list) {
            //startActivity(new Intent(getApplicationContext(), ManageActivity.class));
            loadFragment();
        } else if (id == R.id.nav_logout) {
            //              firebase
            FirebaseAuth.getInstance().signOut();
//               closes the application
            finishAffinity();
//            startActivity(new Intent(getApplicationContext(), ShareActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    public void loadFragment() {
        // Begin the transaction
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        // Replace the contents of the container with the new fragment
        ft.replace(R.id.your_placeholder, new ListFrag());
        // or ft.add(R.id.your_placeholder, new FooFragment());
        // Complete the changes added above
        ft.commit();


    }
}