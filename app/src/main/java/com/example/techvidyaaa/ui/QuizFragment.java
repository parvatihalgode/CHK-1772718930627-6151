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
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QuizFragment extends Fragment {

    private FragmentQuizBinding binding;
    private List<QuestionEntity> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private int correctCount = 0;
    private String subjectName;
    private String difficulty;
    private boolean isQuizMode = false;
    private long startTime;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    
    private final Map<Integer, Integer> selectedAnswers = new HashMap<>();
    private CountDownTimer countDownTimer;
    private static final long QUIZ_TIME_LIMIT = 5 * 60 * 1000; // 5 minutes

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            subjectName = getArguments().getString("subjectName");
            difficulty = getArguments().getString("difficulty", "Easy");
            isQuizMode = getArguments().getBoolean("isQuiz", false);
        }
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        String titlePrefix = isQuizMode ? "Quiz: " : "Practice: ";
        binding.tvQuizTitle.setText(titlePrefix + subjectName + " (" + difficulty + ")");
        
        questionList = new ArrayList<>();
        startTime = System.currentTimeMillis();

        loadQuestionsFromCache();
        
        // Timer logic: ONLY for Quiz Mode
        if (isQuizMode) {
            binding.tvTimer.setVisibility(View.VISIBLE);
            startTimer();
        } else {
            binding.tvTimer.setVisibility(View.GONE);
        }

        binding.btnNext.setOnClickListener(v -> {
            storeCurrentAnswer();
            if (currentQuestionIndex < questionList.size() - 1) {
                currentQuestionIndex++;
                displayQuestion();
            } else {
                calculateFinalScoreAndFinish();
            }
        });

        binding.btnPrevious.setOnClickListener(v -> {
            storeCurrentAnswer();
            if (currentQuestionIndex > 0) {
                currentQuestionIndex--;
                displayQuestion();
            }
        });
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(QUIZ_TIME_LIMIT, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutes = (int) (millisUntilFinished / 1000) / 60;
                int seconds = (int) (millisUntilFinished / 1000) % 60;
                String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                if (binding != null) {
                    binding.tvTimer.setText(timeLeft);
                }
            }

            @Override
            public void onFinish() {
                if (binding != null) {
                    binding.tvTimer.setText("00:00");
                    Toast.makeText(requireContext(), "Time's up!", Toast.LENGTH_SHORT).show();
                    storeCurrentAnswer();
                    calculateFinalScoreAndFinish();
                }
            }
        }.start();
    }

    private void loadQuestionsFromCache() {
        executorService.execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(requireContext());
            List<QuestionEntity> questions = db.questionDao().getQuestionsBySubjectAndDifficulty(subjectName, difficulty);
            mainHandler.post(() -> {
                if (questions != null && !questions.isEmpty()) {
                    questionList.clear();
                    // For Quick Quiz, limit to 10. For Practice, show all.
                    int limit = isQuizMode ? Math.min(questions.size(), 10) : questions.size();
                    questionList.addAll(questions.subList(0, limit));
                    displayQuestion();
                } else {
                    Toast.makeText(requireContext(), "No questions found for " + subjectName, Toast.LENGTH_SHORT).show();
                    Navigation.findNavController(requireView()).popBackStack();
                }
            });
        });
    }

    private void displayQuestion() {
        if (questionList.isEmpty() || binding == null) return;

        QuestionEntity q = questionList.get(currentQuestionIndex);
        binding.tvQuestion.setText(q.question);
        binding.rbOption1.setText(q.option1);
        binding.rbOption2.setText(q.option2);
        binding.rbOption3.setText(q.option3);
        binding.rbOption4.setText(q.option4);
        
        binding.rgOptions.clearCheck();
        if (selectedAnswers.containsKey(currentQuestionIndex)) {
            int savedIndex = selectedAnswers.get(currentQuestionIndex);
            if (savedIndex == 0) binding.rbOption1.setChecked(true);
            else if (savedIndex == 1) binding.rbOption2.setChecked(true);
            else if (savedIndex == 2) binding.rbOption3.setChecked(true);
            else if (savedIndex == 3) binding.rbOption4.setChecked(true);
        }

        binding.quizProgress.setProgress((int) (((float) (currentQuestionIndex + 1) / questionList.size()) * 100));
        binding.btnPrevious.setVisibility(currentQuestionIndex > 0 ? View.VISIBLE : View.GONE);
        
        if (currentQuestionIndex == questionList.size() - 1) {
            binding.btnNext.setText("Submit & Finish");
        } else {
            binding.btnNext.setText("Next Question");
        }
    }

    private void storeCurrentAnswer() {
        if (binding == null) return;
        int checkedId = binding.rgOptions.getCheckedRadioButtonId();
        int answerIndex = -1;
        if (checkedId == binding.rbOption1.getId()) answerIndex = 0;
        else if (checkedId == binding.rbOption2.getId()) answerIndex = 1;
        else if (checkedId == binding.rbOption3.getId()) answerIndex = 2;
        else if (checkedId == binding.rbOption4.getId()) answerIndex = 3;
        
        if (answerIndex != -1) {
            selectedAnswers.put(currentQuestionIndex, answerIndex);
        }
    }

    private void calculateFinalScoreAndFinish() {
        if (countDownTimer != null) countDownTimer.cancel();
        
        score = 0;
        correctCount = 0;
        int pointsPerQuestion;
        
        if ("Medium".equalsIgnoreCase(difficulty)) {
            pointsPerQuestion = 20;
        } else if ("Hard".equalsIgnoreCase(difficulty)) {
            pointsPerQuestion = 30;
        } else {
            pointsPerQuestion = 10;
        }

        for (int i = 0; i < questionList.size(); i++) {
            Integer selected = selectedAnswers.get(i);
            if (selected != null && selected == questionList.get(i).correctAnswerIndex) {
                score += pointsPerQuestion;
                correctCount++;
            }
        }
        
        String userId = FirebaseAuth.getInstance().getUid();
        long durationMillis = System.currentTimeMillis() - startTime;
        int durationMinutes = (int) (durationMillis / (1000 * 60));
        if (durationMinutes == 0) durationMinutes = 1;

        if (userId != null) {
            DatabaseReference statsRef = FirebaseDatabase.getInstance().getReference("user_stats").child(userId);
            DatabaseReference leaderboardRef = FirebaseDatabase.getInstance().getReference("leaderboard").child(userId);

            String activityType = isQuizMode ? "Quick Quiz" : "Practice";
            
            String activityId = statsRef.child("activity_log").push().getKey();
            Map<String, Object> activityData = new HashMap<>();
            activityData.put("timestamp", ServerValue.TIMESTAMP);
            activityData.put("duration", durationMinutes);
            activityData.put("type", activityType + ": " + subjectName); 
            activityData.put("questionsSolved", correctCount);
            activityData.put("scoreEarned", score);
            if (activityId != null) statsRef.child("activity_log").child(activityId).setValue(activityData);

            Map<String, Object> statsUpdates = new HashMap<>();
            statsUpdates.put("totalLearningTime", ServerValue.increment(durationMinutes));
            statsUpdates.put("quizzesTaken", ServerValue.increment(1));
            statsUpdates.put("totalQuestionsSolved", ServerValue.increment(correctCount));
            statsUpdates.put("practiceTime", ServerValue.increment(durationMinutes)); // Track practice time specifically
            statsUpdates.put("lastActive", ServerValue.TIMESTAMP);
            statsRef.updateChildren(statsUpdates);

            leaderboardRef.child("score").setValue(ServerValue.increment(score));
        }

        if (isAdded()) {
            Bundle resultArgs = new Bundle();
            resultArgs.putInt("score", score);
            resultArgs.putInt("total", questionList.size());
            resultArgs.putInt("correctCount", correctCount);
            Navigation.findNavController(requireView()).navigate(R.id.action_quizFragment_to_resultFragment, resultArgs);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (countDownTimer != null) countDownTimer.cancel();
        binding = null;
        executorService.shutdown();
    }
}
