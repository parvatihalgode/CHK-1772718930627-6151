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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

        fetchSubjectsFromCDN();

        binding.actvSubject.setOnItemClickListener((parent, v, position, id) -> {
            selectedSubject = (String) parent.getItemAtPosition(position);
            binding.tvSubjectTitle.setText(selectedSubject);
            binding.btnStartPractice.setEnabled(true);
        });

        binding.btnStartPractice.setOnClickListener(v -> {
            if (selectedSubject != null) {
                Bundle args = new Bundle();
                args.putString("subjectName", selectedSubject);
                Navigation.findNavController(v).navigate(com.example.techvidyaaa.R.id.action_navigation_practice_to_quizFragment, args);
            }
        });
    }

    private void fetchSubjectsFromCDN() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<String> subjects = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Object value = data.getValue();
                        if (value != null) subjects.add(value.toString());
                    }
                    if (isAdded() && getContext() != null && !subjects.isEmpty()) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), 
                                android.R.layout.simple_dropdown_item_1line, subjects);
                        binding.actvSubject.setAdapter(adapter);
                    }
                } else {
                    loadLocalSubjects();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (isAdded() && getContext() != null) loadLocalSubjects();
            }
        });
    }

    private void loadLocalSubjects() {
        String[] localSubjects = {"Data Structures", "Algorithms", "Operating Systems", "Computer Networks", "DBMS"};
        if (isAdded() && getContext() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), 
                    android.R.layout.simple_dropdown_item_1line, localSubjects);
            binding.actvSubject.setAdapter(adapter);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
