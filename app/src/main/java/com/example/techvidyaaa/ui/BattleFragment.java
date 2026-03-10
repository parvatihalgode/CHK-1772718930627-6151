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
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.techvidyaaa.databinding.FragmentBattleBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class BattleFragment extends Fragment {

    private FragmentBattleBinding binding;
    private DatabaseReference battlesRef, subjectsRef;
    private BattleAdapter battleAdapter;
    private List<Battle> battleList;
    private List<String> subjectList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBattleBinding.inflate(inflater, container, false);
        battlesRef = FirebaseDatabase.getInstance().getReference("battles");
        subjectsRef = FirebaseDatabase.getInstance().getReference("subject");
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Setup RecyclerView
        battleList = new ArrayList<>();
        battleAdapter = new BattleAdapter(battleList, battle -> {
            // Join Battle Logic
            Toast.makeText(getContext(), "Joining " + battle.topic + " battle!", Toast.LENGTH_SHORT).show();
        });
        binding.rvAvailableBattles.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.rvAvailableBattles.setAdapter(battleAdapter);

        // Setup Autocomplete
        subjectList = new ArrayList<>();
        fetchSubjects();
        
        // Listen for new battles
        listenForAvailableBattles();

        binding.btnCreateBattle.setOnClickListener(v -> createNewBattle());
    }

    private void fetchSubjects() {
        subjectsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Object val = data.getValue();
                        if (val != null) subjectList.add(val.toString());
                    }
                    if (isAdded() && getContext() != null) {
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, subjectList);
                        binding.actvBattleTopic.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void listenForAvailableBattles() {
        battlesRef.orderByChild("status").equalTo("waiting").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                battleList.clear();
                for (DataSnapshot battleSnapshot : snapshot.getChildren()) {
                    Battle battle = battleSnapshot.getValue(Battle.class);
                    if (battle != null) {
                        battleList.add(battle);
                    }
                }
                if (isAdded()) {
                    battleAdapter.notifyDataSetChanged();
                }
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
        String username = "User";
        if (FirebaseAuth.getInstance().getCurrentUser() != null && FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
            username = FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0];
        }
        String battleId = "Battle #" + (int)(Math.random() * 1000000);

        Battle newBattle = new Battle(battleId, topic, userId, username, "waiting", 1, 2, 5);

        battlesRef.push().setValue(newBattle)
                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(), "Battle created!", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to create battle", Toast.LENGTH_SHORT).show());
    }

    // Battle Data Class
    public static class Battle {
        public String battleId, topic, creatorId, creatorName, status;
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
