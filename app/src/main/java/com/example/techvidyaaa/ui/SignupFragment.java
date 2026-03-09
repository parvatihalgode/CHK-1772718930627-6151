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
import com.example.techvidyaaa.databinding.FragmentSignupBinding;
import com.example.techvidyaaa.db.DatabaseHelper;

public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String[] years = {"1st Year", "2nd Year", "3rd Year", "Final Year"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, years);
        binding.actvYear.setAdapter(adapter);

        binding.btnSignup.setOnClickListener(v -> {
            String username = binding.etUsername.getText().toString();
            String degree = binding.etDegree.getText().toString();
            String year = binding.actvYear.getText().toString();
            String email = binding.etSignupEmail.getText().toString();
            String password = binding.etSignupPassword.getText().toString();

            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
                return;
            }

            long id = dbHelper.addUser(username, degree, year, email, password);
            if (id > 0) {
                Toast.makeText(requireContext(), "Account created successfully!", Toast.LENGTH_SHORT).show();
                Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_navigation_home);
            } else {
                Toast.makeText(requireContext(), "Error creating account", Toast.LENGTH_SHORT).show();
            }
        });

        binding.tvLogin.setOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}