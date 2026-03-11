package com.example.techvidyaaa.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentBattleBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BattleFragment extends Fragment {

    private FragmentBattleBinding binding;
    private DatabaseReference battlesRef;
    private BattleAdapter battleAdapter;
    private List<Battle> battleList;
    private List<String> subjectList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBattleBinding.inflate(inflater, container, false);
        // Persistence is already enabled in TechVidyaApp class
        battlesRef = FirebaseDatabase.getInstance().getReference("battles");
        // battlesRef.keepSynced(true); // Moved to onViewCreated to avoid issues if needed
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        battlesRef.keepSynced(true);
        battleList = new ArrayList<>();
        battleAdapter = new BattleAdapter(battleList, this::joinBattle);
        binding.rvAvailableBattles.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvAvailableBattles.setAdapter(battleAdapter);

        setupSubjectList();
        listenForAvailableBattles();

        binding.btnCreateBattle.setOnClickListener(v -> createNewBattle());
    }

    private void setupSubjectList() {
        subjectList = new ArrayList<>();
        subjectList.add("C Programming");
        subjectList.add("Python");
        subjectList.add("Java");
        subjectList.add("C++");
        subjectList.add("Data Structures");
        subjectList.add("Algorithms");
        subjectList.add("Database Management");
        subjectList.add("Web Development");

        if (getContext() != null) {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, subjectList);
            binding.actvBattleTopic.setAdapter(adapter);
        }
    }

    private void listenForAvailableBattles() {
        battlesRef.orderByChild("status").equalTo("waiting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (binding == null) return;
                battleList.clear();
                for (DataSnapshot battleSnapshot : snapshot.getChildren()) {
                    Battle battle = battleSnapshot.getValue(Battle.class);
                    if (battle != null) {
                        battle.firebaseKey = battleSnapshot.getKey();
                        battleList.add(battle);
                    }
                }
                if (isAdded()) battleAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void createNewBattle() {
        String topic = binding.actvBattleTopic.getText().toString();
        if (topic.isEmpty()) {
            Toast.makeText(getContext(), "Please select a topic", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getUid();
        String username = "Player";
        if (FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
            username = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0];
        }
        String battleId = "BT-" + System.currentTimeMillis() % 10000;

        DatabaseReference newBattleRef = battlesRef.push();
        Battle newBattle = new Battle(battleId, topic, userId, username, "waiting", 1, 2, 5);
        
        newBattleRef.setValue(newBattle).addOnSuccessListener(aVoid -> {
            if (isAdded() && binding != null) {
                Bundle args = new Bundle();
                args.putString("battleId", newBattleRef.getKey());
                args.putString("topic", topic);
                Navigation.findNavController(requireView()).navigate(R.id.action_navigation_battle_to_battleArenaFragment, args);
            }
        });
    }

    private void joinBattle(Battle battle) {
        if (battle.firebaseKey == null) return;
        
        DatabaseReference battleRef = battlesRef.child(battle.firebaseKey);
        Map<String, Object> updates = new HashMap<>();
        updates.put("status", "started");
        updates.put("opponentId", FirebaseAuth.getInstance().getUid());
        updates.put("opponentName", FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0] : "Opponent");
        updates.put("playerCount", 2);

        battleRef.updateChildren(updates).addOnSuccessListener(aVoid -> {
            if (isAdded() && binding != null) {
                Bundle args = new Bundle();
                args.putString("battleId", battle.firebaseKey);
                args.putString("topic", battle.topic);
                Navigation.findNavController(requireView()).navigate(R.id.action_navigation_battle_to_battleArenaFragment, args);
            }
        });
    }

    public static class Battle {
        public String firebaseKey, battleId, topic, creatorId, creatorName, status, opponentId, opponentName;
        public int playerCount, maxPlayers, questionCount;

        public Battle() {}
        public Battle(String id, String t, String cId, String cName, String s, int pCount, int mPlayers, int qCount) {
            this.battleId = id; this.topic = t; this.creatorId = cId; this.creatorName = cName;
            this.status = s; this.playerCount = pCount; this.maxPlayers = mPlayers; this.questionCount = qCount;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
