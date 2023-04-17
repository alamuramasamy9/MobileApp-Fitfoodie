package edu.northeastern.fitfoodie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    private Button workoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        String username = getIntent().getStringExtra("currentUser");

        workoutButton = findViewById(R.id.workout_tracking);
        workoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Workout.class);
            intent.putExtra("currentUser", username);
            startActivity(intent);
        });


    }
}
