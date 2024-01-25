package com.example.usablesecurity;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import com.google.android.gms.common.api.ApiException;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Login extends Activity {

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView signUpTextView;
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN = 9001; // Request code for sign-in

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login); // replace with your layout name

        ActionCodeSettings actionCodeSettings =
                ActionCodeSettings.newBuilder()
                        // URL you want to redirect back to. The domain (www.example.com) for this
                        // URL must be whitelisted in the Firebase Console.
                        .setUrl("https://www.example.com/finishSignUp?cartId=1234")
                        // This must be true
                        .setHandleCodeInApp(true)
                        .setIOSBundleId("com.example.ios")
                        .setAndroidPackageName(
                                "com.example.android",
                                true, /* installIfNotAvailable */
                                "12"    /* minimumVersion */)
                        .build();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.sendSignInLinkToEmail(String.valueOf(emailEditText.getText()), actionCodeSettings)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Email sent.");
                        }
                    }
                });
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Configure Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.account))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Initialize views
        emailEditText = findViewById(R.id.rectangle_1);
        passwordEditText = findViewById(R.id.rectangle_2);
        loginButton = findViewById(R.id.loginBtn);
        signUpTextView = findViewById(R.id.noaccount);

        // Set onClickListener for login button
        loginButton.setOnClickListener(v -> {
            // Handle login logic here
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            // Validate email and password
            if(validateForm(email, password)){
                // Perform Firebase email/password authentication
                performLogin(email, password);
            }
        });

        // Set onClickListener for sign up text
        signUpTextView.setOnClickListener(v -> {
            Intent intent = new Intent(Login.this, Signup.class);
            startActivity(intent);
        });

        // Set onClickListener for Google Sign-In button
        findViewById(R.id.google_sign_in_button).setOnClickListener(v -> signInWithGoogle());
    }

    private boolean validateForm(String email, String password) {
        Pattern pattern = Pattern.compile("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+");
        Matcher matcher = pattern.matcher(email);
        if (!matcher.matches()) {
            Toast.makeText(Login.this, "Invalid email format", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 6) {
            Toast.makeText(Login.this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void performLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(Login.this, "Authentication successful.",
                                Toast.LENGTH_SHORT).show();
                        // Redirect to main activity or user dashboard
                        // Intent intent = new Intent(Login.this, MainActivity.class);
                        // startActivity(intent);
                    } else {
                        Toast.makeText(Login.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign-In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign-In failed
                Toast.makeText(this, "Google sign-in failed.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Sign-in success, update UI with the signed-in user's information
                        FirebaseUser user = mAuth.getCurrentUser();
                        Toast.makeText(Login.this, "Google Authentication successful.",
                                Toast.LENGTH_SHORT).show();
                        // Redirect to main activity or user dashboard
                        // Intent intent = new Intent(Login.this, MainActivity.class);
                        // startActivity(intent);
                    } else {
                        // If sign-in fails, display a message to the user.
                        Toast.makeText(Login.this, "Google Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
