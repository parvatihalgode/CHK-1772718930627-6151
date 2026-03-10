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
            Navigation.findNavController(v).navigate(R.id.navigation_practice, args);
        }));

        binding.cardOptionBattle.setOnClickListener(v -> animateAdvancedFeedback(v, () -> {
            Navigation.findNavController(v).navigate(R.id.navigation_battle);
        }));

        binding.cardOptionConcepts.setOnClickListener(v -> animateAdvancedFeedback(v, () -> {
            Bundle args = new Bundle();
            args.putString("subjectName", subjectName);
            Navigation.findNavController(v).navigate(R.id.learnConceptsFragment, args);
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
        if (v instanceof MaterialCardView) {
            ((MaterialCardView) v).setCardElevation(25f);
        } else {
            v.setElevation(25f);
        }

        v.animate()
                .scaleX(0.92f)
                .scaleY(0.92f)
                .alpha(0.6f)
                .setDuration(100)
                .withEndAction(() -> {
                    v.animate()
                            .scaleX(1.0f)
                            .scaleY(1.0f)
                            .alpha(1.0f)
                            .setDuration(100)
                            .withEndAction(() -> {
                                if (v instanceof MaterialCardView) {
                                    ((MaterialCardView) v).setCardElevation(4f);
                                } else {
                                    v.setElevation(4f);
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
