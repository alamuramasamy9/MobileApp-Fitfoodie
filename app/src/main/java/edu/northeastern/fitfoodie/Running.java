package edu.northeastern.fitfoodie;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Calendar;
import java.util.Date;

public class Running extends RunningVariables implements LocationListener {

    @SuppressLint("DefaultLocale")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.running);
        distanceView = findViewById(R.id.distance_attribute);
        timeView = findViewById(R.id.time_attribute);
        averageSpeedView = findViewById(R.id.average_attribute);
        endRunning = findViewById(R.id.end_attribute);
        endRunning.setOnClickListener(new InnerEndRunningListener());
        workoutObject = new WorkoutTracker("RUNNING", startTime, endTime, avgSpeed, distance, workoutDate);

        endRunning.setOnClickListener(new InnerEndRunningListener());

        if (savedInstanceState != null) {
            distance = savedInstanceState.getDouble(distance_key);
            distanceView.setText(String.format("Distance Covered: %.2f kms", ConvertToKM(distance)));
        }
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
                .setTitle("Back Requested")
                .setMessage("Activity will be marked as completed and will not resume.")
                .setPositiveButton("Yes", ((dialog, which) -> new InnerEndRunningListener()))
                .setNegativeButton("No", null)
                .show();
    }

    private void locationSettings() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
    }

//    private void requestGPS() {
//        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
//            }
//        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
//        final AlertDialog alertDialog = builder.create();
//        alertDialog.show();
//    }


    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(pointLocation!=null){
            distance+=pointLocation.distanceTo(location);
            endTime = String.valueOf(Calendar.getInstance().getTime());
            avgSpeed = 0;
            UpdateObject();
            //Implement averageSpeed = distance/(endTime-startTime);
        }
        distanceView.setText("Distance Covered: " + ConvertToKM(distance) + " kms");
        timeView.setText("Time Spent: 00:00:00");
        //Implement subtract time
        averageSpeedView.setText("Average Speed: 10.00 M/H");
        //Implement average in miles
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        locationManager.removeUpdates(this);
    }

    private double ConvertToKM(double distance){ return distance/100;}

    private Boolean UpdateObject(){
        workoutObject.setEndTime(endTime);
        workoutObject.setDistance(distance);
        workoutObject.setAverageSpeed(avgSpeed);
        return true;
    }

    public class InnerEndRunningListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //Fetch the user [TO DO]
            //Add the workout tracker object to the user's workout's records. [TO DO]

            //Update using Firebase Ref
        }
    }
}

