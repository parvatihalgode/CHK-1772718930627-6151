package com.example.techvidyaaa.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentSubjectOptionsBinding;
import com.google.android.material.card.MaterialCardView;

public class SubjectOptionsFragment extends Fragment {

    private FragmentSubjectOptionsBinding binding;
    private String subjectName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSubjectOptionsBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            subjectName = getArguments().getString("subjectName");
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvSubjectHeader.setText(subjectName);

        binding.cardOptionPractice.setOnClickListener(v -> animateAdvancedFeedback(v, () -> {
            Bundle args = new Bundle();
            args.putString("subjectName", subjectName);
            args.putBoolean("isQuiz", false);
            Navigation.findNavController(v).navigate(R.id.navigation_practice, args);
        }));

        binding.cardOptionBattle.setOnClickListener(v -> animateAdvancedFeedback(v, () -> {
            Navigation.findNavController(v).navigate(R.id.navigation_battle);
        }));

        binding.cardOptionBooks.setOnClickListener(v -> animateAdvancedFeedback(v, () -> {
            Navigation.findNavController(v).navigate(R.id.booksFragment);
        }));

        binding.cardOptionSlides.setOnClickListener(v -> animateAdvancedFeedback(v, () -> {
            Bundle args = new Bundle();
            args.putString("subjectName", subjectName);
            Navigation.findNavController(v).navigate(R.id.slidesFragment, args);
        }));
    }

    private void animateAdvancedFeedback(View v, Runnable endAction) {
        // Optimized animation: using lower elevation and faster duration for smoother performance
        float originalElevation = v.getElevation();
        if (v instanceof MaterialCardView) {
            ((MaterialCardView) v).setCardElevation(originalElevation + 8f);
        } else {
            v.setElevation(originalElevation + 8f);
        }

        v.animate()
                .scaleX(0.96f)
                .scaleY(0.96f)
                .setDuration(80)
                .withEndAction(() -> {
                    if (binding == null) return;
                    v.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .setDuration(80)
                            .withEndAction(() -> {
                                if (v instanceof MaterialCardView) {
                                    ((MaterialCardView) v).setCardElevation(originalElevation);
                                } else {
                                    v.setElevation(originalElevation);
                                }
                                endAction.run();
                            })
                            .start();
                })
                .start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
