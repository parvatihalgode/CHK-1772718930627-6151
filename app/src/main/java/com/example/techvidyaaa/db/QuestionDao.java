package com.example.techvidyaaa.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuestionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<QuestionEntity> questions);

    @Query("SELECT * FROM questions WHERE subject = :subject AND difficulty = :difficulty")
    List<QuestionEntity> getQuestionsBySubjectAndDifficulty(String subject, String difficulty);

    @Query("SELECT COUNT(*) FROM questions")
    int getQuestionCount();
}
