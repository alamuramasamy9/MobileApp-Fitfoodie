package edu.northeastern.fitfoodie;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

import java.util.Calendar;

public class Running extends RunningVariables implements LocationListener {

    private String currentUser;

    private CountDownTimer timer;

    @SuppressLint("DefaultLocale")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running);

        ImageView runningGif = findViewById(R.id.running_gif);
        Glide.with(this).asGif().load(R.drawable.runningicon).into(runningGif);

        currentUser = getIntent().getStringExtra("currentUser");
        resetButton = findViewById(R.id.reset_attribute);

        distanceView = findViewById(R.id.distance_attribute);
        timeView = findViewById(R.id.time_attribute);
        averageSpeedView = findViewById(R.id.average_attribute);
        endRunning = findViewById(R.id.end_attribute);
        endRunning.setOnClickListener(new InnerEndRunningListener());
        Log.println(Log.ASSERT, "ST", String.valueOf(startTime));

        workoutObject = new WorkoutTracker("RUNNING", String.valueOf(startTime), String.valueOf(endTime), avgSpeed, distance, workoutDate);

        endRunning.setOnClickListener(new InnerEndRunningListener());

        if (savedInstanceState != null) {
            distance = savedInstanceState.getDouble(distance_key);
            distanceView.setText(String.format("Distance Covered: %.2f kms", ConvertToKM(distance)));
        }

        resetButton.setOnClickListener(v -> {
            distance = 0.0;
            distanceView.setText("Distance Covered: 0.00 kms");
            timeView.setText("Time Spent: 00:00:00");
            averageSpeedView.setText(String.format("Average Speed: %.2f M/H", 0.0));
        });
        timeView.setText("Time Spent: 00:00:00");
        startTimer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, LOCATION_PERMISSIONS, REQUEST_LOCATION_PERMISSION);
        } else {
            locationSettings();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationSettings();
            } else {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Denied")
                        .setMessage("This app needs location permission to track your running activity")
                        .setPositiveButton("OK", (dialog, which) -> finish())
                        .show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outputState){
        super.onSaveInstanceState(outputState);
        outputState.putDouble(distance_key, distance);
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Exit Pressed")
                .setMessage("Current Running Session will be marked completed. Are you sure?")
                .setPositiveButton("Yes", ((dialog, which) -> new InnerEndRunningListener()))
                .setNegativeButton("No", null)
                .show();
    }

    private void locationSettings() {
        System.out.println("SHOULD START TRACKING LOCATION UPDATES!");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (pointLocation != null) {
            long timeDiff = (location.getTime() - pointLocation.getTime()) / 1000; // in seconds
            double distanceInMiles = ConvertToMiles(distance);
            avgSpeed = distanceInMiles / ((double) timeDiff / 3600);
            UpdateObject();
        }
        pointLocation = location;
        distanceView.setText(String.format("Distance Covered: %.2f kms", ConvertToKM(distance)));
        averageSpeedView.setText(String.format("Average Speed: %.2f M/H", avgSpeed));
    }

    private double ConvertToMiles(double distanceInMeters) {
        return distanceInMeters / 1609.344;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }


    private Boolean UpdateObject(){
        workoutObject.setEndTime(String.valueOf(Calendar.getInstance().getTime()));
        workoutObject.setDistance(ConvertToKM(distance));
        workoutObject.setAverageSpeed(getAverageSpeedInString());
        return true;
    }
    private double ConvertToKM(double distance){ return distance/100;}

    private String getTimeDifference(){
        long sTInSecs = startTime.getTime()/1000;
        long eTInSecs = endTime.getTime()/1000;
        return calculateTime(sTInSecs, eTInSecs);
    }

    private String calculateTime(long sTInSecs, long eTInSecs){
        long stHours = sTInSecs/3600;
        long stMins = (sTInSecs%3600) / 60;
        long stSecs = sTInSecs%60;
        //String sT = stHours + ":" + stMins + ":" + stSecs;
        long etHours = eTInSecs/3600;
        long etMins = (eTInSecs%3600) / 60;
        long etSecs = eTInSecs%60;
        //String eT = etHours + ":" + etMins + ":" + etSecs;
        return String.valueOf(etHours-stHours) + ":"
                + String.valueOf(etMins - stMins) + ":"
                + String.valueOf(etSecs - stSecs);
    }

    private double getAverageSpeedInString(){
        long sTInSecs = startTime.getTime()/1000;
        long eTInSecs = endTime.getTime()/1000;
        return (distance/100) / (eTInSecs - sTInSecs);
    }

    public class InnerEndRunningListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            DatabaseReference userRef = databaseReference.child("Users").child(currentUser);

            //Add the workout tracker object to the user's workout's records. [TO DO]

            // Calculate calories burnt using a formula (example formula)
            double caloriesBurnt = distance * 0.63;


            //Update using Firebase Ref
        }
    }

    private void startTimer() {
        timer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            public void onTick(long millisUntilFinished) {
                long seconds = (millisUntilFinished / 1000) % 60;
                long minutes = (millisUntilFinished / (1000 * 60)) % 60;
                long hours = (millisUntilFinished / (1000 * 60 * 60)) % 24;
                String time = String.format("Time Spent: %02d:%02d:%02d", hours, minutes, seconds);
                timeView.setText(time);
            }
            public void onFinish() {
                // handle timer finish event
            }
        }.start();
    }
}

