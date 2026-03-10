package com.example.techvidyaaa.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentProfileBinding;
import com.example.techvidyaaa.db.DatabaseHelper;
import com.google.firebase.auth.FirebaseAuth;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private DatabaseHelper dbHelper;
    private FirebaseAuth mAuth;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        mAuth = FirebaseAuth.getInstance();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Performance: Fetch real data from SQLite in background
        executorService.execute(() -> {
            Cursor cursor = dbHelper.getLatestUser();
            if (cursor != null && cursor.moveToFirst()) {
                String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
                String degree = cursor.getString(cursor.getColumnIndexOrThrow("degree"));
                String year = cursor.getString(cursor.getColumnIndexOrThrow("year"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                cursor.close();

                mainHandler.post(() -> {
                    if (binding != null) {
                        // Display data in the profile UI
                        binding.tvUsername.setText(username);
                        binding.tvUserInfo.setText(degree + " | " + year + "\n" + email);
                    }
                });
            }
        });

        binding.btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            // Local data remains, but Firebase auth is cleared. 
            // Navigate back to login - usually a global action or pop back
            Navigation.findNavController(v).navigate(R.id.loginFragment); 
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        executorService.shutdown();
    }
}
