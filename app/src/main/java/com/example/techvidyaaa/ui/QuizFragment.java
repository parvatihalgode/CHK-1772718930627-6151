package com.example.techvidyaaa.ui;

import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class QuizFragment extends Fragment {

    private static final String TAG = "QuizFragment";
    private FragmentQuizBinding binding;
    private DatabaseReference mDatabase;
    private List<Question> questionList;
    private int currentQuestionIndex = 0;
    private int score = 0;
    private String subjectName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        if (getArguments() != null) {
            subjectName = getArguments().getString("subjectName");
        }
        mDatabase = FirebaseDatabase.getInstance().getReference("questions").child(subjectName);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.tvQuizTitle.setText(subjectName + " Practice");
        questionList = new ArrayList<>();
        loadQuestions();

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

    private void loadQuestions() {
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot data : snapshot.getChildren()) {
                        Question q = data.getValue(Question.class);
                        if (q != null) questionList.add(q);
                    }
                    if (!questionList.isEmpty()) {
                        displayQuestion();
                    } else {
                        Toast.makeText(requireContext(), "No questions found for this topic", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(requireView()).popBackStack();
                    }
                } else {
                    seedDummyQuestions(); // For testing if DB is empty
                    displayQuestion();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {}
        });
    }

    private void seedDummyQuestions() {
        questionList.add(new Question("What is " + subjectName + "?", "A tool", "A concept", "A language", "All of above", 3));
        questionList.add(new Question("Is " + subjectName + " important?", "Yes", "No", "Maybe", "Don't know", 0));
    }

    private void displayQuestion() {
        Question q = questionList.get(currentQuestionIndex);
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
        if (userId != null) {
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("leaderboard").child(userId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    int currentTotal = 0;
                    if (snapshot.exists() && snapshot.hasChild("score")) {
                        currentTotal = snapshot.child("score").getValue(Integer.class);
                    }
                    userRef.child("score").setValue(currentTotal + score);
                    userRef.child("username").setValue(FirebaseAuth.getInstance().getCurrentUser().getEmail().split("@")[0]);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {}
            });
        }
        Toast.makeText(requireContext(), "Quiz Finished! Points Earned: " + score, Toast.LENGTH_LONG).show();
        Navigation.findNavController(requireView()).popBackStack();
    }

    public static class Question {
        public String question, option1, option2, option3, option4;
        public int correctAnswerIndex;

        public Question() {}
        public Question(String q, String o1, String o2, String o3, String o4, int correct) {
            this.question = q; this.option1 = o1; this.option2 = o2; this.option3 = o3; this.option4 = o4;
            this.correctAnswerIndex = correct;
        }
    }
}
