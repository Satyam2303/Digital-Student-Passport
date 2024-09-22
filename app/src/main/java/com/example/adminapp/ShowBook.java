package com.example.adminapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowBook extends AppCompatActivity {

    private ListView pdfListView;
    private List<String> pdfTitles;
    private List<String> pdfUrls;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_book);

        pdfListView = findViewById(R.id.pdfListView);
        pdfTitles = new ArrayList<>();
        pdfUrls = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.pdf_list_item, R.id.pdfTitleTextView, pdfTitles);

        pdfListView.setAdapter(adapter);
        fetchPdfs();

        pdfListView.setOnItemClickListener((parent, view, position, id) -> {
            String pdfUrl = pdfUrls.get(position);
            downloadPdf(pdfUrl, pdfTitles.get(position));
        });
    }

    private void fetchPdfs() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("pdf");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pdfTitles.clear();
                pdfUrls.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String title = dataSnapshot.child("pdfTitle").getValue(String.class);
                    String url = dataSnapshot.child("pdfUrl").getValue(String.class);
                    if (title != null && url != null) {
                        pdfTitles.add(title);
                        pdfUrls.add(url);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowBook.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void downloadPdf(String url, String title) {
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
        request.setTitle(title);
        request.setDescription("Downloading PDF...");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title + ".pdf");

        DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        if (downloadManager != null) {
            downloadManager.enqueue(request);
            Toast.makeText(this, "Downloading " + title, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to download", Toast.LENGTH_SHORT).show();
        }
    }
}
