package com.example.techvidyaaa.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseHelper dbHelper;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSignupBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
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
            animatePop(v, () -> {
                String username = binding.etUsername.getText().toString().trim();
                String degree = binding.etDegree.getText().toString().trim();
                String year = binding.actvYear.getText().toString().trim();
                String email = binding.etSignupEmail.getText().toString().trim();
                String password = binding.etSignupPassword.getText().toString().trim();

                if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill all required fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(requireContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    return;
                }

                binding.btnSignup.setEnabled(false);

                // Performance: Save to local SQLite in background
                executorService.execute(() -> {
                    dbHelper.addUser(username, degree, year, email, password);
                    
                    mainHandler.post(() -> {
                        if (isNetworkAvailable()) {
                            // Online Signup
                            mAuth.createUserWithEmailAndPassword(email, password)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            if (user != null) {
                                                Map<String, Object> userMap = new HashMap<>();
                                                userMap.put("username", username);
                                                userMap.put("degree", degree);
                                                userMap.put("year", year);
                                                userMap.put("email", email);
                                                mDatabase.child("users").child(user.getUid()).setValue(userMap);
                                            }
                                            Toast.makeText(requireContext(), "Signup Successful!", Toast.LENGTH_SHORT).show();
                                            if (isAdded()) {
                                                Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_navigation_home);
                                            }
                                        } else {
                                            handleSignupError(task.getException(), view);
                                        }
                                    });
                        } else {
                            // Offline Signup
                            Toast.makeText(requireContext(), "Offline Mode: Signup saved locally.", Toast.LENGTH_LONG).show();
                            if (isAdded()) {
                                Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_navigation_home);
                            }
                        }
                    });
                });
            });
        });

        binding.tvLogin.setOnClickListener(v -> {
            animatePop(v, () -> Navigation.findNavController(view).popBackStack());
        });
    }

    private void animatePop(View v, Runnable endAction) {
        v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction(() -> {
            v.animate().scaleX(1.0f).scaleY(1.0f).setDuration(100).withEndAction(endAction).start();
        }).start();
    }

    private void handleSignupError(Exception e, View view) {
        binding.btnSignup.setEnabled(true);
        if (e instanceof FirebaseAuthException) {
            String errorCode = ((FirebaseAuthException) e).getErrorCode();
            if (errorCode.equals("ERROR_EMAIL_ALREADY_IN_USE")) {
                Toast.makeText(requireContext(), "Email already in use. Please login.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(requireContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(requireContext(), "Signup delay. Access granted locally.", Toast.LENGTH_LONG).show();
            if (isAdded()) {
                Navigation.findNavController(view).navigate(R.id.action_signupFragment_to_navigation_home);
            }
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo net = cm.getActiveNetworkInfo();
        return net != null && net.isConnected();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        executorService.shutdown();
    }
}
