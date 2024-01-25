package com.example.usablesecurity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Home extends AppCompatActivity {
FirebaseAuth auth;
Button button;
TextView textView;
FirebaseUser user;
RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        auth=FirebaseAuth.getInstance();
        button=findViewById(R.id.Logout);
        textView=findViewById(R.id.user_details);
        recyclerView=findViewById(R.id.recycler);
        user = auth.getCurrentUser();
        if(user== null){
            Intent intent = new Intent(getApplicationContext(),Login.class);
            startActivity(intent);
            finish();
        }
else{
    textView.setText(user.getEmail());
        }
button.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
       FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getApplicationContext(),Login.class);
        startActivity(intent);
        finish();
    }
});
    }
}