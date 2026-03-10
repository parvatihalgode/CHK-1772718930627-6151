package com.example.techvidyaaa.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.techvidyaaa.databinding.FragmentSlidesBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SlidesFragment extends Fragment {

    private FragmentSlidesBinding binding;
    private DatabaseReference mDatabase;
    private String subjectName;
    private List<Slide> slideList;
    private int currentSlideIndex = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSlidesBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            subjectName = getArguments().getString("subjectName");
        }
        mDatabase = FirebaseDatabase.getInstance().getReference("slides").child(subjectName);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.tvSlidesSubject.setText(subjectName + " Slides");
        slideList = new ArrayList<>();

        loadSlides();

        binding.btnNextSlide.setOnClickListener(v -> {
            if (currentSlideIndex < slideList.size() - 1) {
                currentSlideIndex++;
                displaySlide();
            }
        });

        binding.btnPrevSlide.setOnClickListener(v -> {
            if (currentSlideIndex > 0) {
                currentSlideIndex--;
                displaySlide();
            }
        });
    }

    private void loadSlides() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Slide slide = data.getValue(Slide.class);
                        if (slide != null) slideList.add(slide);
                    }
                    if (!slideList.isEmpty()) {
                        displaySlide();
                    }
                } else {
                    seedDummySlides();
                    displaySlide();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void seedDummySlides() {
        slideList.add(new Slide("Welcome to " + subjectName, "This slide deck will cover the core basics of " + subjectName + "."));
        slideList.add(new Slide("What is " + subjectName + "?", subjectName + " is a powerful tool used widely in the industry for various applications."));
        slideList.add(new Slide("Getting Started", "To begin with " + subjectName + ", you need to set up your environment properly."));
        slideList.add(new Slide("Key Features", "1. Performance\n2. Scalability\n3. Community Support"));
        slideList.add(new Slide("Summary", "Congratulations! You have completed the intro slides for " + subjectName + "."));
    }

    private void displaySlide() {
        if (slideList.isEmpty()) return;
        
        Slide currentSlide = slideList.get(currentSlideIndex);
        binding.tvSlideTitle.setText(currentSlide.title);
        binding.tvSlideDescription.setText(currentSlide.description);
        binding.tvSlideCounter.setText((currentSlideIndex + 1) + " / " + slideList.size());

        binding.btnPrevSlide.setEnabled(currentSlideIndex > 0);
        binding.btnNextSlide.setEnabled(currentSlideIndex < slideList.size() - 1);
    }

    public static class Slide {
        public String title;
        public String description;

        public Slide() {}
        public Slide(String title, String description) {
            this.title = title;
            this.description = description;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
