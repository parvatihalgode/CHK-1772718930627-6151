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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DatabaseHelper dbHelper;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Performance: Load CDN Profile Image with thumbnail for faster perceived speed
        String cdnUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80";
        Glide.with(this)
                .load(cdnUrl)
                .override(200, 200)
                .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_profile)
                .transition(DrawableTransitionOptions.withCrossFade())
                .circleCrop()
                .into(binding.ivProfileCDN);

        // Performance: Fetch User Data in Background
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

        animateProgress(75);

        // Core Navigation with Advanced Pop, Blink, and Shadow Animation
        binding.cardAiGuru.setOnClickListener(v -> animateAdvancedFeedback(v, () -> Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_aiGuruFragment)));
        binding.cardPractice.setOnClickListener(v -> animateAdvancedFeedback(v, () -> Navigation.findNavController(v).navigate(R.id.navigation_practice)));
        binding.cardBattle.setOnClickListener(v -> animateAdvancedFeedback(v, () -> Navigation.findNavController(v).navigate(R.id.navigation_battle)));
        binding.cardBooks.setOnClickListener(v -> animateAdvancedFeedback(v, () -> Navigation.findNavController(v).navigate(R.id.booksFragment)));

        // Programming Languages - Now navigating to SubjectOptionsFragment
        binding.cardLangC.setOnClickListener(v -> navigateToSubjectOptions(v, "C"));
        binding.cardLangPython.setOnClickListener(v -> navigateToSubjectOptions(v, "Python"));
        binding.cardLangCpp.setOnClickListener(v -> navigateToSubjectOptions(v, "C++"));
        binding.cardLangJava.setOnClickListener(v -> navigateToSubjectOptions(v, "Java"));

        // Technical Topics - Also navigating to SubjectOptionsFragment for consistency
        binding.cardTopicAlgorithms.setOnClickListener(v -> navigateToSubjectOptions(v, "Algorithms"));
        binding.cardTopicDataStructures.setOnClickListener(v -> navigateToSubjectOptions(v, "Data Structures"));
        binding.cardTopicDatabases.setOnClickListener(v -> navigateToSubjectOptions(v, "Databases"));
        binding.cardTopicWebDev.setOnClickListener(v -> navigateToSubjectOptions(v, "Web Dev"));

        // More Resources
        binding.btnReferenceBooks.setOnClickListener(v -> animateAdvancedFeedback(v, () -> Navigation.findNavController(v).navigate(R.id.booksFragment)));
        binding.btnStudyNotes.setOnClickListener(v -> animateAdvancedFeedback(v, () -> Navigation.findNavController(v).navigate(R.id.navigation_practice)));
    }

    private void navigateToSubjectOptions(View v, String subjectName) {
        animateAdvancedFeedback(v, () -> {
            Bundle args = new Bundle();
            args.putString("subjectName", subjectName);
            Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_subjectOptionsFragment, args);
        });
    }

    private void animateAdvancedFeedback(View v, Runnable endAction) {
        // 1. Shadow/Elevation Effect
        if (v instanceof MaterialCardView) {
            ((MaterialCardView) v).setCardElevation(20f);
        }

        // 2. Pop (Scale) and Blink (Alpha) Effect
        v.animate()
                .scaleX(0.92f)
                .scaleY(0.92f)
                .alpha(0.7f)
                .setDuration(100)
                .withEndAction(() -> {
                    v.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .alpha(1.0f)
                            .setDuration(100)
                            .withEndAction(() -> {
                                if (v instanceof MaterialCardView) {
                                    ((MaterialCardView) v).setCardElevation(4f); // Restore shadow
                                }
                                endAction.run();
                            })
                            .start();
                })
                .start();
    }

    private void animateProgress(int targetProgress) {
        ObjectAnimator animation = ObjectAnimator.ofInt(binding.usageProgress, "progress", 0, targetProgress);
        animation.setDuration(1500);
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        executorService.shutdown();
    }
}
