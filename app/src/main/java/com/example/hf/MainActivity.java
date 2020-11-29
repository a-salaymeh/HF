package com.example.hf;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private static final long start_time_mili=600000;
    Spinner exc_Spinner;
    ImageView image;
    List<String> exc_list;
    TextView exc_name;
    TextView timer;
    Button btnStart_pause;
    Button btnReset;
    CountDownTimer count_down;
    private long milisecond = start_time_mili;
    boolean timeRunning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        exc_Spinner=findViewById(R.id.excSpinner);
        image=findViewById(R.id.excImg);
        timer=findViewById(R.id.downTimer);
        btnStart_pause=findViewById(R.id.startBtn);
        btnReset=findViewById(R.id.RestartBtn);
        exc_name=findViewById(R.id.excName);
        get_a_exc();
        btnStart_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (timeRunning){
                    stopTimer();
                }else{
                    startTimer();
                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time_Reset();
            }
        });
        updateTime();
    }



    public void startTimer(){
        count_down=new CountDownTimer(milisecond,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                milisecond=millisUntilFinished;
                updateTime();

            }

            @Override
            public void onFinish() {
                timeRunning=false;
                btnStart_pause.setText("START");
                btnStart_pause.setVisibility(View.INVISIBLE);
                btnReset.setVisibility(View.VISIBLE);

            }
        }.start();
        timeRunning=true;
        btnStart_pause.setText("PAUSE");
        btnReset.setVisibility(View.INVISIBLE);

    }
    public void stopTimer(){
        count_down.cancel();
        timeRunning=false;
        btnStart_pause.setText("START");
        btnReset.setVisibility(View.VISIBLE);

    }
    public void time_Reset(){
        milisecond=start_time_mili;
        updateTime();
        btnReset.setVisibility(View.INVISIBLE);
        btnStart_pause.setVisibility(View.VISIBLE);

    }
    public void updateTime(){
        int minute= (int) milisecond/ 60000;
        int second=(int) milisecond %60000/1000;
        String timeLeft=String.format(Locale.getDefault(),"%02d:%02d",minute,second);
        timer.setText(timeLeft);


    }
    public void get_a_exc() {

        // Get the list of Exc.
        exc_list = Arrays.asList(getResources().getStringArray(R.array.Exercises));

        // Make a handler for the exc list.
        exc_Spinner = findViewById(R.id.excSpinner);
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(
                        this, R.array.Exercises, android.R.layout.simple_spinner_dropdown_item);
        //  adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //  android.R.layout.simple_spinner_item
        exc_Spinner.setAdapter(adapter);
        exc_Spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView <?> adapterView, View view, int i, long l) {
                exc_name.setText(exc_list.get(i));

            }

            @Override
            public void onNothingSelected(AdapterView <?> adapterView) {

            }
        });
    }
}