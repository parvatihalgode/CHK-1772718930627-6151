package com.example.techvidyaaa.ui;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentSignupBinding;
import com.example.techvidyaaa.db.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
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
    private static final String TAG = "SignupFragment";

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

            setLoading(true);

            if (isNetworkAvailable()) {
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
                                    mDatabase.child("users").child(user.getUid()).setValue(userMap)
                                            .addOnCompleteListener(dbTask -> {
                                                if (!dbTask.isSuccessful()) {
                                                    Log.e(TAG, "Database error: " + dbTask.getException());
                                                }
                                            });
                                    
                                    // Save locally as well
                                    executorService.execute(() -> dbHelper.addUser(username, degree, year, email, password));
                                }
                                Toast.makeText(requireContext(), "Signup Successful!", Toast.LENGTH_SHORT).show();
                                navigateToHome();
                            } else {
                                setLoading(false);
                                handleSignupError(task.getException());
                            }
                        });
            } else {
                setLoading(false);
                Toast.makeText(requireContext(), "Internet connection required for first-time signup.", Toast.LENGTH_LONG).show();
            }
        });

        binding.tvLogin.setOnClickListener(v -> {
            Navigation.findNavController(view).popBackStack();
        });
    }

    private void handleSignupError(Exception e) {
        String message = "Signup failed.";
        Log.e(TAG, "Auth error: ", e);
        if (e instanceof FirebaseAuthUserCollisionException) {
            message = "This email is already registered. Please login.";
        } else if (e != null && e.getMessage() != null) {
            if (e.getMessage().contains("CONFIGURATION_NOT_FOUND")) {
                message = "Sign-in method not enabled. Go to Firebase Console > Authentication > Sign-in method and enable Email/Password.";
            } else if (e.getMessage().contains("INVALID_API_KEY")) {
                message = "Invalid API Key in google-services.json.";
            } else {
                message = e.getMessage();
            }
        }
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show();
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
        binding.btnSignup.setEnabled(!isLoading);
        binding.progressBarSignup.setVisibility(isLoading ? View.VISIBLE : View.GONE);
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
