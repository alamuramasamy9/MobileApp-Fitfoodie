package edu.northeastern.fitfoodie;

import static android.content.ContentValues.TAG;
import static java.lang.Integer.parseInt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignInActivity extends AppCompatActivity {
    private EditText usernameView;

    @Override
    protected void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.sign_in_form);

        Button signInButton = findViewById(R.id.sign_in_button);

        usernameView = findViewById(R.id.username_signIn_edit_view);

        signInButton.setOnClickListener(
                view -> {
                    if (usernameView.getText().toString().isEmpty()) {
                        resetData();
                        Snackbar snack = Snackbar.make(view, "Please enter username!", Snackbar.LENGTH_LONG).setAction("Action", null);
                        View snackView = snack.getView();
                        TextView mTextView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
                        mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        snack.show();
                        return;
                    }
                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
                    DatabaseReference usersRef = databaseReference.child("Users");

                    /** Below commented code should actually work, but just to test I added code below below*/
                    DatabaseReference requestedUser = usersRef.child(usernameView.getText().toString());
                    /** Attempt with invalid credentials below*/
                    // DatabaseReference requestedUser = usersRef.child("testnotexist").child("username");
                    /** Attempt with valid credentials below*/
                    //DatabaseReference requestedUser = databaseReference.child("Users").child("apurvakhatri");


                    requestedUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                Log.println(Log.ASSERT, "Obj", "User with username does exist");
                                Intent intent = new Intent(SignInActivity.this, Home.class);
                                intent.putExtra("username", usernameView.getText().toString());
                                startActivity(intent);
                            } else {
                                Log.println(Log.ASSERT, "Obj", "User with username does not exist");
                                Snackbar snack = Snackbar.make(view, "User does not exist", Snackbar.LENGTH_LONG).setAction("Action", null);
                                View snackView = snack.getView();
                                TextView mTextView = snackView.findViewById(com.google.android.material.R.id.snackbar_text);
                                mTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                                snack.show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                });

    }

    private void resetData() {
        usernameView.setText("");
    }

}
