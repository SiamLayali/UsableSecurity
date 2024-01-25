package com.example.usablesecurity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private EditText editTextName, editTextPhoneNumber, editTextNumberOfChildren, editTextEmail;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        editTextName = findViewById(R.id.editTextName);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextNumberOfChildren = findViewById(R.id.editTextNumberOfChildren);
        editTextEmail = findViewById(R.id.editTextEmail);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProfile();

                Intent intent = new Intent(Profile.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        user = auth.getCurrentUser();

        // Retrieve and display user details
        if (user != null) {
            displayUserInfo();
        } else {
            // Handle the case where the user is not logged in (redirect to login or handle accordingly)
            // For now, you can show a Toast message
            Toast.makeText(Profile.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayUserInfo() {
        // Fetch user details from Realtime Database
        DatabaseReference userRef = database.getReference("User").child(user.getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // User details found, update the UI
                    String name = dataSnapshot.child("firstName").getValue(String.class) +" "+
                            dataSnapshot.child("lastName").getValue(String.class);
                    String phoneNumber = dataSnapshot.child("phone").getValue(String.class);
                    String numberOfChildren = dataSnapshot.child("numberOfChildren").getValue(String.class);

                    // Update EditText fields
                    editTextName.setText(name);
                    editTextPhoneNumber.setText(phoneNumber);
                    editTextNumberOfChildren.setText(numberOfChildren);
                    editTextEmail.setText(user.getEmail());
                } else {
                    // Handle the case where user details are not found
                    Toast.makeText(Profile.this, "User details not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle errors
                Toast.makeText(Profile.this, "Error fetching user details: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveProfile() {
        // Perform any validation or processing as needed
        // For simplicity, let's just display the information in a Toast message
        String profileInfo = "Name: " + editTextName.getText().toString() +
                "\nPhone Number: " + editTextPhoneNumber.getText().toString() +
                "\nNumber of Children: " + editTextNumberOfChildren.getText().toString() +
                "\nEmail: " + editTextEmail.getText().toString();

        Toast.makeText(this, profileInfo, Toast.LENGTH_LONG).show();
    }
}
