package com.example.techvidyaaa.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.techvidyaaa.databinding.FragmentAiGuruBinding;

public class AiGuruFragment extends Fragment {

    private FragmentAiGuruBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAiGuruBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnSendMessage.setOnClickListener(v -> {
            String message = binding.etChatMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                // Logic to handle message sending would go here
                Toast.makeText(requireContext(), "Sending doubt to AI Guru...", Toast.LENGTH_SHORT).show();
                binding.etChatMessage.setText("");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}