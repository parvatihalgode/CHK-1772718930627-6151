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
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentLoginBinding;
import com.example.techvidyaaa.db.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
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

        // Safety: Double check if already logged in (MainActivity should handle this, but this is a fail-safe)
        if (mAuth.getCurrentUser() != null) {
            navigateToHome();
            return;
        }

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            setLoading(true);

            // Primary Safety: Use Firebase for Authentication
            if (isNetworkAvailable()) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                syncUserFromFirebase(email, password);
                            } else {
                                setLoading(false);
                                handleAuthError(task.getException());
                            }
                        });
            } else {
                // Secondary: Check local DB if offline
                executorService.execute(() -> {
                    boolean userExists = dbHelper.checkUser(email, password);
                    mainHandler.post(() -> {
                        setLoading(false);
                        if (userExists) {
                            navigateToHome();
                        } else {
                            Toast.makeText(requireContext(), "No internet connection and no local user found.", Toast.LENGTH_LONG).show();
                        }
                    });
                });
            }
        });

        binding.tvSignup.setOnClickListener(v -> {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_signupFragment);
        });
    }

    private void handleAuthError(Exception e) {
        String message = "Authentication failed.";
        if (e instanceof FirebaseAuthInvalidUserException) {
            message = "No account found with this email.";
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            message = "Invalid password.";
        } else if (e != null && e.getMessage() != null && e.getMessage().contains("CONFIGURATION_NOT_FOUND")) {
            message = "Server error: Please ensure Email/Password login is enabled in Firebase Console.";
        } else if (e != null) {
            message = e.getMessage();
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
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
                    
                    executorService.execute(() -> dbHelper.addUser(username, degree, year, email, password));
                }
                navigateToHome();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                navigateToHome(); // Navigate anyway if auth succeeded
            }
        });
    }

    private void navigateToHome() {
        if (!isAdded()) return;
        NavController navController = Navigation.findNavController(requireView());
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.loginFragment, true)
                .build();
        navController.navigate(R.id.navigation_home, null, navOptions);
    }

    private void setLoading(boolean isLoading) {
        binding.btnLogin.setEnabled(!isLoading);
        binding.progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
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
