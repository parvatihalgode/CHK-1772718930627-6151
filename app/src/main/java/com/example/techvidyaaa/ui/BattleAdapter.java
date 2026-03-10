package com.example.techvidyaaa.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.techvidyaaa.R;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class BattleAdapter extends RecyclerView.Adapter<BattleAdapter.ViewHolder> {

    private final List<BattleFragment.Battle> battles;
    private final OnJoinClickListener listener;

    public interface OnJoinClickListener {
        void onJoinClick(BattleFragment.Battle battle);
    }

    public BattleAdapter(List<BattleFragment.Battle> battles, OnJoinClickListener listener) {
        this.battles = battles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_battle, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BattleFragment.Battle battle = battles.get(position);
        holder.tvBattleId.setText(battle.battleId);
        holder.tvPlayerCount.setText(battle.playerCount + "/" + battle.maxPlayers);
        holder.tvDuration.setText("5 min"); // Example static data
        holder.tvQuestionCount.setText(battle.questionCount + " questions");
        
        holder.btnJoinBattle.setOnClickListener(v -> {
            if (listener != null) {
                listener.onJoinClick(battle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return battles.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBattleId, tvPlayerCount, tvDuration, tvQuestionCount;
        MaterialButton btnJoinBattle;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBattleId = itemView.findViewById(R.id.tvBattleId);
            tvPlayerCount = itemView.findViewById(R.id.tvPlayerCount);
            tvDuration = itemView.findViewById(R.id.tvDuration);
            tvQuestionCount = itemView.findViewById(R.id.tvQuestionCount);
            btnJoinBattle = itemView.findViewById(R.id.btnJoinBattle);
        }
    }
}
