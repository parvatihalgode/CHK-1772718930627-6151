package com.example.techvidyaaa.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.techvidyaaa.databinding.FragmentQuizBinding;
import com.example.techvidyaaa.db.AppDatabase;
import com.example.techvidyaaa.db.QuestionEntity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizFragment extends Fragment {

    private FragmentQuizBinding binding;
    private List<QuestionEntity> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private String subjectName;
    private long startTime;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            subjectName = getArguments().getString("subjectName");
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvQuizTitle.setText(subjectName + " Practice");
        questionList = new ArrayList<>();
        startTime = System.currentTimeMillis();

        loadQuestionsFromCache();

        binding.btnNext.setOnClickListener(v -> {
            checkAnswer();
            if (currentQuestionIndex < questionList.size() - 1) {
                currentQuestionIndex++;
                displayQuestion();
            } else {
                finishQuiz();
            }
        });
    }

    private void loadQuestionsFromCache() {
        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(requireContext());
            List<QuestionEntity> questions = db.questionDao().getQuestionsBySubject(subjectName);
            mainHandler.post(() -> {
                if (questions != null && !questions.isEmpty()) {
                    questionList.clear();
                    questionList.addAll(questions);
                    displayQuestion();
                } else {
                    Toast.makeText(requireContext(), "No questions found for this topic. Syncing from CDN...", Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireView()).popBackStack();
                }
            });
        });
    }

    private void displayQuestion() {
        QuestionEntity q = questionList.get(currentQuestionIndex);
        binding.tvQuestion.setText(q.question);
        binding.rbOption1.setText(q.option1);
        binding.rbOption2.setText(q.option2);
        binding.rbOption3.setText(q.option3);
        binding.rbOption4.setText(q.option4);
        binding.rgOptions.clearCheck();
        binding.quizProgress.setProgress((int) (((float) (currentQuestionIndex + 1) / questionList.size()) * 100));

        if (currentQuestionIndex == questionList.size() - 1) {
            binding.btnNext.setText("Finish Quiz");
        }
    }

    private void checkAnswer() {
        int checkedId = binding.rgOptions.getCheckedRadioButtonId();
        int answerIndex = -1;
        if (checkedId == binding.rbOption1.getId()) answerIndex = 0;
        else if (checkedId == binding.rbOption2.getId()) answerIndex = 1;
        else if (checkedId == binding.rbOption3.getId()) answerIndex = 2;
        else if (checkedId == binding.rbOption4.getId()) answerIndex = 3;

        if (answerIndex == questionList.get(currentQuestionIndex).correctAnswerIndex) {
            score += 10;
        }
    }

    private void finishQuiz() {
        String userId = FirebaseAuth.getInstance().getUid();
        long durationMillis = System.currentTimeMillis() - startTime;
        int durationMinutes = (int) (durationMillis / (1000 * 60));
        if (durationMinutes == 0) durationMinutes = 1; // Minimum 1 minute

        if (userId != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("leaderboard").child(userId);
            DatabaseReference statsRef = FirebaseDatabase.getInstance().getReference("user_stats").child(userId);

            Map<String, Object> leaderboardUpdates = new HashMap<>();
            leaderboardUpdates.put("score", ServerValue.increment(score));
            leaderboardUpdates.put("username", FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0]);
            userRef.updateChildren(leaderboardUpdates);

            Map<String, Object> statsUpdates = new HashMap<>();
            statsUpdates.put("totalLearningTime", ServerValue.increment(durationMinutes));
            statsUpdates.put("quizzesTaken", ServerValue.increment(1));
            statsUpdates.put("lastActive", ServerValue.TIMESTAMP);
            statsRef.updateChildren(statsUpdates);
        }

        Toast.makeText(requireContext(), "Quiz Finished! Points Earned: " + score, Toast.LENGTH_LONG).show();
        Navigation.findNavController(requireView()).popBackStack();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        executorService.shutdown();
    }
}
