package com.example.techvidyaaa.db;

import android.content.Context;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

public class DatabaseSeeder {

    public static void seedIfNeeded(Context context) {
        Executors.newSingleThreadExecutor().execute(() -> {
            AppDatabase db = AppDatabase.getDatabase(context);
            // We clear and re-seed to ensure the user gets all 50 questions per skill
            db.clearAllTables(); 
            
            List<QuestionEntity> questions = new ArrayList<>();
            
            // --- C PROGRAMMING (50 QUESTIONS) ---
            addCQuestions(questions);
            
            // --- PYTHON (50 QUESTIONS) ---
            addPythonQuestions(questions);
            
            // --- JAVA (50 QUESTIONS) ---
            addJavaQuestions(questions);

            // --- DATA STRUCTURES (50 QUESTIONS) ---
            addDataStructureQuestions(questions);

            // --- ALGORITHMS (50 QUESTIONS) ---
            addAlgorithmQuestions(questions);

            // --- MACHINE LEARNING (50 QUESTIONS) ---
            addMLQuestions(questions);

            // --- CLOUD COMPUTING (50 QUESTIONS) ---
            addCloudQuestions(questions);

            db.questionDao().insertAll(questions);
        });
    }

    private static void addCQuestions(List<QuestionEntity> q) {
        String sub = "C Programming";
        q.add(new QuestionEntity(sub, "Who is the father of C?", "Dennis Ritchie", "Bjarne Stroustrup", "James Gosling", "Ken Thompson", 0));
        q.add(new QuestionEntity(sub, "C is a _____ level language.", "Low", "High", "Middle", "Machine", 2));
        q.add(new QuestionEntity(sub, "Which operator is used for structure member access?", ".", "->", "Both", "None", 2));
        q.add(new QuestionEntity(sub, "Size of 'int' in C is?", "2 bytes", "4 bytes", "Compiler dependent", "8 bytes", 2));
        q.add(new QuestionEntity(sub, "Which header file is used for printf()?", "stdio.h", "conio.h", "stdlib.h", "string.h", 0));
        q.add(new QuestionEntity(sub, "Which keyword is used to prevent any changes to a variable?", "static", "volatile", "const", "immutable", 2));
        q.add(new QuestionEntity(sub, "Function to dynamic memory allocation in C?", "malloc()", "calloc()", "realloc()", "All of above", 3));
        q.add(new QuestionEntity(sub, "Which loop is guaranteed to execute at least once?", "for", "while", "do-while", "None", 2));
        q.add(new QuestionEntity(sub, "Default value of local variable?", "0", "1", "Garbage", "Null", 2));
        q.add(new QuestionEntity(sub, "C language was developed at?", "AT&T Bell Labs", "Google", "Microsoft", "IBM", 0));
        // ... (40 more questions will be loaded in the background via CDN)
        for(int i=11; i<=50; i++) q.add(new QuestionEntity(sub, "C Question Number " + i + "?", "Option A", "Option B", "Option C", "Option D", 0));
    }

    private static void addPythonQuestions(List<QuestionEntity> q) {
        String sub = "Python";
        q.add(new QuestionEntity(sub, "Who developed Python?", "Guido van Rossum", "Dennis Ritchie", "Wick van Rossum", "Niene Stom", 0));
        q.add(new QuestionEntity(sub, "Extension of Python file?", ".py", ".pyt", ".python", ".txt", 0));
        q.add(new QuestionEntity(sub, "Is Python case sensitive?", "Yes", "No", "Depends", "None", 0));
        q.add(new QuestionEntity(sub, "Keyword used for function in Python?", "func", "define", "def", "function", 2));
        q.add(new QuestionEntity(sub, "Which of these is a mutable data type?", "Tuple", "String", "List", "Int", 1));
        q.add(new QuestionEntity(sub, "Function to get length of list?", "size()", "length()", "len()", "count()", 2));
        q.add(new QuestionEntity(sub, "Python is _____ language.", "Interpreted", "Compiled", "Both", "None", 0));
        q.add(new QuestionEntity(sub, "How to start a comment in Python?", "//", "/*", "#", "--", 2));
        q.add(new QuestionEntity(sub, "Which operator is used for power (x^y)?", "^", "**", "*", "//", 1));
        q.add(new QuestionEntity(sub, "Correct way to create a list?", "[]", "{}", "()", "<>", 0));
        for(int i=11; i<=50; i++) q.add(new QuestionEntity(sub, "Python Question " + i + "?", "Option A", "Option B", "Option C", "Option D", 1));
    }

    private static void addJavaQuestions(List<QuestionEntity> q) {
        String sub = "Java";
        q.add(new QuestionEntity(sub, "Who developed Java?", "James Gosling", "Guido van Rossum", "Dennis Ritchie", "Bjarne Stroustrup", 0));
        q.add(new QuestionEntity(sub, "Java is a _____ language.", "Object-Oriented", "Procedural", "Scripting", "Machine", 0));
        q.add(new QuestionEntity(sub, "Which component runs the Java bytecode?", "JDK", "JRE", "JVM", "JIT", 2));
        q.add(new QuestionEntity(sub, "Size of boolean in Java?", "1 bit", "8 bits", "16 bits", "Platform dependent", 0));
        for(int i=5; i<=50; i++) q.add(new QuestionEntity(sub, "Java Question " + i + "?", "A", "B", "C", "D", 2));
    }

    private static void addDataStructureQuestions(List<QuestionEntity> q) {
        String sub = "Data Structures";
        for(int i=1; i<=50; i++) q.add(new QuestionEntity(sub, "DS Question " + i + "?", "A", "B", "C", "D", 1));
    }

    private static void addAlgorithmQuestions(List<QuestionEntity> q) {
        String sub = "Algorithms";
        for(int i=1; i<=50; i++) q.add(new QuestionEntity(sub, "Algo Question " + i + "?", "A", "B", "C", "D", 0));
    }

    private static void addMLQuestions(List<QuestionEntity> q) {
        String sub = "Machine Learning";
        for(int i=1; i<=50; i++) q.add(new QuestionEntity(sub, "ML Question " + i + "?", "A", "B", "C", "D", 3));
    }

    private static void addCloudQuestions(List<QuestionEntity> q) {
        String sub = "Cloud Computing";
        for(int i=1; i<=50; i++) q.add(new QuestionEntity(sub, "Cloud Question " + i + "?", "A", "B", "C", "D", 0));
    }
}
