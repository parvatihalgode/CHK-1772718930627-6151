package com.example.techvidyaaa.ui;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentResultBinding;

public class ResultFragment extends Fragment {

    private FragmentResultBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            int score = getArguments().getInt("score", 0);
            int total = getArguments().getInt("total", 0);
            int correctCount = getArguments().getInt("correctCount", 0);
            
            int percent = (total > 0) ? (correctCount * 100 / total) : 0;
            
            binding.tvScoreDetail.setText("You got " + correctCount + " out of " + total + " correct");
            binding.tvPointsEarned.setText("+" + score + " Points");
            binding.tvScorePercent.setText(percent + "%");
            
            // Animate Ring
            ObjectAnimator animation = ObjectAnimator.ofInt(binding.resultProgress, "progress", 0, percent);
            animation.setDuration(1500);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();
            
            // Set color based on performance
            if (percent < 40) {
                binding.resultProgress.setIndicatorColor(requireContext().getColor(android.R.color.holo_red_light));
                binding.tvScorePercent.setTextColor(requireContext().getColor(android.R.color.holo_red_light));
            } else if (percent < 75) {
                binding.resultProgress.setIndicatorColor(requireContext().getColor(android.R.color.holo_orange_light));
                binding.tvScorePercent.setTextColor(requireContext().getColor(android.R.color.holo_orange_light));
            } else {
                binding.resultProgress.setIndicatorColor(requireContext().getColor(android.R.color.holo_green_light));
                binding.tvScorePercent.setTextColor(requireContext().getColor(android.R.color.holo_green_light));
            }
        }

        binding.btnFinish.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_home);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
