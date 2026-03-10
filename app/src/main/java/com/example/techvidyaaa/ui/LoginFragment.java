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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentLoginBinding;
import com.example.techvidyaaa.db.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding binding;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseHelper dbHelper;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        dbHelper = new DatabaseHelper(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mAuth.getCurrentUser() != null) {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_navigation_home);
        }

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter credentials", Toast.LENGTH_SHORT).show();
                return;
            }

            binding.btnLogin.setEnabled(false);
            binding.progressBar.setVisibility(View.VISIBLE);

            // Performance Fix: Run database check in background thread
            executorService.execute(() -> {
                boolean userExists = dbHelper.checkUser(email, password);
                
                mainHandler.post(() -> {
                    if (userExists) {
                        Toast.makeText(requireContext(), "Logging in...", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_navigation_home);
                        
                        // Sync in background if online
                        if (isNetworkAvailable()) {
                            mAuth.signInWithEmailAndPassword(email, password);
                        }
                    } else if (isNetworkAvailable()) {
                        // Online only check
                        mAuth.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(task -> {
                                    binding.progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        syncUserFromFirebase(email, password);
                                    } else {
                                        Toast.makeText(requireContext(), "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                        binding.btnLogin.setEnabled(true);
                                    }
                                });
                    } else {
                        binding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(requireContext(), "No local record found. Please connect to internet to login.", Toast.LENGTH_LONG).show();
                        binding.btnLogin.setEnabled(true);
                    }
                });
            });
        });

        binding.tvSignup.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signupFragment);
        });
    }

    private void syncUserFromFirebase(String email, String password) {
        if (mAuth.getCurrentUser() == null) return;
        String userId = mAuth.getCurrentUser().getUid();
        mDatabase.child("users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String username = snapshot.child("username").getValue(String.class);
                    String degree = snapshot.child("degree").getValue(String.class);
                    String year = snapshot.child("year").getValue(String.class);
                    
                    // Save to local DB in background
                    executorService.execute(() -> dbHelper.addUser(username, degree, year, email, password));
                }
                if (isAdded()) {
                    Navigation.findNavController(requireView()).navigate(R.id.action_loginFragment_to_navigation_home);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
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
    }
}
