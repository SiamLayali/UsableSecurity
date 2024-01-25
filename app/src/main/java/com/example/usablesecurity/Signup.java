package com.example.usablesecurity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Signup extends Activity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneEditText, numberOfChildrenEditText, passwordEditText;
    private Button signupButton;
    private TextView loginTextView;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Initialize mAuth
        mAuth = FirebaseAuth.getInstance();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToHome();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // Initialize views
        firstNameEditText = findViewById(R.id.rectangle_1);
        lastNameEditText = findViewById(R.id.rectangle_2);
        emailEditText = findViewById(R.id.rectangle_3);
        phoneEditText = findViewById(R.id.rectangle_4);
        numberOfChildrenEditText = findViewById(R.id.rectangle_5);
        passwordEditText = findViewById(R.id.rectangle_6);
        signupButton = findViewById(R.id.signupBtn);
        loginTextView = findViewById(R.id.account);

        mAuth = FirebaseAuth.getInstance();

        // Set onClickListener for signup button
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle signup logic here
                String firstName = firstNameEditText.getText().toString();
                String lastName = lastNameEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String phone = phoneEditText.getText().toString();
                String numberOfChildren = numberOfChildrenEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Signup.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Signup.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Show progress bar
                if (progressBar != null) {
                    progressBar.setVisibility(View.VISIBLE);
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Hide progress bar
                                if (progressBar != null) {
                                    progressBar.setVisibility(View.GONE);
                                }

                                if (task.isSuccessful()) {
                                    Toast.makeText(Signup.this, "Account Created Successfully.", Toast.LENGTH_SHORT).show();
                                    navigateToHome();
                                } else {
                                    // If sign up fails, display a message to the user.
                                    Toast.makeText(Signup.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Set onClickListener for login text
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(Signup.this, Home.class);
        startActivity(intent);
        finish();
    }
}
