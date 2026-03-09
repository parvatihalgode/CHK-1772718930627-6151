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
import com.example.techvidyaaa.databinding.FragmentAlgorithmOptionsBinding;

public class AlgorithmOptionsFragment extends Fragment {

    private FragmentAlgorithmOptionsBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAlgorithmOptionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnAlgoPractice.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_practice));

        binding.btnAlgoBattle.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_battle));

        binding.btnAlgoConcept.setOnClickListener(v -> 
            Navigation.findNavController(v).navigate(R.id.navigation_practice)); // Placeholder
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}