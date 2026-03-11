package com.example.techvidyaaa.db;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "questions")
public class QuestionEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    
    public String subject;
    public String difficulty; // "Easy", "Medium", "Hard"
    public String question;
    public String option1;
    public String option2;
    public String option3;
    public String option4;
    public int correctAnswerIndex;

    public QuestionEntity(String subject, String difficulty, String question, String option1, String option2, String option3, String option4, int correctAnswerIndex) {
        this.subject = subject;
        this.difficulty = difficulty;
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctAnswerIndex = correctAnswerIndex;
    }
}
