package com.example.techvidyaaa.ui;

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
        String cdnUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?ixlib=rb-1.2.1&auto=format&fit=crop&w=800&q=80";
        Glide.with(this)
                .load(cdnUrl)
                .thumbnail(0.1f) // Load 10% size first
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

        // Core Navigation
        binding.cardAiGuru.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_aiGuruFragment));
        binding.cardPractice.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.navigation_practice));
        binding.cardBattle.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.navigation_battle));
        binding.cardBooks.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.booksFragment));

        // Programming Languages
        View.OnClickListener practiceNav = v -> Navigation.findNavController(v).navigate(R.id.navigation_practice);
        binding.cardLangC.setOnClickListener(practiceNav);
        binding.cardLangPython.setOnClickListener(practiceNav);
        binding.cardLangCpp.setOnClickListener(practiceNav);
        binding.cardLangJava.setOnClickListener(practiceNav);

        // Technical Topics
        binding.cardTopicAlgorithms.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.algorithmOptionsFragment));
        binding.cardTopicDataStructures.setOnClickListener(practiceNav);
        binding.cardTopicDatabases.setOnClickListener(practiceNav);
        binding.cardTopicWebDev.setOnClickListener(practiceNav);

        // More Resources
        binding.btnReferenceBooks.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.booksFragment));
        binding.btnStudyNotes.setOnClickListener(practiceNav);
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
