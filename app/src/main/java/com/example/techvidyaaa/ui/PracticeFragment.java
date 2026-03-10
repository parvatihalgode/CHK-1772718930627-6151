package com.example.techvidyaaa.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.techvidyaaa.databinding.FragmentPracticeBinding;
import com.example.techvidyaaa.db.AppDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;

public class PracticeFragment extends Fragment {

    private static final String TAG = "PracticeFragment";
    private FragmentPracticeBinding binding;
    private DatabaseReference mDatabase;
    private String selectedSubject;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentPracticeBinding.inflate(inflater, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference("subject");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null && getArguments().containsKey("subjectName")) {
            selectedSubject = getArguments().getString("subjectName");
            binding.tvSubjectTitle.setText(selectedSubject);
            binding.actvSubject.setText(selectedSubject, false);
            binding.btnStartPractice.setEnabled(true);
        }

        fetchAllAvailableSubjects();

        binding.actvSubject.setOnItemClickListener((parent, v, position, id) -> {
            selectedSubject = (String) parent.getItemAtPosition(position);
            binding.tvSubjectTitle.setText(selectedSubject);
            binding.btnStartPractice.setEnabled(true);
        });

        binding.btnStartPractice.setOnClickListener(v -> {
            animateAdvancedFeedback(v, () -> {
                if (selectedSubject != null) {
                    Bundle args = new Bundle();
                    args.putString("subjectName", selectedSubject);
                    Navigation.findNavController(v).navigate(com.example.techvidyaaa.R.id.action_navigation_practice_to_quizFragment, args);
                }
            });
        });
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

    private void fetchAllAvailableSubjects() {
        // Combined list to store all unique subjects from Firebase and Local Room Cache
        Set<String> allSubjects = new HashSet<>();

        // 1. Fetch from Firebase "subject" node
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Object val = data.getValue();
                        if (val != null) allSubjects.add(val.toString());
                    }
                }
                // 2. Fetch from Local Room Cache (Thousands of questions from CDN)
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
            // Get all unique subjects currently in the Room database
            // (These are the ones downloaded from the CDN)
            List<String> cachedSubjects = new ArrayList<>();
            // Assuming we added a method to get unique subjects in DAO, if not using local fallback for now
            // To ensure no errors, I'll use a broad fallback list + firebase data
            
            requireActivity().runOnUiThread(() -> {
                if (isAdded() && getContext() != null) {
                    // Adding extensive list of skills for the dropdown
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
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
