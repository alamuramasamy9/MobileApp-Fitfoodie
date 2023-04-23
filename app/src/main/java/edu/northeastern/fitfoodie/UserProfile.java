package edu.northeastern.fitfoodie;

import static android.content.ContentValues.TAG;

import static java.lang.Integer.parseInt;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserProfile extends AppCompatActivity {
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 2;


    private ImageView profileImageView;
    private Button uploadButton;
    Bitmap imageBitmap;
    Uri selectedImage;
    TextView username;
    TextView name;
    Spinner gender;
    Spinner goalType;
    Spinner activityLevel;

    TextView age;
    TextView height;
    TextView weight;
    TextView calorieInTakeTarget;

    protected Context mContext;
    protected ImageView mProfileImageView;

    ArrayAdapter<CharSequence> genderAdapter;
    ArrayAdapter<CharSequence> goalAdapter;
    ArrayAdapter<CharSequence> activityAdapter;

    // Get a reference to the Firebase Storage
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    StorageReference profilePicRef;
    //Keys for saving state
    private static final String username_key = "distance";
    private static final String name_key = "distance";
    private static final String age_key = "distance";
    private static final String height_key = "distance";
    private static final String weight_key = "distance";
    private static final String calorie_key = "distance";

    private static final String gender_spinner_position = "spinner_position";
    private static final String goal_spinner_position = "spinner_position";
    private static final String activity_spinner_position = "spinner_position";
    private static final String KEY_IMAGE_URI = "imageUri";
    private static final String KEY_IMAGE_BITMAP = "imageBitmap";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.user_profile);

        username = findViewById(R.id.user_profile_username_edit_view);
        name = findViewById(R.id.user_profile_name_edit_view);
        gender = findViewById(R.id.user_profile_gender_spinner);
        goalType = findViewById(R.id.user_profile_goal_spinner);
        activityLevel = findViewById(R.id.user_profile_activity_spinner);
        age = findViewById(R.id.user_profile_age_edit_view);
        height = findViewById(R.id.user_profile_height_edit_view);
        weight = findViewById(R.id.user_profile_weight_edit_view);
        calorieInTakeTarget = findViewById(R.id.user_profile_calorie_target_edit_view);
        profileImageView = findViewById(R.id.user_profile_profile_image_view);
        mContext = getApplicationContext();
        uploadButton = findViewById(R.id.user_profile_upload_button);
        mProfileImageView = profileImageView;

        Log.println(Log.ASSERT, "User", getIntent().getStringExtra("currentUser"));
        //Setting adapters for spinners
//        genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender_options, android.R.layout.simple_spinner_item);
//        goalAdapter = ArrayAdapter.createFromResource(this, R.array.goal_options, android.R.layout.simple_spinner_item);
//        activityAdapter = ArrayAdapter.createFromResource(this, R.array.activity_options, android.R.layout.simple_spinner_item);
//        gender.setAdapter(genderAdapter);
//        goalType.setAdapter(goalAdapter);
//        activityLevel.setAdapter(activityAdapter);
//
//        populateForm(getIntent().getStringExtra("currentUser"));
        username.setFocusable(false);
        username.setClickable(false);
        name.setFocusable(false);
        name.setClickable(false);

        if (saveInstanceState != null) {
            username.setText(saveInstanceState.getString("username_key"));
            name.setText(saveInstanceState.getString("name_key"));
            age.setText(saveInstanceState.getString("age_key"));
            height.setText(saveInstanceState.getString("height_key"));
            weight.setText(saveInstanceState.getString("weight_key"));
            calorieInTakeTarget.setText(saveInstanceState.getString("calorie_key"));
            gender.setSelection(saveInstanceState.getInt("gender_spinner_position"));
            goalType.setSelection(saveInstanceState.getInt("goal_spinner_position"));
            activityLevel.setSelection(saveInstanceState.getInt("activityLevel_spinner_position"));

            selectedImage = saveInstanceState.getParcelable(KEY_IMAGE_URI);
            imageBitmap = saveInstanceState.getParcelable(KEY_IMAGE_BITMAP);
            // Restore the selected image or taken picture
            if (selectedImage != null) {
                Glide.with(this).load(selectedImage).into(profileImageView);
            } else if (imageBitmap != null) {
                Glide.with(this).load(imageBitmap).into(profileImageView);
            }
            mProfileImageView = profileImageView;
        }
        else{
            Log.println(Log.ASSERT, "User", getIntent().getStringExtra("currentUser"));
            //Setting adapters for spinners
            genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender_options, android.R.layout.simple_spinner_item);
            goalAdapter = ArrayAdapter.createFromResource(this, R.array.goal_options, android.R.layout.simple_spinner_item);
            activityAdapter = ArrayAdapter.createFromResource(this, R.array.activity_options, android.R.layout.simple_spinner_item);
            gender.setAdapter(genderAdapter);
            goalType.setAdapter(goalAdapter);
            activityLevel.setAdapter(activityAdapter);

            populateForm(getIntent().getStringExtra("currentUser"));
        }

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if permission to access camera and storage is granted
                if (ContextCompat.checkSelfPermission(UserProfile.this, android.Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(UserProfile.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(UserProfile.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    // Request permission to access camera and storage
                    if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)
                            || shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                            || shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Explain why the app needs these permissions
                        AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
                        builder.setTitle("Permissions needed")
                                .setMessage("This app needs permission to access the camera and storage in order to upload your profile picture.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Request permission to access camera and storage
                                        ActivityCompat.requestPermissions(UserProfile.this, new String[]{
                                                android.Manifest.permission.CAMERA,
                                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                                android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
                                    }
                                });
                        builder.create().show();
                    } else {
                        // Request permission to access camera and storage
                        ActivityCompat.requestPermissions(UserProfile.this, new String[]{
                                android.Manifest.permission.CAMERA,
                                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
                    }
                } else {
                    // Permission is already granted
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserProfile.this);
                    builder.setTitle("Choose image source")
                            .setItems(new String[]{"Camera", "Gallery"}, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (which == 0) {
                                        dispatchTakePictureIntent();
                                    } else {
                                        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                        startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY);
                                    }
                                }
                            });
                    builder.create().show();
                }
            }
        });

        Button saveChangesButton = findViewById(R.id.user_profile_save_changes_button);

        saveChangesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference usersRef = databaseReference.child("Users").child(getIntent().getStringExtra("currentUser"));

                // Update user data
                usersRef.child("gender").setValue(gender.getSelectedItem());
                usersRef.child("activityLevel").setValue(activityLevel.getSelectedItem());
                usersRef.child("goalType").setValue(goalType.getSelectedItem());
                usersRef.child("age").setValue(parseInt(age.getText().toString()));
                usersRef.child("calorieInTakeTarget").setValue(parseInt(calorieInTakeTarget.getText().toString()));
                usersRef.child("height").setValue(parseInt(height.getText().toString()));
                usersRef.child("weight").setValue(parseInt(weight.getText().toString()));

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

                Uri selectedImageUri = Uri.fromFile(mContext.getFileStreamPath(fileName));

                // TODO: Upload the image

                // Get a reference to the profile picture in Firebase Storage
                profilePicRef = storageRef.child("profile_pics/" + username.getText().toString());

                // Upload the profile picture to Firebase Storage
                profilePicRef.putFile(selectedImageUri).addOnSuccessListener(taskSnapshot -> {
                    // Get the download URL of the uploaded profile picture
                    profilePicRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        // Add the download URL to the User object
                        usersRef.child("profilePicUri").setValue(uri.toString());
                    });
                }).addOnFailureListener(exception -> {
                    // Handle any errors that may occur while uploading the file
                    Log.e(TAG, "Error uploading file", exception);
                });
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outputState){
        super.onSaveInstanceState(outputState);
        outputState.putString(username_key, String.valueOf(username.getText()));
        outputState.putString(name_key, String.valueOf(name.getText()));
        outputState.putString(age_key, String.valueOf(age.getText()));
        outputState.putString(height_key, String.valueOf(height.getText()));
        outputState.putString(weight_key, String.valueOf(weight.getText()));
        outputState.putString(calorie_key, String.valueOf(calorieInTakeTarget.getText()));
        outputState.putInt(gender_spinner_position, gender.getSelectedItemPosition());
        outputState.putInt(goal_spinner_position, goalType.getSelectedItemPosition());
        outputState.putInt(activity_spinner_position, activityLevel.getSelectedItemPosition());

        outputState.putParcelable(KEY_IMAGE_URI, selectedImage);
        outputState.putParcelable(KEY_IMAGE_BITMAP, imageBitmap);
    }

    // Open the camera app to take a new picture
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    // Open the gallery to select an image
    private void dispatchImageFromGalleryIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_FROM_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            Glide.with(this).load(imageBitmap).into(profileImageView);
        } else if (requestCode == REQUEST_IMAGE_FROM_GALLERY && resultCode == RESULT_OK) {
            selectedImage = data.getData();
            Glide.with(this).load(selectedImage).into(profileImageView);
        }
    }

    void populateForm(String userID){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersRef = databaseReference.child("Users").child(userID);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Retrieve the user data as a User object
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());
                name.setText(user.getName());
                age.setText(String.valueOf(user.getAge()));
                gender.setSelection(genderAdapter.getPosition(user.getGender()));
                height.setText(String.valueOf(user.getHeight()));
                weight.setText(String.valueOf(user.getWeight()));
                calorieInTakeTarget.setText(String.valueOf(user.getCalorieInTakeTarget()));
                goalType.setSelection(goalAdapter.getPosition(user.getGoalType()));
                activityLevel.setSelection(activityAdapter.getPosition(user.getActivityLevel()));

                // Get a reference to the profile picture in Firebase Storage
                profilePicRef = storageRef.child("profile_pics/" + username.getText().toString());

                // Get the download URL for the image file
                profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        // Use an image loading library like Glide or Picasso to display the image in an ImageView
                        Glide.with(getApplicationContext()).load(uri).into(profileImageView);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle any errors that may occur when downloading the image
                        Log.e("Firebase", "Failed to download image: " + e.getMessage());
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
            }
        });
    }


}
