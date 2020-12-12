package edu.ib.isotopes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int seconds;                //A field responsible for storing the amount of seconds
    private double mass = 0;            //A field responsible for storing the element's mass value
    private double halfLifePeriod;      //A field responsible for storing the element's half life period duration
    private double periodCounter = 0;   //A field responsible for storing the amount of periods that have passed since user pressed the start button

    private boolean running;            //A boolean value informing if the activity is currently running
    private boolean wasRunning = false; //A boolean value informing if the activity was running before some event happened (eg. device rotation)


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);         //Sets appropriate layout

        // If there are some values written in savedInstanceState, class fields are being initialized with them;
        if(savedInstanceState != null){
            seconds         = savedInstanceState.getInt     ("seconds");
            mass            = savedInstanceState.getDouble  ("mass");
            periodCounter   = savedInstanceState.getDouble  ("periodCounter");
            halfLifePeriod  = savedInstanceState.getDouble  ("halfLifePeriod");
            running         = savedInstanceState.getBoolean ("running");
            wasRunning      = savedInstanceState.getBoolean ("wasRunning");
        }
        runTimer(); //The runTimer(); method is being executed
    }

    @Override
    protected void onStart() {
        super.onStart();
        running = wasRunning;
    }

    private void runTimer(){
        //Mapping required layout TextViews to the Objects so I can operate on them
        final TextView timeView     = (TextView) findViewById(R.id.tvTime);
        final TextView massView     = (TextView) findViewById(R.id.tvMass);
        final TextView periodView   = (TextView) findViewById(R.id.tvPeriod);

        //Creating a new Handler executing Runnable every second
        final Handler handler       = new Handler(getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                //Transforming seconds into hh:mm:ss format
                int hours = seconds / 3600;
                int minutes = (seconds % 3600) / 60;
                int secs = seconds % 60;

                //A formatter for displaying time
                String time = String.format(Locale.getDefault(), "%d:%02d:%02d",hours,minutes,secs);
                //A formatter for displaying the element's mass
                NumberFormat formatter = new DecimalFormat("#########.######E0");
                String massString = formatter.format(mass);

                //Populating each TextView in layout with corresponding variable values.
                timeView.setText    ("Time: "   + time);
                massView.setText    ("Mass: "   + massString);
                periodView.setText  ("Period: " + periodCounter);

                //LogCat logs to check the correctness of my code
//                Log.d("lcznik", String.valueOf(seconds));
//                Log.d("masa",   String.valueOf(mass));
                if(running) {                             //If the activity is running at the moment
                    seconds++;                            //Seconds are being incremented each 1000ms
                    if (seconds % halfLifePeriod == 0) {  //If the number of seconds is equal to element's half life period
                        mass /= 2;                        //It's mass is being reduced by half
                        periodCounter++;                  //A number of periods elapsed is being incremented
                    }
                }
                handler.postDelayed(this, 1000);
            }
        });
    }


    public void onClickStart(View view) {
        //When the user clicks on 'Start' button, the EditTexts in layout are being referenced to.
        final EditText massInput    = (EditText) findViewById(R.id.mass);
        final EditText periodInput  = (EditText) findViewById(R.id.timePeriod);

        //If the current value of element's mass equals 0
        if (mass == 0){
            //It is being read from the EditText field
            mass = Double.parseDouble(massInput.getText().toString());}
        // the element's half life period is being read from the EditText field
        halfLifePeriod = Double.parseDouble(periodInput.getText().toString());
        running = true;     //running is set to true;
        wasRunning = true;  //was running is also set to true;

    }

    public void onClickStop(View view) {
        running = false;
        wasRunning = false;
    }

    public void onClickReset(View view) {
        running = false;
        wasRunning = false;
        seconds = 0;
        periodCounter = 0;
        final EditText massInput = (EditText) findViewById(R.id.mass);
        mass = Double.parseDouble(massInput.getText().toString());
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt       ("seconds",         seconds);
        savedInstanceState.putDouble    ("mass",            mass);
        savedInstanceState.putDouble    ("periodCounter",   periodCounter);
        savedInstanceState.putDouble    ("halfLifePeriod",  halfLifePeriod);
        savedInstanceState.putBoolean   ("running",         running);
        savedInstanceState.putBoolean   ("wasRunning",      wasRunning);
    }

}