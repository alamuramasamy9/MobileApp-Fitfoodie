package edu.northeastern.fitfoodie;
import static android.content.ContentValues.TAG;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class SignUpOnClickListener extends SignUpActivity implements View.OnClickListener {

        SignUpOnClickListener(TextView username, TextView name, Spinner gender, Spinner goalType,
                              Spinner activityLevel, TextView age, TextView height,
                              TextView weight, TextView calorieInTakeTarget, ImageView profileImageView, Context mContext){
            this.username = username;
            this.name = name;
            this.gender = gender;
            this.goalType = goalType;
            this.activityLevel = activityLevel;
            this.age = age;
            this.height = height;
            this.weight = weight;
            this.calorieInTakeTarget = calorieInTakeTarget;
            this.mProfileImageView = profileImageView;
            this.mContext = mContext;
            //this.thisContext = t;
        }

        SignUpOnClickListener(){} //Empty Constructor required

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
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

                    // Get the Bitmap object from the ImageView
                    mProfileImageView.setDrawingCacheEnabled(true);
                    mProfileImageView.buildDrawingCache();
                    Bitmap bitmap = mProfileImageView.getDrawingCache();

                    // Save the bitmap to a file
                    String fileName = "image.jpg";
                    FileOutputStream outputStream = null;
                    try {
                        outputStream = mContext.openFileOutput("image.jpg", Context.MODE_PRIVATE);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    // Get the URI of the saved file
                    Uri selectedImageUri = Uri.fromFile(mContext.getFileStreamPath(fileName));

                    System.out.print("REACHED HERE, ABOUT TO CREATE NEW USER!");

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
                            .calorieInTakeTarget(parseFloat(calorieInTakeTarget.getText().toString()))
                            .workouts(new WorkoutTracker[]{}) //Empty workout initializer
                            .buildUser();

                    userList.add(newUser);

                    // TODO: Upload the image
                    // Get a reference to the Firebase Storage
                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference();

                    // Get a reference to the profile picture in Firebase Storage
                    StorageReference profilePicRef = storageRef.child("profile_pics/" + username.getText().toString());

                    // Upload the profile picture to Firebase Storage
                    profilePicRef.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> {
                        // Get the download URL of the uploaded profile picture
                        profilePicRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Add the download URL to the User object
                            newUser.setProfilePicUri(uri.toString());

                        });
                    }).addOnFailureListener(exception -> {
                        // Handle any errors that may occur while uploading the file
                        Log.e(TAG, "Error uploading file", exception);
                    });

                    // Add the new user to the database
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Users");
                    databaseReference.child(username.getText().toString()).setValue(newUser);

                    // Show a success message
                    Snackbar snack = Snackbar.make(v, "User Created!", Snackbar.LENGTH_LONG).setAction("Action", null);
                    View snackView = snack.getView();
                    TextView mTextView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
                    mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    snack.show();
                    new Handler().postDelayed(() -> {
                        Intent intent = new Intent(mContext, Home.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        // Add the username as an extra to the intent
                        intent.putExtra("currentUser", newUser.getUsername());
                        intent.putExtra("username", newUser.getUsername());
                        mContext.startActivity(intent);
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
