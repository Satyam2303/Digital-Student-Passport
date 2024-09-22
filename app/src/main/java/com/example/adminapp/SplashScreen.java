package com.example.adminapp;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {

    private static final int SPLASH_SCREEN_DELAY = 3000;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                if (currentUser != null) {
                    // User is signed in
                    Intent homeIntent = new Intent(SplashScreen.this, HomeActivity.class);
                    startActivity(homeIntent);
                } else {
                    // No user is signed in
                    Intent selectionIntent = new Intent(SplashScreen.this, SelectionActivity.class);
                    startActivity(selectionIntent);
                }
                finish();
            }
        }, SPLASH_SCREEN_DELAY);
    }
}
