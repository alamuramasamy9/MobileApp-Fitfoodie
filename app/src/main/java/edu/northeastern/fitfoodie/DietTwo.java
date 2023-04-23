package edu.northeastern.fitfoodie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.common.reflect.TypeToken;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import io.grpc.Server;

public class DietTwo extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText foodConsumed;
    private EditText quantityOfFoodConsumed;
    private Button submitButtonAfterProvidingInfo;

    private TextView toastmessge;
    private TextView foodName;
    private TextView foodQuantity;
    private TextView caloriesConsumed;
    private TextView fatConsumed;
    private TextView proteinConsumed;

    private static final String foodName_key = "xxx";
    private static final String foodQuality_key = "aaa";
    private static final String caloriesConsumed_key = "bbb";
    private static final String fatConsumed_key = "ccc";
    private static final String proteinConsumed_key = "ddd";
    private static final String toastmessage_key = "eee";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diet_two);


        //refernce for edit text in the layout
        foodConsumed = findViewById(R.id.foodNameConsumed);
        quantityOfFoodConsumed = findViewById(R.id.foodQuantityConsumed);
        submitButtonAfterProvidingInfo = findViewById(R.id.submitButton);
        foodName = findViewById(R.id.foodNameConsumedTextView);
        foodQuantity = findViewById(R.id.foodQuantityConsumedTextView);
        caloriesConsumed = findViewById(R.id.caloriesConsumedTextView);
        fatConsumed = findViewById(R.id.fatConsumedTextView);
        proteinConsumed = findViewById(R.id.proteinConsumedTextView);
        toastmessge = findViewById(R.id.toastView);



        if (savedInstanceState != null) {
            foodName.setText(savedInstanceState.getString("xxx"));
            foodQuantity.setText(savedInstanceState.getString("aaa"));
            caloriesConsumed.setText(savedInstanceState.getString("bbb"));
            fatConsumed.setText(savedInstanceState.getString("ccc"));
            proteinConsumed.setText(savedInstanceState.getString("ddd"));
            toastmessge.setText(savedInstanceState.getString("eee"));

        }

        Intent intent = getIntent();
        String userid = intent.getStringExtra("username");
        System.out.println("USER NAME IS: " + userid);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


        DatabaseReference nutrition_ref = databaseReference.child("Users").child(userid).child("nutrition");


        submitButtonAfterProvidingInfo.setOnClickListener(view ->
        {
            String xword = foodConsumed.getText().toString();
            String yword = quantityOfFoodConsumed.getText().toString();
            String selected = yword + " " + xword;
            System.out.println("QUERY STRING: " + selected);


            resetData();

            String urlString = "https://api.api-ninjas.com/v1/nutrition?query=" + selected;
            new Thread(() -> {


                try {
                    String apiKey = "ycVFyqdxalYZNhflfRMpT318qctIPovNO6LlPmqo";
                    URL url = new URL(urlString);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestProperty("X-Api-Key", apiKey);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.connect();

                    InputStream inputStream = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    String line;
                    StringBuilder response = new StringBuilder();
                    while ((line = reader.readLine()) != null) {
                        response.append(line);
                    }


                    reader.close();
                    String result = response.toString();
                    System.out.println("RESULT:" + result);

                    JSONArray jsonArray = new JSONArray(result);

                    JSONObject responseJSON = new JSONObject();

                    responseJSON.put("foods", jsonArray);
                    System.out.println("JSON ARRAY: " + jsonArray);




                    List<FoodItem> foods = new ArrayList<>();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);


                        Gson gson = new Gson();
                        String jsonString = gson.toJson(jsonObject);
                        Map<String, Object> jsonMap = new Gson().fromJson(jsonString, new TypeToken<HashMap<String, Object>>() {
                        }.getType());
                        System.out.println("JSON MAP: " + jsonMap);


                        String pattern = "dd-MM-yyyy";
                        String dateInString = new SimpleDateFormat(pattern).format(new Date());
                        Calendar calendar = Calendar.getInstance();
                        nutrition_ref.child(String.valueOf(dateInString)).child(String.valueOf(calendar.getTime())).setValue(jsonMap);


                        // Create FoodItem object from JSON object
                        FoodItem foodItem = new FoodItem();
                        foodItem.setName(jsonObject.getString("name"));
                        foodItem.setCalories(jsonObject.getDouble("calories"));
                        foodItem.setServingSize(jsonObject.getDouble("serving_size_g"));
                        foodItem.setTotalFat(jsonObject.getDouble("fat_total_g"));
                        foodItem.setSaturatedFat(jsonObject.getDouble("fat_saturated_g"));
                        foodItem.setProtein(jsonObject.getDouble("protein_g"));
                        foodItem.setSodium(jsonObject.getInt("sodium_mg"));
                        foodItem.setPotassium(jsonObject.getInt("potassium_mg"));
                        foodItem.setCholesterol(jsonObject.getInt("cholesterol_mg"));
                        foodItem.setTotalCarbohydrates(jsonObject.getDouble("carbohydrates_total_g"));
                        foodItem.setDietaryFiber(jsonObject.getDouble("fiber_g"));
                        foodItem.setSugars(jsonObject.getDouble("sugar_g"));

                        foods.add(foodItem);
                    }

                    String itemNames = "Food Consumed: " + foods.stream().map(FoodItem::getName).collect(Collectors.joining(", "));
                    String quantity = "Quantity of Food Consumed: " + foods.stream().mapToDouble(FoodItem::getServingSize).sum() + " grams";


                    double totalCalories = foods.stream().mapToDouble(FoodItem::getCalories).sum();
                    double totalFat = foods.stream().mapToDouble(FoodItem::getTotalFat).sum();
                    double totalProtein = foods.stream().mapToDouble(FoodItem::getProtein).sum();


                    System.out.println("REACHED UPDATE UI");

                    if(jsonArray.length() == 0) {
                        UpdateUI2();
                    }
                    else {
                        updateUI(itemNames, quantity, totalCalories, totalFat, totalProtein);
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                } catch (NumberFormatException e) {
                    //e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                }
            }).start();
        });
    }

    public void onSaveInstanceState(Bundle outputState) {
        super.onSaveInstanceState(outputState);
        outputState.putString(foodName_key, String.valueOf(foodName.getText()));
        outputState.putString(foodQuality_key, String.valueOf(foodQuantity.getText()));
        outputState.putString(caloriesConsumed_key, String.valueOf(caloriesConsumed.getText()));
        outputState.putString(fatConsumed_key, String.valueOf(fatConsumed.getText()));
        outputState.putString(proteinConsumed_key, String.valueOf(proteinConsumed.getText()));
        outputState.putString(toastmessage_key, String.valueOf(toastmessge.getText()));

    }

    private void resetData() {
        toastmessge.setText("");
        foodQuantity.setText("");
        foodName.setText("");
        caloriesConsumed.setText("");
        fatConsumed.setText("");
        proteinConsumed.setText("");
    }

//    private void onSaveInstanceState(Bundle outState)

    @SuppressLint("SetTextI18n")
    private void UpdateUI2() {
        runOnUiThread(() -> toastmessge.setText("OOPS!!! Please enter a valid Food name and Quantity!"));
    }

    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    private void updateUI(String itemNames, String quantity, double totalCalories, double totalFat, double totalProtein) {

        runOnUiThread(() -> {

            foodName.setText(itemNames);
            foodQuantity.setText(quantity);
            caloriesConsumed.setText("Total Calorie Count: " + String.format("%.1f", totalCalories));
            fatConsumed.setText("Total Fat Count: " + String.format("%.1f", totalFat));
            proteinConsumed.setText("Total Protein Count: " + String.format("%.1f", totalProtein));

        });

    }
}