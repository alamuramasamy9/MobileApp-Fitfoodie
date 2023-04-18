package edu.northeastern.fitfoodie;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    private Button workoutButton;
    private Button userProfileButton;
    private TextView quoteView;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        quoteView = findViewById(R.id.quote);
        quoteView.setText(fetchQuote());


        String username = getIntent().getStringExtra("currentUser");

        workoutButton = findViewById(R.id.workout_tracking);
        workoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Workout.class);
            intent.putExtra("currentUser", username);
            startActivity(intent);
        });

        userProfileButton = findViewById(R.id.user_profile);
        userProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, UserProfile.class);
            intent.putExtra("currentUser", username);
            startActivity(intent);
        });


    }

    private String fetchQuote() {
        String csv = "A little progress each day adds up to BIG results.," +
                "The journey to stronger muscles passes through sore muscles.," +
                "Physical fitness and mental fitness compliment each other. Your body is psychosomatic.," +
                "If you keep good food in your fridge, you will eat good food.";
        String[] array = csv.split(",");
        int randomIndex = (int) (Math.random() * array.length);
        return array[randomIndex];
    }
}
