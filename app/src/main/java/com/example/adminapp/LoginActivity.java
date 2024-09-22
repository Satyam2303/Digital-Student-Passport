package com.example.adminapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    EditText emailedt, passwordedt;
    Button loginbtn;
    FirebaseAuth auth;
    FirebaseFirestore fStore;
    int loginAttempts = 0; // Track login attempts
    boolean isBlocked = false; // Flag to check if login is blocked
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        instance();
        initiatelogin();
    }

    void init() {
        emailedt = findViewById(R.id.email_log);
        passwordedt = findViewById(R.id.pass_log);
        loginbtn = findViewById(R.id.LogInBtn);
    }

    private void instance() {
        auth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
    }

    private void initiatelogin() {
        loginbtn.setOnClickListener(view -> login());
    }

    private void login() {
        if (isBlocked) {
            Toast.makeText(this, "Login temporarily blocked. Please try again later.", Toast.LENGTH_SHORT).show();
            return;
        }

        String email = emailedt.getText().toString();
        String password = passwordedt.getText().toString();
        if (email.isEmpty()) {
            Toast.makeText(this, "Email cannot be empty", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty()) {
            Toast.makeText(this, "Password cannot be empty", Toast.LENGTH_SHORT).show();
        } else if (!isValidEmail(email)) {
            Toast.makeText(this, "Email or Password is not valid", Toast.LENGTH_SHORT).show();
        } else if (!isValidPass(password)) {
            Toast.makeText(this, "Password should be at least 8 characters long", Toast.LENGTH_LONG).show();
        } else {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Signing in, Please wait...");
            progressDialog.show();


            auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                @Override
                public void onSuccess(AuthResult authResult) {
                    progressDialog.dismiss();
                    checkUserAccesLevel(authResult.getUser().getUid());
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                    loginAttempts++; // Increment login attempts
                    if (loginAttempts >= 3) {
                        isBlocked = true; // Block further login attempts
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Unblock login after 5 minutes
                                isBlocked = false;
                                loginAttempts = 0; // Reset login attempts
                            }
                        }, 30 * 1000); // 5 minutes delay

                    }
                }
            });
        }
    }

    public static boolean isValidEmail(CharSequence target) {
        return (Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidPass(CharSequence target) {
        return (!TextUtils.isEmpty(target) && target.length() >= 8);
    }

    private void checkUserAccesLevel(String uid) {
        DocumentReference df = fStore.collection("Students").document(uid);
        df.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("isStudent") != null) {
                    Toast.makeText(LoginActivity.this, "You are a student", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DocumentReference df2 = fStore.collection("student").document(uid);
        df2.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.getString("Students") != null)
                {
                    startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                    finish();
                }
            }
        });
    }
}