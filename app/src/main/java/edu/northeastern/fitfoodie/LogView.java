package edu.northeastern.fitfoodie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

public class LogView extends AppCompatActivity {

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
        DatabaseReference nutrition_ref = databaseReference.child("Users").child(userid).child("nutrition").child(dateInString);

        nutrition_ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Map<String, Object> result = (Map<String, Object>) task.getResult().getValue();
                    Iterator hmIterator = result.entrySet().iterator();
                    while (hmIterator.hasNext()) {
                        Map.Entry mapElement
                                = (Map.Entry)hmIterator.next();
                        // Printing mark corresponding to string entries
//                        Log.d("firebase",mapElement.getKey() + " : "
//                                + mapElement.getValue());
                        Map<String, Object> value = (Map<String, Object>) mapElement.getValue();
                        Map<String, Object> final_value = (Map<String, Object>) value.get("nameValuePairs");
                        Iterator iterator = final_value.entrySet().iterator();
                        while (iterator.hasNext()) {
                            Map.Entry element
                                    = (Map.Entry)iterator.next();
                            Log.d("firebase", element.getKey() + " : "
                                    + element.getValue());
                        }

                    }
//                    Log.d("firebase", result.toString());
                }
            }
        });
    }
}