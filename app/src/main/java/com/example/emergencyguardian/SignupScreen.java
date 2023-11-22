package com.example.emergencyguardian;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;
import java.util.HashMap;



public class SignupScreen extends AppCompatActivity implements View.OnClickListener {

    TextView signIn;
    Button signUp;
    EditText fullName, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_screen);

        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);
        fullName = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.sign_in){
            finish();
        } else if(view.getId() == R.id.sign_up){
            // Initialize Firestore
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // Get the values from the EditText fields
            String name = fullName.getText().toString();
            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();
            DocumentReference docRef = db.collection("emergency_guardian").document(userEmail);
            // Create a HashMap to store the user data
            Map<String, Object> userData = new HashMap<>();
            userData.put("name", name);
            userData.put("email", userEmail);
            userData.put("password", userPassword);

            // Use Firestore's add() method to store the data in the database
            docRef.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    // Navigate to the login screen
                    Intent intent = new Intent(SignupScreen.this, LoginScreen.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }
}

