package com.example.adminapp;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;

public class HomeActivity extends AppCompatActivity {
    MaterialCardView addNotice;
    MaterialCardView addgalleryimage;
    MaterialCardView addEbook;
    MaterialCardView faculty;
    MaterialCardView deleteNotice;
    MaterialCardView studentdata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        initiate();
    }
    private void init()
    {
        addNotice = findViewById(R.id.addNotice);
        addgalleryimage = findViewById(R.id.addgalleryimage);
        addEbook = findViewById(R.id.addebook);
        faculty = findViewById(R.id.fac);
        deleteNotice = findViewById(R.id.deleteNotice);
        studentdata = findViewById(R.id.student_data);
    }
    private void initiate()
    {
        addNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UploadNotice.class));
            }
        });
        addgalleryimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UploadImage.class));
            }
        });
        addEbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),UploadPdfActivity.class));
            }
        });
        faculty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startActivity(new Intent(getApplicationContext(),UpdateTeacher.class));

            }
        });
        deleteNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),DeleteNoticeActivity.class));
            }
        });
        studentdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomeActivity.this, "Open Website", Toast.LENGTH_SHORT).show();
            }
        });
    }
}