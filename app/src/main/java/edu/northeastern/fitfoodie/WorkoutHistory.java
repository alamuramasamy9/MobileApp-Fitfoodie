package edu.northeastern.fitfoodie;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkoutHistory extends AppCompatActivity {
    private LinearLayout linearLayout;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_history);

        linearLayout = findViewById(R.id.linear_layout);
        List<WorkoutTracker> workoutObjects = fetchMyObjects(getIntent().getStringExtra("currentUser"));

        // create a TextView for each MyObject object, and add it to the linear layout
        for (WorkoutTracker workoutObject : workoutObjects) {
            TextView textViewType = new TextView(this);
            TextView textViewStartTime = new TextView(this);
            TextView textViewEndTime = new TextView(this);
            TextView textViewDistance = new TextView(this);
            TextView textViewAvgSpeed = new TextView(this);


            textViewType.setText(workoutObject.getType());
            textViewStartTime.setText("Start Time:"  + workoutObject.getStartTime());
            textViewEndTime.setText("End Time:"  + workoutObject.getEndTime());
            textViewDistance.setText("Distance: " + (int) workoutObject.getDistance());
            textViewAvgSpeed.setText("Average Speed: " + workoutObject.getType());

            linearLayout.addView(textViewType);
            linearLayout.addView(textViewStartTime);
            linearLayout.addView(textViewEndTime);
            linearLayout.addView(textViewDistance);
            linearLayout.addView(textViewAvgSpeed);
        }
    }

    private List<WorkoutTracker> fetchMyObjects(String username) {
        // implement this method to fetch a list of MyObject objects
        // for example, from a database
        List<WorkoutTracker> workoutList = new ArrayList<>();


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = databaseReference.child("Users").child(username).child("Workouts");

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve the user data as a User object
                WorkoutTracker wt = dataSnapshot.getValue(WorkoutTracker.class);
                for (WorkoutTracker workout : wt) {
                    if (user.getUsername().equals(username.getText().toString())) {
                        recordExists = true;
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });

        return workoutList;
    }
}
