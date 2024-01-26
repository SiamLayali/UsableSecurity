package com.example.usablesecurity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        auth = FirebaseAuth.getInstance();
        button = findViewById(R.id.Logout);
        addButton = findViewById(R.id.addButton);
        textView = findViewById(R.id.user_details);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler);
        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            // Log an error or handle the case where the RecyclerView is not found
            Toast.makeText(this, "Error: RecyclerView not found!", Toast.LENGTH_LONG).show();
            return; // Optional: return from onCreate if RecyclerView is crucial for the activity
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("links");

        link = new ArrayList<>();
        adapter = new Adapter(link);
        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText editTextLink = findViewById(R.id.editTextLink);
                String linkText = editTextLink.getText().toString();
                if (!linkText.isEmpty()) {
                    Links newLink = new Links(linkText);
                    adapter.addItem(newLink);
                    saveLinksToDatabase(adapter.getLinks());
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
            }
        });

      //  fetchDataFromFirebase();
    }

    void fetchDataFromFirebase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                link.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Links linkData = snapshot.getValue(Links.class);
                    if (linkData != null) {
                        link.add(linkData);
                    }
                }
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Home.this, "Error fetching data from Firebase", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void saveLinksToDatabase(ArrayList<Links> linksList) {
        databaseReference.removeValue();
        for (Links link : linksList) {
            String key = databaseReference.push().getKey();
            if (key != null) {
                databaseReference.child(key).setValue(link);
            }
        }
    }
}