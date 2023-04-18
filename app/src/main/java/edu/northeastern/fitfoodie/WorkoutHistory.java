package edu.northeastern.fitfoodie;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WorkoutHistory extends AppCompatActivity {
    private RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.workout_history);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String username = getIntent().getStringExtra("currentUser");

        fetchMyObjects(username, workoutList -> {
            System.out.println("Workouts Obtained: " + workoutList);
            // create a WorkoutAdapter with the workoutList, and set it as the RecyclerView's adapter
            WorkoutAdapter adapter = new WorkoutAdapter(workoutList);
            recyclerView.setAdapter(adapter);
        });
    }

    public interface WorkoutListCallback {
        void onWorkoutListFetched(List<WorkoutTracker> workoutList);
    }

    private void fetchMyObjects(String username, WorkoutListCallback callback) {
        // implement this method to fetch a list of MyObject objects
        // for example, from a database
        List<WorkoutTracker> workoutList = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference workoutRef = databaseReference.child("Users").child(username).child("Workouts");

        workoutRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve the workout data from firebase
                Iterable<DataSnapshot> workoutChildren = dataSnapshot.getChildren();

                for (DataSnapshot workoutSnapshot : workoutChildren) {
                    String workoutId = workoutSnapshot.getKey();
                    long timestamp = workoutSnapshot.child("workoutDate").child("time").getValue(Long.class);
                    double caloriesBurnt = workoutSnapshot.child("caloriesBurnt").getValue(Double.class);
                    double distance = workoutSnapshot.child("distance").getValue(Double.class);
                    String type = workoutSnapshot.child("type").getValue(String.class);
                    long duration = workoutSnapshot.child("workoutDuration").getValue(Long.class);
                    Date date = new Date(timestamp);
                    double averageSpeed = workoutSnapshot.child("averageSpeed").getValue(Double.class);

                    String startTime = workoutSnapshot.child("startTime").getValue(String.class);
                    String endTime = workoutSnapshot.child("endTime").getValue(String.class);

                    WorkoutTracker newWorkout = new WorkoutTracker(type, startTime, endTime, averageSpeed, distance, date, duration, caloriesBurnt);

                    workoutList.add(newWorkout);
                }

                // Call the callback method with the populated workoutList
                callback.onWorkoutListFetched(workoutList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }
}
