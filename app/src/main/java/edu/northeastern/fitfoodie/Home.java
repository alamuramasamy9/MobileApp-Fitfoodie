package edu.northeastern.fitfoodie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        Button workoutTracker = findViewById(R.id.workout_tracking);
        workoutTracker.setOnClickListener(view ->
        {
            Intent intent = new Intent(Home.this, Workout.class);
            startActivity(intent);
        });
    }
}
