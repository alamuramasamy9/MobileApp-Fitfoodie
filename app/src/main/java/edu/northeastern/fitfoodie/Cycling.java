package edu.northeastern.fitfoodie;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Cycling extends CyclingVariables implements LocationListener {
    private String currentUser;

    private CountDownTimer timer;

    private long elapsedTime = 0;
    private boolean isTimerRunning = false;

    private double distance;

    private double averageSpeed;


    @SuppressLint("DefaultLocale")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cycling);

        ImageView cyclingGif = findViewById(R.id.cycling_gif);
        Glide.with(this).asGif().load(R.drawable.cycling).into(cyclingGif);

        currentUser = getIntent().getStringExtra("currentUser");
        resetButton = findViewById(R.id.reset_attribute);

        distanceView = findViewById(R.id.distance_attribute);
        timeView = findViewById(R.id.time_attribute);
        averageSpeedView = findViewById(R.id.average_attribute);
        endCycling = findViewById(R.id.end_attribute);
        endCycling.setOnClickListener(new InnerEndCyclingListener(this));
        Log.println(Log.ASSERT, "ST", String.valueOf(startTime));

        workoutObject = new WorkoutTracker("CYCLING", String.valueOf(startTime), String.valueOf(endTime), avgSpeed, distance, workoutDate, 0, 0);

        endCycling.setOnClickListener(new InnerEndCyclingListener(this));

        if (savedInstanceState != null) {
            distance = savedInstanceState.getDouble(distance_key);
            distanceView.setText(String.format("Distance Covered: %.2f m", ConvertToKM(distance)));
            averageSpeed = savedInstanceState.getDouble(averagespeed_key);
            averageSpeedView.setText(String.format("Average Speed: %.2f m/s", averageSpeed));
            elapsedTime = savedInstanceState.getLong(timespent_key);
            long seconds = (elapsedTime / 1000) % 60;
            long minutes = (elapsedTime / (1000 * 60)) % 60;
            long hours = (elapsedTime / (1000 * 60 * 60)) % 24;
            String time = String.format("Time Spent: %02d:%02d:%02d", hours, minutes, seconds);
            timeView.setText(time);
        }

        resetButton.setOnClickListener(v -> {
            distance = 0.0;
            elapsedTime = 0;
            distanceView.setText("Distance Covered: 0.00 m");
            timeView.setText("Time Spent: 00:00:00");
            averageSpeedView.setText(String.format("Average Speed: %.2f m/s", 0.0));
            startTimer();
        });
        timeView.setText("Time Spent: 00:00:00");
        startTimer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
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
                        .setMessage("This app needs location permission to track your cycling activity")
                        .setPositiveButton("OK", (dialog, which) -> finish())
                        .show();
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outputState){
        super.onSaveInstanceState(outputState);
        outputState.putDouble(distance_key, distance);
        outputState.putDouble(averagespeed_key, averageSpeed);
        outputState.putLong(timespent_key, elapsedTime);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        distance = savedInstanceState.getDouble(distance_key);
        elapsedTime = savedInstanceState.getLong(timespent_key);
        averageSpeed = savedInstanceState.getDouble(averagespeed_key);
    }

    @Override
    public void onBackPressed(){
        new AlertDialog.Builder(this)
                .setTitle("Exit Pressed")
                .setMessage("Current Cycling Session will be marked completed. Are you sure?")
                .setPositiveButton("Yes", ((dialog, which) -> {
                    InnerEndCyclingListener listener = new InnerEndCyclingListener(this);
                    listener.onClick(null); // pass a null view to onClick method
                }))
                .setNegativeButton("No", null)
                .show();
    }

    private void locationSettings() {
        System.out.println("SHOULD START TRACKING LOCATION UPDATES!");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Set distance threshold to 5 meter
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, this);

    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (pointLocation != null) {
            // Calculate distance traveled between previous and current locations
            float[] results = new float[1];
            Location.distanceBetween(pointLocation.getLatitude(), pointLocation.getLongitude(), location.getLatitude(), location.getLongitude(), results);
            float distanceTraveled = results[0];
            distance += distanceTraveled;

            // Calculate average speed in m/s
            long timeDiff = (location.getTime() - pointLocation.getTime()) / 1000; // in seconds
            double distanceInMeters = distance;
            double avgSpeedInMps = distanceInMeters / ((double) timeDiff);

            // Update object and distance view
            UpdateObject();
            distanceView.setText(String.format("Distance Covered: %.2f m", distance));
            averageSpeedView.setText(String.format("Average Speed: %.2f m/s", avgSpeedInMps));
        }
        pointLocation = location;
        // Add print statement to display location update
        System.out.println("Location update: " + location);
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
        workoutObject.setAverageSpeed(averageSpeed);
        return true;
    }
    private double ConvertToKM(double distance){ return distance/100;}

    private double getAverageSpeedInString(){
        long sTInSecs = startTime.getTime()/1000;
        long eTInSecs = endTime.getTime()/1000;
        return (distance/100) / (eTInSecs - sTInSecs);
    }

    public class InnerEndCyclingListener implements View.OnClickListener{

        private Context mContext;

        public InnerEndCyclingListener(Context context) {
            mContext = context;
        }

        @Override
        public void onClick(View v) {
            stopTimer();
            final double[] weight = new double[1];
            DatabaseReference userRef = databaseReference.child("Users").child(currentUser);
            userRef.child("weight").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    weight[0] = snapshot.getValue(Double.class);
                    // UPDATE WORKOUT OBJECT
                    workoutObject.setEndTime(String.valueOf(Calendar.getInstance().getTime()));
                    workoutObject.setWorkoutDuration(elapsedTime/1000);
                    workoutObject.setDistance(distance);

                    double caloriesBurnt = calculateCaloriesBurnt(elapsedTime/1000, avgSpeed, distance, weight[0]);
                    workoutObject.setCaloriesBurnt(caloriesBurnt);
                    workoutObject.setAverageSpeed(avgSpeed);

                    // Update using Firebase Ref
                    userRef.child("Workouts").push().setValue(workoutObject, (error, ref) -> {
                        if (error != null) {
                            // Handle error
                            Log.e(TAG, "Data could not be saved: " + error.getMessage());
                        } else {
                            // Handle success
                            Log.d(TAG, "Data saved successfully.");
                            Intent intent = new Intent(Cycling.this, Workout.class);
                            intent.putExtra("currentUser", currentUser);
                            startActivity(intent);
                            finish();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Handle error
                }
            });
        }
    }

    public static double calculateCaloriesBurnt(double durationInSeconds, double averageSpeedInMetersPerSecond, double distanceInMeters, double weightInKg) {
        // Convert average speed from m/s to km/h
        double averageSpeedInKmh = averageSpeedInMetersPerSecond * 3.6;

        // Calculate MET value based on average speed
        double metValue;
        if (averageSpeedInKmh <= 10.0) {
            metValue = 5.5;
        } else if (averageSpeedInKmh <= 16.0) {
            metValue = 8.0;
        } else if (averageSpeedInKmh <= 19.2) {
            metValue = 10.0;
        } else {
            metValue = 12.0;
        }

        // Calculate calories burnt based on duration, distance, and MET value
        double caloriesPerHour = metValue * weightInKg;
        double durationInHours = durationInSeconds / 3600.0;
        double distanceInKm = distanceInMeters / 1000.0;
        double caloriesBurnt = caloriesPerHour * durationInHours;

        // Adjust calories burnt based on distance
        if(distanceInKm != 0) {
            double caloriesPerKm = caloriesBurnt / distanceInKm;
            System.out.println("CALORIES PER KM CALC: " + caloriesPerKm);
            return caloriesPerKm * (distanceInMeters / 1000.0);
        }
        else {
            return 0;
        }
    }

    private void startTimer() {
        isTimerRunning = true;
        timer = new CountDownTimer(Long.MAX_VALUE, 1000) {
            public void onTick(long millisUntilFinished) {
                elapsedTime += 1000;
                long seconds = (elapsedTime / 1000) % 60;
                long minutes = (elapsedTime / (1000 * 60)) % 60;
                long hours = (elapsedTime / (1000 * 60 * 60)) % 24;
                String time = String.format("Time Spent: %02d:%02d:%02d", hours, minutes, seconds);
                timeView.setText(time);
            }
            public void onFinish() {
                // handle timer finish event
            }
        }.start();
    }

    private void stopTimer() {
        if (isTimerRunning) {
            isTimerRunning = false;
            timer.cancel();
        }
    }
}
