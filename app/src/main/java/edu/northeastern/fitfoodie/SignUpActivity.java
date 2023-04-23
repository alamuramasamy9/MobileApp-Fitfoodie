package edu.northeastern.fitfoodie;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class SignUpActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_FROM_GALLERY = 2;

    private ImageView profileImageView;
    private Button uploadButton;
    Uri selectedImage;
    Bitmap imageBitmap;
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



    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.sign_up_form);





        username = findViewById(R.id.username_edit_view);
        name =  findViewById(R.id.name_edit_view);
        gender = findViewById(R.id.gender_spinner);
        goalType = findViewById(R.id.goal_spinner);
        activityLevel = findViewById(R.id.activity_spinner);
        age = findViewById(R.id.age_edit_view);
        height = findViewById(R.id.height_edit_view);
        weight = findViewById(R.id.weight_edit_view);
        calorieInTakeTarget = findViewById(R.id.calorie_target_edit_view);
        profileImageView = findViewById(R.id.profile_image_view);
        mContext = getApplicationContext();
        uploadButton = findViewById(R.id.upload_button);

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

        configureGenderSpinner();
        configureGoalSpinner();
        configureActivitySpinner();

        Button signupButton = findViewById(R.id.sign_up_button);
        signupButton.setOnClickListener(new SignUpOnClickListener(
                username,
                name,
                gender,
                goalType,
                activityLevel,
                age,
                height,
                weight,
                calorieInTakeTarget,
                profileImageView,
                mContext
        ) {
            @Override
            public void onClick(View v) {
                if (username.getText().toString().trim().isEmpty()) {
                    Toast.makeText(mContext, "Please enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (name.getText().toString().trim().isEmpty()) {
                    Toast.makeText(mContext, "Please enter your name", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (age.getText().toString().trim().isEmpty()) {
                    Toast.makeText(mContext, "Please enter your age", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (height.getText().toString().trim().isEmpty()) {
                    Toast.makeText(mContext, "Please enter your height", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (weight.getText().toString().trim().isEmpty()) {
                    Toast.makeText(mContext, "Please enter your weight", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (calorieInTakeTarget.getText().toString().trim().isEmpty()) {
                    Toast.makeText(mContext, "Please enter your calorie intake target", Toast.LENGTH_SHORT).show();
                    return;
                }
                super.onClick(v);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if permission to access camera and storage is granted
                if (ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.CAMERA)
                        != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED ||
                        ContextCompat.checkSelfPermission(SignUpActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    // Request permission to access camera and storage
                    if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
                            || shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
                            || shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        // Explain why the app needs these permissions
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
                        builder.setTitle("Permissions needed")
                                .setMessage("This app needs permission to access the camera and storage in order to upload your profile picture.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Request permission to access camera and storage
                                        ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{
                                                Manifest.permission.CAMERA,
                                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
                                    }
                                });
                        builder.create().show();
                    } else {
                        // Request permission to access camera and storage
                        ActivityCompat.requestPermissions(SignUpActivity.this, new String[]{
                                Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_IMAGE_CAPTURE);
                    }
                } else {
                    // Permission is already granted
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUpActivity.this);
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

    // Handle permission requests result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            // Check if all permission requests are granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                // Permission request is denied, show a message to the user
                Toast.makeText(this, "Permission is required to take a picture.", Toast.LENGTH_SHORT).show();
            }
        }
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

    void configureGenderSpinner(){
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        gender.setAdapter(genderAdapter);

        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose Gender")){
                }else {
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(),"Selected: " +item, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void configureGoalSpinner(){
        ArrayAdapter<CharSequence> goalAdapter = ArrayAdapter.createFromResource(this, R.array.goal_options, android.R.layout.simple_spinner_item);
        goalAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        goalType.setAdapter(goalAdapter);

        goalType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose Goal Type")){
                }else {
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(),"Selected: " +item, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    void configureActivitySpinner(){
        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(this, R.array.activity_options, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        activityLevel.setAdapter(activityAdapter);

        activityLevel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).equals("Choose Activity")){
                }else {
                    String item = parent.getItemAtPosition(position).toString();
                    Toast.makeText(parent.getContext(),"Selected: " +item, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }


}
