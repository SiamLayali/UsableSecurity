package com.example.usablesecurity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ColorSignUp extends AppCompatActivity {

    private ImageView redImageView, blueImageView, greenImageView, yellowImageView;
    private String selectedColor;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_sign_up);

        // Initialize views
        redImageView = findViewById(R.id.red);
        blueImageView = findViewById(R.id.blue);
        greenImageView = findViewById(R.id.green);
        yellowImageView = findViewById(R.id.yellow);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("User").child(userId);

            // Set onClickListener for each ImageView
            redImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleColorSelection("Red");
                    navigateToHome();
                }
            });

            blueImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleColorSelection("Blue");
                    navigateToHome();
                }
            });

            greenImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleColorSelection("Green");
                    navigateToHome();
                }
            });

            yellowImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleColorSelection("Yellow");
                    navigateToHome();
                }
            });
        } else {
            // Handle the case where the user is not authenticated
            // You might want to redirect the user to the login screen or take appropriate action
        }
    }

    private void navigateToHome() {
        Intent intent = new Intent(ColorSignUp.this, Home.class);
        startActivity(intent);
        finish();
    }

    private void handleColorSelection(String color) {
        selectedColor = color;

        // Save the selected color to Firebase Realtime Database under the user's UID
        databaseReference.child("color").setValue(selectedColor);

        // You can perform any additional actions related to color selection here
        Toast.makeText(this, "Selected Color: " + selectedColor, Toast.LENGTH_SHORT).show();
    }
}
