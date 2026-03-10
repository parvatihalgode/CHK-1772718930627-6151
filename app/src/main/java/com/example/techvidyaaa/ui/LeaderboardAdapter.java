package com.example.techvidyaaa.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techvidyaaa.R;

import java.util.List;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    private final List<UserScore> userScores;

    public LeaderboardAdapter(List<UserScore> userScores) {
        this.userScores = userScores;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Since podium handles top 3, the list starts from rank 4
        int rank = position + 4;
        UserScore score = userScores.get(position);
        holder.tvRank.setText(String.valueOf(rank));
        holder.tvName.setText(score.username);
        holder.tvScore.setText(String.valueOf(score.score));
        holder.tvUserStatus.setText("Active Learner");
    }

    @Override
    public int getItemCount() {
        return userScores.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvName, tvScore, tvUserStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tvRank);
            tvName = itemView.findViewById(R.id.tvName);
            tvScore = itemView.findViewById(R.id.tvScore);
            tvUserStatus = itemView.findViewById(R.id.tvUserStatus);
        }
    }

    public static class UserScore {
        public String username;
        public int score;

        public UserScore() {} // Required for Firebase

        public UserScore(String username, int score) {
            this.username = username;
            this.score = score;
        }
    }
}
