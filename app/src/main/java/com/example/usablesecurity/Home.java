package com.example.usablesecurity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    FirebaseAuth auth;
    Button button, addButton;
    TextView textView;
    FirebaseUser user;
    private DatabaseReference databaseReference;
    RecyclerView recyclerView;
    ArrayList<Links> link;
    Adapter adapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.Logout);
        addButton = findViewById(R.id.addButton);
        textView = findViewById(R.id.user_details);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("links");
        recyclerView = findViewById(R.id.recycler);
        link = new ArrayList<>();
        adapter = new Adapter(link);
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open a dialog or use an EditText to get the link from the user
                // For simplicity, let's assume you have an EditText named editTextLink

                // Add the link to the RecyclerView
                EditText editTextLink = findViewById(R.id.editTextLink);
                ImageView img = findViewById(R.id.linkImg);
                int I = img.getId();
                String link = editTextLink.getText().toString();
                if (!link.isEmpty()) {
                    Links newLink = new Links(I, link);

                    // Add the new item to the adapter
                    adapter.addItem(newLink);
                    adapter.notifyDataSetChanged();
                    // Save the updated list to Firebase Realtime Database
                    fetchDataFromFirebase();
                    saveLinksToDatabase(adapter.getLinks());

                    // Clear the EditText for the next input
                    editTextLink.getText().clear();
                }
            }
        });

        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
            finish();
        } else {
            textView.setText(user.getEmail());
        }

        button.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View view) {
                                          FirebaseAuth.getInstance().signOut();
                                          Intent intent = new Intent(getApplicationContext(), Login.class);
                                          startActivity(intent);
                                          finish();
                                      };

                                  });
    }
    void fetchDataFromFirebase() {
        {
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // Clear the existing list
                    link.clear();

                    // Iterate through the dataSnapshot to get links
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Links linkData = snapshot.getValue(Links.class);
                        if (linkData != null) {
                            link.add(linkData);
                        }
                    }

                    // Notify the adapter that the data has changed
                    adapter.notifyDataSetChanged();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error, if any
                    Toast.makeText(Home.this, "Error fetching data from Firebase", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void saveLinksToDatabase(ArrayList<Links> linksList) {
        // Clear existing data in the database node (optional, depends on your requirements)
        databaseReference.removeValue();

        // Iterate through the list and save each link to the database
        for (Links link : linksList) {
            String key = databaseReference.push().getKey();
            if (key != null) {
                databaseReference.child(key).setValue(link);
            }
        }
    }

    {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Clear the existing list
                link.clear();

                // Iterate through the dataSnapshot to get links
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Links linkData = snapshot.getValue(Links.class);
                    if (linkData != null) {
                        link.add(linkData);
                    }
                }

                // Notify the adapter that the data has changed
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the error, if any
                Toast.makeText(Home.this, "Error fetching data from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }
}