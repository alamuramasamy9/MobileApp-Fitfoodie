package edu.northeastern.fitfoodie;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class Home extends AppCompatActivity {
    private Button workoutButton;
    private Button userProfileButton;
    private TextView quoteView;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_screen);

        quoteView = findViewById(R.id.quote);

        if (savedInstanceState != null) {
            quoteView.setText(savedInstanceState.getString("quote"));
            username = savedInstanceState.getString("currentUser");
        } else {
            quoteView.setText(fetchQuote());
            username = getIntent().getStringExtra("currentUser");
        }

        workoutButton = findViewById(R.id.workout_tracking);
        workoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, Workout.class);
            intent.putExtra("currentUser", username);
            startActivity(intent);
            finish();
        });

        userProfileButton = findViewById(R.id.user_profile);
        userProfileButton.setOnClickListener(v -> {
            Intent intent = new Intent(Home.this, UserProfile.class);
            intent.putExtra("currentUser", username);
            startActivity(intent);
            finish();
        });

        Button meal_tracker = findViewById(R.id.meal_tracking);
        Intent oldIntent = getIntent();
        String userid = oldIntent.getStringExtra("username");
        meal_tracker.setOnClickListener(view -> {
            Intent new_intent = new Intent(Home.this, DietOne.class);
            new_intent.putExtra("username", userid);
            startActivity(new_intent);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure you want to log out?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Perform logout action here
                Intent intent = new Intent(Home.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Dismiss the dialog
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("quote", quoteView.getText().toString());
        outState.putString("currentUser", username);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        quoteView.setText(savedInstanceState.getString("quote"));
        username = savedInstanceState.getString("currentUser");
    }

    private String fetchQuote() {
        String csv = "💪 Exercise is a celebration of what your body can do, not a punishment for what you ate. 🏋️‍♀️🍎🙌," +
                "🔥 You are stronger than you think. 💪🌟🚀," +
                "🏆 The only limit to our realization of tomorrow will be our doubts of today. 🌅🙏✨," +
                "💥 Fall in love with the process and the results will come. 🏃‍♂️💦💪," +
                "👊 You don't have to be great to start, but you have to start to be great. 🌟🚀🙌," +
                "🌟 Don't stop when you're tired, stop when you're done. 💪🏅😎," +
                "🌈 Strive for progress, not perfection. 🌟👍👏," +
                "💯 Fitness is not a destination, it's a journey. 🚶‍♀️🏋️‍♂️🌟," +
                "🌞 Your body can stand almost anything, it's your mind that you have to convince. 🧠💪🌟," +
                "🔥 The only bad workout is the one that didn't happen. 🙌💦💪";
        String[] array = csv.split(",");
        int randomIndex = (int) (Math.random() * array.length);
        return array[randomIndex];
    }
}
