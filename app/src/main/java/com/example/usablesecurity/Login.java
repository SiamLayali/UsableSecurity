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

public class Login extends Activity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signUpTextView;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            navigateToHome();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize views
        emailEditText = findViewById(R.id.rectangle_1);
        passwordEditText = findViewById(R.id.rectangle_2);
        loginButton = findViewById(R.id.loginBtn);
        signUpTextView = findViewById(R.id.noaccount);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        // Set onClickListener for login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle login logic here
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Login.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Login.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Show progress bar
                progressBar.setVisibility(View.VISIBLE);

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // Hide progress bar
                                progressBar.setVisibility(View.GONE);

                                if (task.isSuccessful()) {
                                    Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                    navigateToHome();
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Set onClickListener for sign up text
        signUpTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Signup.class);
                startActivity(intent);
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(Login.this, Profile.class);
        startActivity(intent);
        finish();
    }
}
