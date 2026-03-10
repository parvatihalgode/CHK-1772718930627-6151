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
        binding.rvLeaderboard.setHasFixedSize(true);
        binding.rvLeaderboard.setAdapter(adapter);

        fetchRealLeaderboardData();
    }

    private void fetchRealLeaderboardData() {
        // Query top users sorted by score. This only fetches real users from Firebase.
        Query leaderboardQuery = mDatabase.orderByChild("score").limitToLast(50);

        leaderboardQuery.addValueEventListener(new ValueEventListener() {
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
                    // Sort descending: highest score first
                    Collections.sort(scoreList, (a, b) -> b.score - a.score);
                    
                    if (isAdded()) {
                        updatePodium();
                        adapter.notifyDataSetChanged();
                    }
                } else {
                    // Reset podium if no data exists
                    resetPodium();
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                if (isAdded()) {
                    Toast.makeText(requireContext(), "Sync Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updatePodium() {
        // Clear previous podium data
        resetPodium();

        if (scoreList.size() >= 1) {
            binding.tvRank1Name.setText(scoreList.get(0).username);
            binding.tvRank1Initials.setText(getInitials(scoreList.get(0).username));
        }
        if (scoreList.size() >= 2) {
            binding.tvRank2Name.setText(scoreList.get(1).username);
            binding.tvRank2Initials.setText(getInitials(scoreList.get(1).username));
        }
        if (scoreList.size() >= 3) {
            binding.tvRank3Name.setText(scoreList.get(2).username);
            binding.tvRank3Initials.setText(getInitials(scoreList.get(2).username));
        }
    }

    private void resetPodium() {
        binding.tvRank1Name.setText("-");
        binding.tvRank1Initials.setText("1");
        binding.tvRank2Name.setText("-");
        binding.tvRank2Initials.setText("2");
        binding.tvRank3Name.setText("-");
        binding.tvRank3Initials.setText("3");
    }

    private String getInitials(String name) {
        if (name == null || name.isEmpty()) return "?";
        if (name.length() >= 2) return name.substring(0, 2).toUpperCase();
        return name.toUpperCase();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
