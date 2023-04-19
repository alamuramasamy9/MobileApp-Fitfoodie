package edu.northeastern.fitfoodie;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

public class DietOne extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_one);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})

        Button DietViewButton = findViewById(R.id.DietLogButton);
        Button LogViewButton  =findViewById(R.id.button);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        LogViewButton.setOnClickListener(view -> {
            Intent new_intent1 = new Intent(DietOne.this, LogView.class);
            new_intent1.putExtra("username", username);
            startActivity(new_intent1);
        });

        DietViewButton.setOnClickListener(view ->
        {
            Intent new_intent1 = new Intent(DietOne.this, DietTwo.class);
            new_intent1.putExtra("username", username);
            startActivity(new_intent1);
        });


    }





}