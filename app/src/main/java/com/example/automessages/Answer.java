package com.example.automessages;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.automessages.fragments.AnswersFragment;

public class Answer {

    public static String getAnswer(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(AnswersFragment.PREFS_KEY, Context.MODE_PRIVATE);
        String[] answers = sharedPreferences.getString(AnswersFragment.ANSWERS_LIST_KEY, null).split("\\|");
        int selectedAnswer = sharedPreferences.getInt(AnswersFragment.SELECTED_ANSWER_KEY, 0);

        return answers[selectedAnswer];
    }
}
