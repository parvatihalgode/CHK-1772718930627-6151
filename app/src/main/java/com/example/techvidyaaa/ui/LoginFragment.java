package com.example.techvidyaaa.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentLoginBinding;
import com.example.techvidyaaa.db.DatabaseHelper;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString();
            String password = binding.etPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Enter credentials", Toast.LENGTH_SHORT).show();
                return;
            }

            if (dbHelper.checkUser(email, password)) {
                // Fetch the user to verify it's working
                Cursor cursor = dbHelper.getUserByEmail(email);
                if (cursor != null && cursor.moveToFirst()) {
                    String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                    Toast.makeText(requireContext(), "Welcome " + username, Toast.LENGTH_SHORT).show();
                    cursor.close();
                }
                
                Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_navigation_home);
            } else {
                Toast.makeText(requireContext(), "Invalid Email or Password", Toast.LENGTH_SHORT).show();
            }
        });

        binding.tvSignup.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signupFragment);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}