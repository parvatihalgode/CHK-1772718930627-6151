package com.example.techvidyaaa.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentPracticeBinding;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

public class PracticeFragment extends Fragment {

    private FragmentPracticeBinding binding;
    private DatabaseReference mDatabase;
    private String selectedSubject;
    private String selectedDifficulty;
    private boolean isQuizMode = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPracticeBinding.inflate(inflater, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference("subject");
        
        if (getArguments() != null) {
            isQuizMode = getArguments().getBoolean("isQuiz", false);
        }
        
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (isQuizMode) {
            binding.tvPracticeTitle.setText(R.string.quiz_center);
            binding.btnStartPractice.setText(R.string.start_quiz_with_timer);
        } else {
            binding.tvPracticeTitle.setText(R.string.practice_center);
            binding.btnStartPractice.setText(R.string.start_practice_session);
        }

        setupDifficultyDropdown();

        if (getArguments() != null && getArguments().containsKey("subjectName")) {
            selectedSubject = getArguments().getString("subjectName");
            binding.tvSubjectTitle.setText(selectedSubject);
            binding.actvSubject.setText(selectedSubject, false);
            showDifficultyMenu();
        }

        fetchAllAvailableSubjects();

        binding.actvSubject.setOnItemClickListener((parent, v, position, id) -> {
            selectedSubject = (String) parent.getItemAtPosition(position);
            binding.tvSubjectTitle.setText(selectedSubject);
            showDifficultyMenu();
        });

        binding.actvDifficulty.setOnItemClickListener((parent, v, position, id) -> {
            selectedDifficulty = (String) parent.getItemAtPosition(position);
            binding.btnStartPractice.setEnabled(true);
        });

        binding.btnStartPractice.setOnClickListener(v -> animateAdvancedFeedback(v, () -> {
            if (selectedSubject != null && selectedDifficulty != null) {
                Bundle args = new Bundle();
                args.putString("subjectName", selectedSubject);
                args.putString("difficulty", selectedDifficulty);
                args.putBoolean("isQuiz", isQuizMode);
                Navigation.findNavController(v).navigate(R.id.action_navigation_practice_to_quizFragment, args);
            } else {
                Toast.makeText(getContext(), R.string.select_subject_difficulty, Toast.LENGTH_SHORT).show();
            }
        }));
    }

    private void setupDifficultyDropdown() {
        String[] difficulties = {"Easy", "Medium", "Hard"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_dropdown_item_1line, difficulties);
        binding.actvDifficulty.setAdapter(adapter);
    }

    private void showDifficultyMenu() {
        binding.tilDifficulty.setVisibility(View.VISIBLE);
        binding.tvSubjectStatus.setText(R.string.select_difficulty_level);
        binding.btnStartPractice.setEnabled(false);
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
                .withEndAction(() -> v.animate()
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
                        .start())
                .start();
    }

    private void fetchAllAvailableSubjects() {
        Set<String> allSubjects = new HashSet<>();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Object val = data.getValue();
                        if (val != null) allSubjects.add(val.toString());
                    }
                }
                fetchFromRoomCache(allSubjects);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                fetchFromRoomCache(allSubjects);
            }
        });
    }

    private void fetchFromRoomCache(Set<String> subjectSet) {
        Executors.newSingleThreadExecutor().execute(() -> {
            if (isAdded() && getContext() != null) {
                requireActivity().runOnUiThread(() -> {
                    if (isAdded() && getContext() != null) {
                        subjectSet.add("C Programming");
                        subjectSet.add("Python");
                        subjectSet.add("Java");
                        subjectSet.add("Data Structures");
                        subjectSet.add("Algorithms");
                        subjectSet.add("Machine Learning");
                        subjectSet.add("Cloud Computing");
                        subjectSet.add("Cybersecurity");
                        subjectSet.add("Database Management");
                        subjectSet.add("Web Development");
                        subjectSet.add("Operating Systems");
                        subjectSet.add("Computer Networks");

                        List<String> finalList = new ArrayList<>(subjectSet);
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), 
                                android.R.layout.simple_dropdown_item_1line, finalList);
                        binding.actvSubject.setAdapter(adapter);
                    }
                });
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
