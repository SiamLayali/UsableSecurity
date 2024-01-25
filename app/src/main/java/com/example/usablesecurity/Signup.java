package com.example.usablesecurity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Signup extends Activity {

    private EditText firstNameEditText, lastNameEditText, emailEditText, phoneEditText, numberOfChildrenEditText, passwordEditText;
    private Button signupButton;
    private TextView loginTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup); // replace with your layout name

        // Initialize views
        firstNameEditText = findViewById(R.id.rectangle_1);
        lastNameEditText = findViewById(R.id.rectangle_2);
        emailEditText = findViewById(R.id.rectangle_3);
        phoneEditText = findViewById(R.id.rectangle_4);
        numberOfChildrenEditText = findViewById(R.id.rectangle_5);
        passwordEditText = findViewById(R.id.rectangle_6);
        signupButton = findViewById(R.id.signupBtn);
        loginTextView = findViewById(R.id.account);

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

                // Process the signup data (e.g., validation, send to server)
            }
        });

        // Set onClickListener for login text view
        loginTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Signup.this, Login.class);
                startActivity(intent);            }
        });
    }
}
