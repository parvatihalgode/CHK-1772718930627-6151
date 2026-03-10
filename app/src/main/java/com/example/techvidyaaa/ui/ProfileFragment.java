package com.example.techvidyaaa.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentProfileBinding;
import com.example.techvidyaaa.db.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private DatabaseHelper dbHelper;
    private FirebaseAuth mAuth;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        mAuth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadUserData();

        binding.btnProfileLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Navigation.findNavController(v).navigate(R.id.loginFragment); 
        });
    }

    private void loadUserData() {
        executorService.execute(() -> {
            // 1. Fetch Local Data for Speed
            Cursor cursor = dbHelper.getLatestUser();
            if (cursor != null && cursor.moveToFirst()) {
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String degree = cursor.getString(cursor.getColumnIndexOrThrow("degree"));
                String year = cursor.getString(cursor.getColumnIndexOrThrow("year"));
                cursor.close();

                mainHandler.post(() -> updateUI(username, degree, year));
            }

            // 2. Sync with Firebase for Real-time Stats
            String userId = mAuth.getUid();
            if (userId != null) {
                DatabaseReference leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard").child(userId);
                leaderboardRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            long score = 0;
                            if (snapshot.hasChild("score")) {
                                Object scoreVal = snapshot.child("score").getValue();
                                if (scoreVal instanceof Long) score = (Long) scoreVal;
                                else if (scoreVal instanceof Integer) score = (int) scoreVal;
                            }
                            
                            final String finalScore = String.valueOf(score);
                            mainHandler.post(() -> {
                                if (binding != null) {
                                    binding.tvTotalScoreProfile.setText(finalScore);
                                    // For demo based on your images
                                    binding.tvLearningTimeToday.setText("6 minutes");
                                    binding.tvDailyPercent.setText("5%");
                                    binding.profileDailyProgress.setProgress(5);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
            }
        });
    }

    private void updateUI(String name, String degree, String year) {
        if (binding == null) return;
        
        binding.tvProfileName.setText(name.toLowerCase());
        binding.chipRole.setText("Student - " + degree + " (" + year + ")");
        
        // Extract Initials
        if (name.length() >= 2) {
            binding.tvUserInitials.setText(name.substring(0, 2).toUpperCase());
        } else if (name.length() == 1) {
            binding.tvUserInitials.setText(name.toUpperCase());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        executorService.shutdown();
    }
}
