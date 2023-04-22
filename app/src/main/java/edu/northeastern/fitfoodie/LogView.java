package edu.northeastern.fitfoodie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointBackward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import edu.northeastern.fitfoodie.adaptors.CustomAdapter;

public class LogView extends AppCompatActivity {

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    DatabaseReference databaseReference;

    ArrayList<String> names = new ArrayList<>();
    ArrayList<Double> calories = new ArrayList<>();
    String dateInString;
    SimpleDateFormat formatter;

    Button calender;
    TextView totalCalories;
    TextView NoDataTV;
    String pattern;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_view);
        initializeViewComponents();
        Intent intent = getIntent();
        String userid = intent.getStringExtra("username");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(userid).child("nutrition");

        pattern = "dd-MM-yyyy";
        formatter = new SimpleDateFormat(pattern);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder().setValidator(DateValidatorPointBackward.now());
        MaterialDatePicker.Builder materialDateBuilder = MaterialDatePicker.Builder.datePicker().setCalendarConstraints(constraintsBuilder.build());

        materialDateBuilder.setTitleText("SELECT A DATE");
        final MaterialDatePicker materialDatePicker = materialDateBuilder.build();

        calender.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // getSupportFragmentManager() to
                        // interact with the fragments
                        // associated with the material design
                        // date picker tag is to get any error
                        // in logcat
                        materialDatePicker.show(getSupportFragmentManager(), "MATERIAL_DATE_PICKER");
                    }
                });

        // now create the instance of the material date
        // picker

        materialDatePicker.addOnPositiveButtonClickListener(selection -> {
            Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
            cal.setTimeInMillis((Long) selection);
            dateInString = formatter.format(cal.getTime());
            Log.d("datepicker", dateInString);
            getNutritionData(dateInString);
        });

        mLayoutManager = new LinearLayoutManager(mRecyclerView.getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CustomAdapter(names, calories);
        mRecyclerView.setAdapter(mAdapter);



    }

    @Override
    protected void onStart() {
        super.onStart();
        dateInString = new SimpleDateFormat(pattern).format(new Date());
        calender.setText(dateInString);
        getNutritionData(dateInString);
    }

    public void initializeViewComponents(){
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        totalCalories = findViewById(R.id.TotalCaloriesTV);
        NoDataTV = findViewById(R.id.NoDataTV);
        calender = findViewById(R.id.calender_picker);
    }

    public void getNutritionData(String date){
        DatabaseReference nutrition_ref = databaseReference.child(date);
        names.clear();
        calories.clear();
        nutrition_ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Map<String, Object> result = (Map<String, Object>) task.getResult().getValue();
                    if(result == null){
                        mRecyclerView.setVisibility(View.INVISIBLE);
                        NoDataTV.setVisibility(View.VISIBLE);
                        totalCalories.setText("Total Calories: 0");
                    }
                    else{
                        NoDataTV.setVisibility(View.GONE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                        Iterator hmIterator = result.entrySet().iterator();
                        while (hmIterator.hasNext()) {
                            Map.Entry mapElement
                                    = (Map.Entry)hmIterator.next();
                            Map<String, Object> value = (Map<String, Object>) mapElement.getValue();
                            Map<String, Object> final_value = (Map<String, Object>) value.get("nameValuePairs");
                            names.add(StringUtils.capitalize(final_value.get("name").toString().toLowerCase()));
                            calories.add(Double.parseDouble(final_value.get("calories").toString()));
                        }
                        Log.d("firebase", names.toString());
                        Log.d("calories", calories.toString());
                        updateRecyclerView(names, calories);
                        calender.setText(date);
                        totalCalories.setText("Total Calories: "+String.valueOf(calories.stream().mapToDouble(a -> a).sum()));
                    }
                }
            }
        });
    }

    public void updateRecyclerView(ArrayList<String> names, ArrayList<Double> calories){
        mAdapter.updateList(names, calories);
    }

}