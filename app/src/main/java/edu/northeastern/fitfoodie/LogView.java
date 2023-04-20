package edu.northeastern.fitfoodie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.northeastern.fitfoodie.adaptors.CustomAdapter;

public class LogView extends AppCompatActivity {

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_view);
        Intent intent = getIntent();
        String userid = intent.getStringExtra("username");
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

//            FirebaseUser user1 = FirebaseAuth.getInstance().getCurrentUser();
//            String uid1 = user1.getUid();
//            System.out.println("CURRENT USER DETAILS: " +uid1);


//            mAuth = FirebaseAuth.getInstance();
//            String id = String.valueOf(mAuth.getCurrentUser());
        String pattern = "dd-MM-yyyy";
        String dateInString =new SimpleDateFormat(pattern).format(new Date());

        ArrayList<String> names = new ArrayList<>();
        ArrayList<Double> calories = new ArrayList<>();

        DatabaseReference nutrition_ref = databaseReference.child("Users").child(userid).child("nutrition").child(dateInString);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        TextView totalCalories = findViewById(R.id.TotalCaloriesTV);
        TextView NoDataTV = findViewById(R.id.NoDataTV);


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
                    }
                    else{
                        NoDataTV.setVisibility(View.GONE);
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
                        Log.d("firebase", calories.toString());
                        totalCalories.setText("Total Calories: "+String.valueOf(calories.stream().mapToDouble(a -> a).sum()));


                        mLayoutManager = new LinearLayoutManager(mRecyclerView.getContext());
                        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                                mLayoutManager.getOrientation());
                        mRecyclerView.addItemDecoration(dividerItemDecoration);
                        mRecyclerView.setLayoutManager(mLayoutManager);

                        mAdapter = new CustomAdapter(names, calories);
                        mRecyclerView.setAdapter(mAdapter);
                    }

                }
            }
        });
    }

}