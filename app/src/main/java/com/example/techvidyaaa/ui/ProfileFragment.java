package com.example.techvidyaaa.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.techvidyaaa.databinding.FragmentProfileBinding;
import com.example.techvidyaaa.db.DatabaseHelper;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private DatabaseHelper dbHelper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        dbHelper = new DatabaseHelper(requireContext());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Fetch real data from SQLite
        Cursor cursor = dbHelper.getLatestUser();
        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow("username"));
            String degree = cursor.getString(cursor.getColumnIndexOrThrow("degree"));
            String year = cursor.getString(cursor.getColumnIndexOrThrow("year"));
            String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));

            // Display data in the profile UI
            binding.tvUsername.setText(username);
            binding.tvUserInfo.setText(degree + " | " + year + "\n" + email);
            cursor.close();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}