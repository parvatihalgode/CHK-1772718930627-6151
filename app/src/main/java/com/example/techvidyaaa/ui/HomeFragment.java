package com.example.techvidyaaa.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentHomeBinding;
import com.example.techvidyaaa.db.DatabaseHelper;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DatabaseHelper dbHelper;
    private FirebaseAuth mAuth;
    private ExecutorService executorService;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final int DAILY_GOAL_MINUTES = 120;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        mAuth = FirebaseAuth.getInstance();
        executorService = Executors.newSingleThreadExecutor();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // TechVidya Logo applied instead of profile image
        Glide.with(this)
                .load(R.drawable.ic_app_logo)
                .override(200, 200)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .transition(DrawableTransitionOptions.withCrossFade())
                .circleCrop()
                .into(binding.ivProfileCDN);

        loadUserData();
        syncRealtimeData();

        // Quick Quiz Button Navigation
        binding.btnQuickQuiz.setOnClickListener(v -> animateAdvancedFeedback(v, () -> {
            if (isAdded()) {
                Bundle args = new Bundle();
                args.putBoolean("isQuiz", true);
                Navigation.findNavController(requireView()).navigate(R.id.navigation_practice, args);
            }
        }));

        // Core Navigation
        binding.cardAiGuru.setOnClickListener(v -> animateAdvancedFeedback(v, () -> {
            if (isAdded()) Navigation.findNavController(requireView()).navigate(R.id.action_navigation_home_to_aiGuruFragment);
        }));
        binding.cardPractice.setOnClickListener(v -> animateAdvancedFeedback(v, () -> {
            if (isAdded()) Navigation.findNavController(requireView()).navigate(R.id.navigation_practice);
        }));
        binding.cardBattle.setOnClickListener(v -> animateAdvancedFeedback(v, () -> {
            if (isAdded()) Navigation.findNavController(requireView()).navigate(R.id.navigation_battle);
        }));
        binding.cardBooks.setOnClickListener(v -> animateAdvancedFeedback(v, () -> {
            if (isAdded()) Navigation.findNavController(requireView()).navigate(R.id.booksFragment);
        }));

        // Programming Languages
        binding.cardLangC.setOnClickListener(v -> navigateToSubjectOptions(v, "C Programming"));
        binding.cardLangPython.setOnClickListener(v -> navigateToSubjectOptions(v, "Python"));
        binding.cardLangCpp.setOnClickListener(v -> navigateToSubjectOptions(v, "C++"));
        binding.cardLangJava.setOnClickListener(v -> navigateToSubjectOptions(v, "Java"));

        // Technical Topics
        binding.cardTopicAlgorithms.setOnClickListener(v -> navigateToSubjectOptions(v, "Algorithms"));
        binding.cardTopicDataStructures.setOnClickListener(v -> navigateToSubjectOptions(v, "Data Structures"));
        binding.cardTopicDatabases.setOnClickListener(v -> navigateToSubjectOptions(v, "Database Management"));
        binding.cardTopicWebDev.setOnClickListener(v -> navigateToSubjectOptions(v, "Web Development"));

        // More Resources
        binding.btnReferenceBooks.setOnClickListener(v -> animateAdvancedFeedback(v, () -> {
            if (isAdded()) Navigation.findNavController(requireView()).navigate(R.id.booksFragment);
        }));
        binding.btnStudyNotes.setOnClickListener(v -> animateAdvancedFeedback(v, () -> {
            if (isAdded()) Navigation.findNavController(requireView()).navigate(R.id.navigation_practice);
        }));
    }

    private void loadUserData() {
        if (executorService == null || executorService.isShutdown()) return;
        executorService.execute(() -> {
            Cursor cursor = dbHelper.getLatestUser();
            if (cursor != null && cursor.moveToFirst()) {
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                cursor.close();
                mainHandler.post(() -> {
                    if (binding != null) {
                        binding.tvWelcome.setText("Welcome, " + username + "! 👋");
                    }
                });
            }
        });
    }

    private void syncRealtimeData() {
        String userId = mAuth.getUid();
        if (userId == null) return;

        DatabaseReference userStatsRef = FirebaseDatabase.getInstance().getReference("user_stats").child(userId);

        userStatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && binding != null) {
                    long totalSolved = snapshot.hasChild("totalQuestionsSolved") ? snapshot.child("totalQuestionsSolved").getValue(Long.class) : 0;
                    long totalTime = snapshot.hasChild("totalLearningTime") ? snapshot.child("totalLearningTime").getValue(Long.class) : 0;

                    binding.scoreChip.setText("Questions Solved: " + totalSolved);
                    
                    if (totalTime >= 60) {
                        binding.tvTimeValue.setText((totalTime / 60) + "h " + (totalTime % 60) + "m");
                    } else {
                        binding.tvTimeValue.setText(totalTime + " mins");
                    }

                    int progress = (int) ((totalTime * 100.0) / DAILY_GOAL_MINUTES);
                    animateProgress(Math.min(progress, 100));
                    
                    if (progress < 30) {
                        binding.usageProgress.setIndicatorColor(requireContext().getColor(android.R.color.holo_red_light));
                    } else if (progress < 70) {
                        binding.usageProgress.setIndicatorColor(requireContext().getColor(android.R.color.holo_orange_light));
                    } else {
                        binding.usageProgress.setIndicatorColor(requireContext().getColor(android.R.color.holo_green_light));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void navigateToSubjectOptions(View v, String subjectName) {
        animateAdvancedFeedback(v, () -> {
            if (isAdded()) {
                Bundle args = new Bundle();
                args.putString("subjectName", subjectName);
                Navigation.findNavController(requireView()).navigate(R.id.action_navigation_home_to_subjectOptionsFragment, args);
            }
        });
    }

    private void animateAdvancedFeedback(View v, Runnable endAction) {
        if (v instanceof MaterialCardView) {
            ((MaterialCardView) v).setCardElevation(20f);
        }

        v.animate()
                .scaleX(0.92f)
                .scaleY(0.92f)
                .alpha(0.7f)
                .setDuration(100)
                .withEndAction(() -> {
                    if (binding == null) return;
                    v.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .alpha(1.0f)
                            .setDuration(100)
                            .withEndAction(() -> {
                                if (v instanceof MaterialCardView) {
                                    ((MaterialCardView) v).setCardElevation(4f);
                                }
                                endAction.run();
                            })
                            .start();
                })
                .start();
    }

    private void animateProgress(int targetProgress) {
        if (binding == null) return;
        ObjectAnimator animation = ObjectAnimator.ofInt(binding.usageProgress, "progress", binding.usageProgress.getProgress(), targetProgress);
        animation.setDuration(1000);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }
}
