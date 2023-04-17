package edu.northeastern.fitfoodie;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Workout extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_tracker);

        String username = getIntent().getStringExtra("currentUser");

        Button runningWorkout = findViewById(R.id.running_workout);
        runningWorkout.setOnClickListener(view ->
        {
            Intent intent = new Intent(Workout.this, Running.class);
            intent.putExtra("currentUser", username);
            startActivity(intent);
        });

        Button cyclingWorkout = findViewById(R.id.cycling_workout);
        cyclingWorkout.setOnClickListener(view ->
        {
            Intent intent = new Intent(Workout.this, Cycling.class);
            intent.putExtra("currentUser", username);
            startActivity(intent);
        });

    }
}
