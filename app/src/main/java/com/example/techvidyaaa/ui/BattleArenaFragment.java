package com.example.techvidyaaa.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.techvidyaaa.R;
import com.example.techvidyaaa.databinding.FragmentBattleArenaBinding;
import com.example.techvidyaaa.db.AppDatabase;
import com.example.techvidyaaa.db.QuestionEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BattleArenaFragment extends Fragment {

    private FragmentBattleArenaBinding binding;
    private DatabaseReference battleRef;
    private String battleId, topic, myId;
    private List<QuestionEntity> questionList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int myScore = 0;
    private long startTime;
    private CountDownTimer countDownTimer;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentBattleArenaBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            battleId = getArguments().getString("battleId");
            topic = getArguments().getString("topic");
        }
        myId = FirebaseAuth.getInstance().getUid();
        battleRef = FirebaseDatabase.getInstance().getReference("battles").child(battleId);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startTime = System.currentTimeMillis();
        loadQuestions();
        listenForNetworkStatus();
        listenToOpponent();

        binding.btnSubmit.setOnClickListener(v -> submitAnswer());
    }

    private void loadQuestions() {
        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(requireContext());
            List<QuestionEntity> questions = db.questionDao().getQuestionsBySubjectAndDifficulty(topic, "Easy");
            if (questions.isEmpty()) questions = db.questionDao().getQuestionsBySubjectAndDifficulty(topic, "Medium");
            
            final List<QuestionEntity> finalQuestions = questions;
            mainHandler.post(() -> {
                if (finalQuestions != null && !finalQuestions.isEmpty()) {
                    questionList.addAll(finalQuestions.subList(0, Math.min(finalQuestions.size(), 5)));
                    displayQuestion();
                    startTimer();
                } else {
                    Toast.makeText(getContext(), "Loading battle questions...", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private void displayQuestion() {
        if (currentQuestionIndex >= questionList.size()) {
            finishBattle();
            return;
        }

        QuestionEntity q = questionList.get(currentQuestionIndex);
        binding.tvQuestionNumber.setText("Question " + (currentQuestionIndex + 1) + "/5");
        binding.tvQuestion.setText(q.question);
        binding.rbOption1.setText(q.option1);
        binding.rbOption2.setText(q.option2);
        binding.rbOption3.setText(q.option3);
        binding.rbOption4.setText(q.option4);
        binding.rgOptions.clearCheck();
    }

    private void startTimer() {
        if (countDownTimer != null) countDownTimer.cancel();
        countDownTimer = new CountDownTimer(30000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                binding.tvTimer.setText(String.format(Locale.getDefault(), "00:%02d", millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                submitAnswer();
            }
        }.start();
    }

    private void submitAnswer() {
        int checkedId = binding.rgOptions.getCheckedRadioButtonId();
        int answerIndex = -1;
        if (checkedId == R.id.rbOption1) answerIndex = 0;
        else if (checkedId == R.id.rbOption2) answerIndex = 1;
        else if (checkedId == R.id.rbOption3) answerIndex = 2;
        else if (checkedId == R.id.rbOption4) answerIndex = 3;

        if (answerIndex == questionList.get(currentQuestionIndex).correctAnswerIndex) {
            myScore += 10;
        }

        battleRef.child("scores").child(myId).setValue(myScore);
        battleRef.child("progress").child(myId).setValue(currentQuestionIndex + 1);

        binding.tvPlayerScore.setText(String.valueOf(myScore));
        binding.playerProgress.setProgress((currentQuestionIndex + 1) * 20);

        currentQuestionIndex++;
        if (currentQuestionIndex < questionList.size()) {
            displayQuestion();
            startTimer();
        } else {
            finishBattle();
        }
    }

    private void listenToOpponent() {
        battleRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists() || binding == null) return;

                String creatorId = snapshot.child("creatorId").getValue(String.class);
                String opponentId = snapshot.child("opponentId").getValue(String.class);
                String targetId = myId.equals(creatorId) ? opponentId : creatorId;
                String targetName = myId.equals(creatorId) ? snapshot.child("opponentName").getValue(String.class) : snapshot.child("creatorName").getValue(String.class);

                if (targetId != null) {
                    binding.tvOpponentName.setText(targetName != null ? targetName : "Opponent");
                    if (snapshot.child("scores").hasChild(targetId)) {
                        long opScore = snapshot.child("scores").child(targetId).getValue(Long.class);
                        binding.tvOpponentScore.setText(String.valueOf(opScore));
                    }
                    if (snapshot.child("progress").hasChild(targetId)) {
                        long opProg = snapshot.child("progress").child(targetId).getValue(Long.class);
                        binding.opponentProgress.setProgress((int) (opProg * 20));
                    }
                }

                if ("finished".equals(snapshot.child("status").getValue(String.class))) {
                    navigateToResult(snapshot);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void listenForNetworkStatus() {
        DatabaseReference connectedRef = FirebaseDatabase.getInstance().getReference(".info/connected");
        connectedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean connected = snapshot.getValue(Boolean.class);
                if (binding != null) {
                    binding.tvNetworkStatus.setVisibility(connected ? View.GONE : View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void finishBattle() {
        if (countDownTimer != null) countDownTimer.cancel();
        long endTime = System.currentTimeMillis();
        long timeTaken = (endTime - startTime) / 1000;
        battleRef.child("timeTaken").child(myId).setValue(timeTaken);
        battleRef.child("status").setValue("finished");
    }

    private void navigateToResult(DataSnapshot snapshot) {
        if (!isAdded()) return;
        
        String creatorId = snapshot.child("creatorId").getValue(String.class);
        String opponentId = snapshot.child("opponentId").getValue(String.class);
        String opId = myId.equals(creatorId) ? opponentId : creatorId;

        if (opId == null || !snapshot.child("scores").hasChild(opId) || !snapshot.child("scores").hasChild(myId)) return;

        long myFinalScore = snapshot.child("scores").child(myId).getValue(Long.class);
        long opFinalScore = snapshot.child("scores").child(opId).getValue(Long.class);
        long myTime = snapshot.child("timeTaken").hasChild(myId) ? snapshot.child("timeTaken").child(myId).getValue(Long.class) : 300;
        long opTime = snapshot.child("timeTaken").hasChild(opId) ? snapshot.child("timeTaken").child(opId).getValue(Long.class) : 300;

        Bundle args = new Bundle();
        args.putLong("myScore", myFinalScore);
        args.putLong("opScore", opFinalScore);
        args.putLong("myTime", myTime);
        args.putLong("opTime", opTime);
        
        Navigation.findNavController(requireView()).navigate(R.id.action_battleArenaFragment_to_battleResultFragment, args);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) countDownTimer.cancel();
        binding = null;
    }
}
