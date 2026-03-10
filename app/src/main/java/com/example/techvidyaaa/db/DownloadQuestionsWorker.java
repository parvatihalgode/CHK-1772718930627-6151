package com.example.techvidyaaa.db;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadQuestionsWorker extends Worker {

    private static final String TAG = "DownloadQuestionsWorker";
    // Replace with your real CDN URL later
    private static final String CDN_URL = "https://raw.githubusercontent.com/your-repo/techvidya-content/main/questions.json";

    public DownloadQuestionsWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(CDN_URL).build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful() || response.body() == null) {
                return Result.retry();
            }

            String jsonData = response.body().string();
            Gson gson = new Gson();
            Type type = new TypeToken<Map<String, List<RemoteQuestion>>>(){}.getType();
            Map<String, List<RemoteQuestion>> dataMap = gson.fromJson(jsonData, type);

            List<QuestionEntity> allQuestions = new ArrayList<>();
            for (Map.Entry<String, List<RemoteQuestion>> entry : dataMap.entrySet()) {
                String subject = entry.getKey();
                for (RemoteQuestion rq : entry.getValue()) {
                    allQuestions.add(new QuestionEntity(subject, rq.question, rq.o1, rq.o2, rq.o3, rq.o4, rq.correct));
                }
            }

            AppDatabase db = AppDatabase.getDatabase(getApplicationContext());
            db.questionDao().insertAll(allQuestions);

            Log.d(TAG, "Successfully downloaded and cached " + allQuestions.size() + " questions from CDN.");
            return Result.success();

        } catch (IOException e) {
            Log.e(TAG, "Error downloading questions", e);
            return Result.retry();
        }
    }

    private static class RemoteQuestion {
        String question, o1, o2, o3, o4;
        int correct;
    }
}
