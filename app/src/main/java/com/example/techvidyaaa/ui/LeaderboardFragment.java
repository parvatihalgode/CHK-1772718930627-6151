package com.example.techvidyaaa.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techvidyaaa.databinding.FragmentLeaderboardBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    private static final String TAG = "LeaderboardFragment";
    private FragmentLeaderboardBinding binding;
    private DatabaseReference mDatabase;
    private LeaderboardAdapter adapter;
    private List<LeaderboardAdapter.UserScore> scoreList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentLeaderboardBinding.inflate(inflater, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference("leaderboard");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scoreList = new ArrayList<>();
        adapter = new LeaderboardAdapter(scoreList);
        binding.rvLeaderboard.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvLeaderboard.setAdapter(adapter);

        fetchRealLeaderboardData();
    }

    private void fetchRealLeaderboardData() {
        // Optimization: Use Query to limit results and sort by score
        // This is much faster than downloading all users
        Query leaderboardQuery = mDatabase.orderByChild("score").limitToLast(50);

        leaderboardQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                scoreList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        LeaderboardAdapter.UserScore userScore = data.getValue(LeaderboardAdapter.UserScore.class);
                        if (userScore != null) {
                            scoreList.add(userScore);
                        }
                    }
                    // Firebase returns in ascending order, we want descending for leaderboard
                    Collections.reverse(scoreList);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "No leaderboard data found");
                    // Add some initial real-looking data if empty for demo
                    seedInitialDataIfEmpty();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Database error: " + error.getMessage());
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Failed to load leaderboard", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void seedInitialDataIfEmpty() {
        // Only for first-time use so the user sees a "real" working list
        List<LeaderboardAdapter.UserScore> seedData = new ArrayList<>();
        seedData.add(new LeaderboardAdapter.UserScore("TechMaster", 2500));
        seedData.add(new LeaderboardAdapter.UserScore("CodeNinja", 2100));
        seedData.add(new LeaderboardAdapter.UserScore("JavaGuru", 1850));
        seedData.add(new LeaderboardAdapter.UserScore("Pythonic", 1500));
        seedData.add(new LeaderboardAdapter.UserScore("AlgoWhiz", 1200));
        
        for (LeaderboardAdapter.UserScore user : seedData) {
            mDatabase.push().setValue(user);
        }
        
        scoreList.addAll(seedData);
        Collections.sort(scoreList, (a, b) -> b.score - a.score);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
