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

    @Query("SELECT * FROM questions WHERE subject = :subject")
    List<QuestionEntity> getQuestionsBySubject(String subject);

    @Query("SELECT COUNT(*) FROM questions")
    int getQuestionCount();
}
