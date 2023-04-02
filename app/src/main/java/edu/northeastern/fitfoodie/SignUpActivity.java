package edu.northeastern.fitfoodie;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {
    Spinner spinnerGender;
    Spinner spinnerGoal;
    Spinner spinnerActivity;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.sign_up_form);

        spinnerGender = findViewById(R.id.gender_spinner);
        spinnerGoal = findViewById(R.id.goal_spinner);
        spinnerActivity = findViewById(R.id.activity_spinner);

        configureGenderSpinner();
        configureGoalSpinner();
        configureActivitySpinner();

        Button signupButton = findViewById(R.id.sign_up_button);
        signupButton.setOnClickListener(new SignUpOnClickListener(findViewById(R.id.name_edit_view),
                findViewById(R.id.gender_spinner),
                findViewById(R.id.goal_spinner),
                findViewById(R.id.activity_spinner),
                findViewById(R.id.age_edit_view),
                findViewById(R.id.height_edit_view),
                findViewById(R.id.weight_edit_view),
                findViewById(R.id.calorie_target_edit_view)
                //this
        ));
//        Intent intent = new Intent(SignUpActivity.this, Home.class);
//        startActivity(intent);
    }

    void configureGenderSpinner(){
        ArrayAdapter<CharSequence> genderAdapter = ArrayAdapter.createFromResource(this, R.array.gender_options, android.R.layout.simple_spinner_item);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        spinnerGender.setAdapter(genderAdapter);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        spinnerGoal.setAdapter(goalAdapter);

        spinnerGoal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

        spinnerActivity.setAdapter(activityAdapter);

        spinnerActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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

    public void signUp(View v){


        //Validate input can be implemented later
        //validateInput(name, gender, goalType, activityLevel, age, height, weight, calorieInTakeTarget);
    }


}
