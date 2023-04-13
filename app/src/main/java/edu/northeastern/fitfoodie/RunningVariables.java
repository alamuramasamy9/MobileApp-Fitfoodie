package edu.northeastern.fitfoodie;

import android.Manifest;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

    LocationManager locationManager;
    Location pointLocation;
    WorkoutTracker workoutObject;
    TextView distanceView;
    TextView timeView;
    TextView averageSpeedView;
    Button endRunning;
    double distance = 0.0F;
    String startTime = String.valueOf(Calendar.getInstance().getTime());
    String endTime = String.valueOf(Calendar.getInstance().getTime());
    double avgSpeed = 00.00;
    Date workoutDate = new Date();

    protected static final String distance_key = "distance";
    protected static final String timespent_key = "time";
    protected static final String averagespeed_key = "average";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
}
