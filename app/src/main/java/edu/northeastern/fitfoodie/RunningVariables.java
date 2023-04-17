package edu.northeastern.fitfoodie;

import android.Manifest;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RunningVariables extends AppCompatActivity {
    protected static final int REQUEST_LOCATION_PERMISSION = 1;
    protected static final String[] LOCATION_PERMISSIONS = {
            android.Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    LocationManager locationManager;
    Location pointLocation;
    WorkoutTracker workoutObject;
    TextView distanceView;
    TextView timeView;
    TextView averageSpeedView;
    Button endRunning;

    Button resetButton;

    double distance = 0.0F;
    //LocalTime startTime = LocalTime.now();
    Date startTime = Calendar.getInstance().getTime();
    //LocalTime endTime = LocalTime.now();
    Date endTime = Calendar.getInstance().getTime();
    double avgSpeed = 00.00;
    Date workoutDate = new Date();

    protected static final String distance_key = "distance";
    protected static final String timespent_key = "time";
    protected static final String averagespeed_key = "average";


    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
}
