package com.example.techvidyaaa;

import android.app.Application;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.techvidyaaa.db.DatabaseSeeder;
import com.example.techvidyaaa.db.DownloadQuestionsWorker;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.FirebaseDatabase;

public class TechVidyaApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        
        // Explicitly initialize Firebase with your Database URL
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("1:657805799973:android:863199931f90dcb47c05cc")
                .setApiKey("AIzaSyBuAYuxIFJvWf5oLNfqoY50po8RoQQfsOs")
                .setDatabaseUrl("https://techvidya-cbca8-default-rtdb.firebaseio.com/")
                .setProjectId("techvidya-cbca8")
                .setStorageBucket("techvidya-cbca8.firebasestorage.app")
                .build();

        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this, options);
        }

        // Enable offline persistence
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // Seed initial large question set locally for immediate use
        DatabaseSeeder.seedIfNeeded(this);

        // Schedule the CDN question downloader for future updates
        scheduleQuestionDownload();
    }

    private void scheduleQuestionDownload() {
        Constraints constraints = new Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        OneTimeWorkRequest downloadRequest = new OneTimeWorkRequest.Builder(DownloadQuestionsWorker.class)
                .setConstraints(constraints)
                .build();

        WorkManager.getInstance(this).enqueue(downloadRequest);
    }
}
