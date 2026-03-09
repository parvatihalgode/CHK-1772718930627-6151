package com.example.techvidyaaa.ui;

import android.animation.ObjectAnimator;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentHomeBinding;
import com.example.techvidyaaa.db.DatabaseHelper;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DatabaseHelper dbHelper;

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

        // Fetch real data from SQLite to personalize the home screen
        Cursor cursor = dbHelper.getLatestUser();
        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            binding.tvWelcome.setText("Welcome back, " + username + "! 👋");
            cursor.close();
        }

        // Animate the usage progress for interactivity
        animateProgress(75); // Let's animate it to 75% for visual appeal

        // Interactivity for the usage card
        binding.cardUsage.setOnClickListener(v -> {
            Toast.makeText(requireContext(), "You've studied for 0 hours today!", Toast.LENGTH_SHORT).show();
            animateProgress(75); // Re-animate on click
        });

        // Navigation for Quick Actions
        binding.cardAiGuru.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_practice));
            
        binding.cardPractice.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_practice));
            
        binding.cardBattle.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_battle));

        binding.cardBooks.setOnClickListener(v -> 
             Navigation.findNavController(v).navigate(R.id.booksFragment));

        // Navigation for Programming Languages
        binding.cardLangC.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_practice));
        binding.cardLangPython.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_practice));
        binding.cardLangCpp.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_practice));
        binding.cardLangJava.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_practice));

        // Navigation for Technical Topics
        binding.cardTopicAlgorithms.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.algorithmOptionsFragment));
        binding.cardTopicDataStructures.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_practice));
        binding.cardTopicDatabases.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_practice));
        binding.cardTopicWebDev.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_practice));

        // Navigation for More Resources
        binding.btnReferenceBooks.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.booksFragment));
        binding.btnStudyNotes.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_practice));
    }

    private void animateProgress(int targetProgress) {
        ObjectAnimator animation = ObjectAnimator.ofInt(binding.usageProgress, "progress", 0, targetProgress);
        animation.setDuration(1500); // 1.5 seconds
        animation.setInterpolator(new DecelerateInterpolator());
        animation.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}