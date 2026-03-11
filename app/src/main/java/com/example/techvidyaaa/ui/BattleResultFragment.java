package com.example.techvidyaaa.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentBattleResultBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

public class BattleResultFragment extends Fragment {

    private FragmentBattleResultBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBattleResultBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            long myScore = getArguments().getLong("myScore");
            long opScore = getArguments().getLong("opScore");
            long myTime = getArguments().getLong("myTime");
            long opTime = getArguments().getLong("opTime");

            binding.tvMyFinalScore.setText(String.valueOf(myScore));
            binding.tvOpponentFinalScore.setText(String.valueOf(opScore));
            binding.tvTimeTaken.setText("Time taken: " + myTime + "s");

            boolean iWon = false;
            if (myScore > opScore) {
                iWon = true;
            } else if (myScore == opScore) {
                if (myTime < opTime) {
                    iWon = true;
                }
            }

            if (iWon) {
                binding.tvBattleStatus.setText("YOU WIN!");
                binding.tvBattleStatus.setTextColor(requireContext().getColor(android.R.color.holo_green_dark));
                binding.ivResultIcon.setImageResource(R.drawable.ic_battle);
                binding.ivResultIcon.setColorFilter(requireContext().getColor(android.R.color.holo_orange_light));
                binding.tvBattleMessage.setText("Incredible performance!");
                updateLeaderboard(myScore);
            } else {
                binding.tvBattleStatus.setText("YOU LOSE");
                binding.tvBattleStatus.setTextColor(requireContext().getColor(android.R.color.holo_red_dark));
                binding.ivResultIcon.setImageResource(R.drawable.ic_battle);
                binding.ivResultIcon.setColorFilter(requireContext().getColor(android.R.color.darker_gray));
                binding.tvBattleMessage.setText("Don't give up! Try again.");
            }
        }

        binding.btnBackHome.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.navigation_battle);
        });
    }

    private void updateLeaderboard(long score) {
        String userId = FirebaseAuth.getInstance().getUid();
        if (userId != null) {
            DatabaseReference leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard").child(userId);
            leaderboardRef.child("score").setValue(ServerValue.increment(score));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
