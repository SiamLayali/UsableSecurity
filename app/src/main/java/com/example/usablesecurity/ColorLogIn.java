package com.example.usablesecurity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ColorLogIn extends AppCompatActivity {

    private ImageView redImageView, blueImageView, greenImageView, yellowImageView;
    private String selectedColor;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_color_log_in);

        redImageView = findViewById(R.id.red);
        blueImageView = findViewById(R.id.blue);
        greenImageView = findViewById(R.id.green);
        yellowImageView = findViewById(R.id.yellow);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());

        // Set onClickListener for each ImageView
        displayUserInfo();

        redImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor= "red";
                handleColorSelection("Red");
                navigateToHome();
            }
        });

        blueImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor="blue";
                handleColorSelection("Blue");
                navigateToHome();
            }
        });

        greenImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor="green";
                handleColorSelection("Green");
                navigateToHome();
            }
        });

        yellowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedColor="yellow";
                handleColorSelection("Yellow");
                navigateToHome();
            }
        });
    }

    private void navigateToHome() {
        Intent intent = new Intent(ColorLogIn.this, Home.class);
        startActivity(intent);
        finish();
    }

    private void displayUserInfo() {
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child(user.getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String color = dataSnapshot.child("color").getValue(String.class);
                    if(color==selectedColor){
                            Intent intent = new Intent(ColorLogIn.this, Home.class);
                            startActivity(intent);
                            finish();
                    }else {
                        Toast.makeText(ColorLogIn.this, "oppss..! wrong color " , Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ColorLogIn.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(ColorLogIn.this, "Error fetching user details: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleColorSelection(String color) {
        selectedColor = color;

        // Save the selected color to Firebase Realtime Database under the user's UID
        databaseReference.child("color").setValue(selectedColor);

        // You can perform any additional actions related to color selection here
        Toast.makeText(this, "Selected Color: " + selectedColor, Toast.LENGTH_SHORT).show();
    }
}
