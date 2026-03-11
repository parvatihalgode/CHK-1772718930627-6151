package com.example.techvidyaaa.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentProfileBinding;
import com.example.techvidyaaa.databinding.ItemQuizAttemptBinding;
import com.example.techvidyaaa.db.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
            NavController navController = Navigation.findNavController(v);
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph, true)
                    .build();
            navController.navigate(R.id.loginFragment, null, navOptions);
        });
    }

    private void loadUserData() {
        executorService.execute(() -> {
            // 1. Local Data (Fast)
            Cursor cursor = dbHelper.getLatestUser();
            if (cursor != null && cursor.moveToFirst()) {
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String degree = cursor.getString(cursor.getColumnIndexOrThrow("degree"));
                String year = cursor.getString(cursor.getColumnIndexOrThrow("year"));
                cursor.close();
                mainHandler.post(() -> updateUI(username, degree, year));
            }

            // 2. Real-time Data (Firebase)
            String userId = mAuth.getUid();
            if (userId != null) {
                DatabaseReference userStatsRef = FirebaseDatabase.getInstance().getReference("user_stats").child(userId);
                DatabaseReference leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard").child(userId);

                // Sync Stats & Breakdown
                userStatsRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && binding != null) {
                            long totalTime = snapshot.child("totalLearningTime").exists() ? snapshot.child("totalLearningTime").getValue(Long.class) : 0;
                            long battleTime = snapshot.child("battleTime").exists() ? snapshot.child("battleTime").getValue(Long.class) : 0;
                            long practiceTime = snapshot.child("practiceTime").exists() ? snapshot.child("practiceTime").getValue(Long.class) : 0;
                            long quizzesTaken = snapshot.child("quizzesTaken").exists() ? snapshot.child("quizzesTaken").getValue(Long.class) : 0;

                            mainHandler.post(() -> {
                                if (binding != null) {
                                    binding.tvLearningTimeToday.setText(totalTime + " minutes");
                                    binding.tvQuizzesTaken.setText(String.valueOf(quizzesTaken));
                                    
                                    int progress = (int) ((totalTime / 120.0) * 100);
                                    binding.profileDailyProgress.setProgress(Math.min(progress, 100));
                                    binding.tvDailyPercent.setText(Math.min(progress, 100) + "%");

                                    // Activity Breakdown Calculation
                                    long generalTime = Math.max(0, totalTime - battleTime - practiceTime);
                                    
                                    if (totalTime > 0) {
                                        int battlePerc = (int) (battleTime * 100 / totalTime);
                                        int practicePerc = (int) (practiceTime * 100 / totalTime);
                                        int generalPerc = 100 - battlePerc - practicePerc;

                                        binding.tvBattleTimeBreakdown.setText(battleTime + "m (" + battlePerc + "%)");
                                        binding.progressBattle.setProgress(battlePerc);

                                        binding.tvPracticeTimeBreakdown.setText(practiceTime + "m (" + practicePerc + "%)");
                                        binding.progressPractice.setProgress(practicePerc);

                                        binding.tvGeneralTime.setText(generalTime + "m (" + generalPerc + "%)");
                                        binding.progressGeneral.setProgress(generalPerc);
                                    } else {
                                        binding.tvBattleTimeBreakdown.setText("0m (0%)");
                                        binding.progressBattle.setProgress(0);
                                        binding.tvPracticeTimeBreakdown.setText("0m (0%)");
                                        binding.progressPractice.setProgress(0);
                                        binding.tvGeneralTime.setText("0m (0%)");
                                        binding.progressGeneral.setProgress(0);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

                // Sync Score
                leaderboardRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists() && binding != null) {
                            long scoreVal = snapshot.child("score").exists() ? snapshot.child("score").getValue(Long.class) : 0;
                            mainHandler.post(() -> {
                                if (binding != null) {
                                    binding.tvTotalScoreProfile.setText(String.valueOf(scoreVal));
                                    long qCount = Long.parseLong(binding.tvQuizzesTaken.getText().toString());
                                    if (qCount > 0) {
                                        binding.tvAvgScore.setText(String.valueOf(scoreVal / qCount));
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });

                // Load Recent Quiz Attempts
                loadRecentAttempts(userStatsRef.child("activity_log"));
            }
        });
    }

    private void loadRecentAttempts(DatabaseReference logRef) {
        Query recentQuery = logRef.orderByChild("timestamp").limitToLast(5);
        recentQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (binding == null) return;
                binding.layoutRecentAttempts.removeAllViews();
                
                if (snapshot.exists()) {
                    binding.tvNoAttempts.setVisibility(View.GONE);
                    // Iterate backwards to show newest first
                    java.util.List<DataSnapshot> list = new java.util.ArrayList<>();
                    for (DataSnapshot item : snapshot.getChildren()) list.add(item);
                    java.util.Collections.reverse(list);

                    for (DataSnapshot item : list) {
                        ItemQuizAttemptBinding itemBinding = ItemQuizAttemptBinding.inflate(getLayoutInflater());
                        
                        String type = item.child("type").getValue(String.class);
                        long score = item.child("scoreEarned").exists() ? item.child("scoreEarned").getValue(Long.class) : 0;
                        long ts = item.child("timestamp").exists() ? item.child("timestamp").getValue(Long.class) : 0;
                        int correct = item.child("questionsSolved").exists() ? item.child("questionsSolved").getValue(Integer.class) : 0;

                        itemBinding.tvAttemptTitle.setText(type != null ? type : "Session");
                        itemBinding.tvAttemptScore.setText("+" + score + " pts (" + correct + " correct)");
                        
                        if (ts > 0) {
                            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault());
                            itemBinding.tvAttemptDate.setText(sdf.format(new Date(ts)));
                        }

                        if ("Battle".equalsIgnoreCase(type)) {
                            itemBinding.ivAttemptIcon.setImageResource(R.drawable.ic_battle);
                            itemBinding.ivAttemptIcon.setColorFilter(requireContext().getColor(android.R.color.holo_orange_dark));
                        }

                        binding.layoutRecentAttempts.addView(itemBinding.getRoot());
                    }
                } else {
                    binding.tvNoAttempts.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void updateUI(String name, String degree, String year) {
        if (binding == null) return;
        binding.tvProfileName.setText(name);
        binding.chipRole.setText("Student • " + degree);
        if (name.length() >= 2) binding.tvUserInitials.setText(name.substring(0, 2).toUpperCase());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        executorService.shutdown();
    }
}
