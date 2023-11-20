package com.example.emergencyguardian;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener {

    Button signIn;
    TextView signUp;
    EditText email, password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        signIn = findViewById(R.id.sign_in);
        signUp = findViewById(R.id.sign_up);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        mAuth = FirebaseAuth.getInstance();

        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.sign_in) {
            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("emergency_guardian")
                    .whereEqualTo("email", userEmail)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            boolean isAuthenticated = false;
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String password = document.getString("password");
                                if (password.equals(userPassword)) {
                                    isAuthenticated = true;
                                    break;
                                }
                            }
                            if (isAuthenticated) {
                                // Sign in success
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginScreen.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(LoginScreen.this, "Error getting documents: " + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        } else if (view.getId() == R.id.sign_up) {
            Intent intent = new Intent(getApplicationContext(), SignupScreen.class);
            startActivity(intent);
        }
    }
}