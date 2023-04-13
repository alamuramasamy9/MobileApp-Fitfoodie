package edu.northeastern.fitfoodie;
import static android.content.ContentValues.TAG;

import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpOnClickListener extends AppCompatActivity implements View.OnClickListener {
        TextView username;
        TextView name;
        Spinner gender;
        Spinner goalType;
        Spinner activityLevel;

        TextView age;
        TextView height;
        TextView weight;
        TextView calorieInTakeTarget;
        //SignUpActivity thisContext;


        SignUpOnClickListener(TextView username, TextView name, Spinner gender, Spinner goalType,
                              Spinner activityLevel, TextView age, TextView height,
                              TextView weight, TextView calorieInTakeTarget){
            this.username = username;
            this.name = name;
            this.gender = gender;
            this.goalType = goalType;
            this.activityLevel = activityLevel;
            this.age = age;
            this.height = height;
            this.weight = weight;
            this.calorieInTakeTarget = calorieInTakeTarget;
            //this.thisContext = t;
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Map<String, User> users = new HashMap<>();
        DatabaseReference usersRef = databaseReference.child("Users");



    @Override
    public void onClick(View v) {
        if (username.getText().toString().isEmpty()) {
            resetData();
            Snackbar snack = Snackbar.make(v, "Please enter username!", Snackbar.LENGTH_LONG).setAction("Action", null);
            View snackView = snack.getView();
            TextView mTextView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
            mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            snack.show();
            return;
        }

        List<User> userList = new ArrayList<>();
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                System.out.println("ON DATA CHANGE CALLED! ENTERED Email: " + username.getText().toString());
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    System.out.println("Snapshot: " + snapshot.toString());
                    User user = snapshot.getValue(User.class);
                    System.out.println("USER RETRIEVED: " + user);
                    userList.add(user);
                }

                System.out.println("USER LIST RETRIEVED FROM FIREBASE: " + userList);

                boolean recordExists = false;
                for (User user : userList) {
                    if (user.getUsername().equals(username.getText().toString())) {
                        recordExists = true;
                        break;
                    }
                }

                // If the record exists, show an error message
                if (recordExists) {
                    Snackbar snack = Snackbar.make(v, "Account already exists with this Email!", Snackbar.LENGTH_LONG).setAction("Action", null);
                    View snackView = snack.getView();
                    TextView mTextView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
                    mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    snack.show();
                } else {
                    // Create a new record and add it to the list
                    User newUser = new UserBuilder()
                            .username(username.getText().toString())
                            .name(name.getText().toString())
                            .gender(gender.getSelectedItem().toString())
                            .goalType(goalType.getSelectedItem().toString())
                            .activityLevel(activityLevel.getSelectedItem().toString())
                            .age(parseInt(age.getText().toString()))
                            .height(parseInt(height.getText().toString()))
                            .weight(parseInt(weight.getText().toString()))
                            .calorieInTakeTarget(parseInt(calorieInTakeTarget.getText().toString()))
                            .workouts(new WorkoutTracker[]{}) //Empty workout initializer
                            .buildUser();

                    userList.add(newUser);

                    // Add the new record to the database
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    databaseReference.child(username.getText().toString()).setValue(newUser);

                    // Show a success message
                    Snackbar snack = Snackbar.make(v, "User Created!", Snackbar.LENGTH_LONG).setAction("Action", null);
                    View snackView = snack.getView();
                    TextView mTextView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
                    mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    snack.show();
                    new Handler().postDelayed(() -> {
                        //Intent myIntent = getIntent();
                        //Log.println(Log.ASSERT, TAG, thisContext.getPackageName());
                        Intent intent = new Intent(SignUpOnClickListener.this, Home.class);
                        startActivity(intent);
                    }, 1000); // 3000 milliseconds = 3 seconds
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });
    }

    private void resetData() {
        username.setText("");
    }
}
