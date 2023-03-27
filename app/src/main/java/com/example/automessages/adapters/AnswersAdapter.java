package com.example.automessages.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.automessages.R;
import com.example.automessages.fragments.AnswersFragment;

import java.util.ArrayList;

public class AnswersAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final ArrayList<String> answersList;
    private int selectedAnswerIndex;

    public AnswersAdapter(Context context, @NonNull ArrayList<String> answersList) {
        super(context, 0, answersList);
        this.context = context;
        this.answersList = answersList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.answer_item, parent, false);

        SharedPreferences sharedPreferences = context.getSharedPreferences(AnswersFragment.PREFS_KEY, Context.MODE_PRIVATE);

        String answer = answersList.toArray(new String[0])[position];

        view.<TextView>findViewById(R.id.answer_text).setText(answer);

        ImageButton removeButton = view.findViewById(R.id.answer_remove);
        removeButton.setOnClickListener(v -> {
            if (position == selectedAnswerIndex) {
                Toast.makeText(context, "You can't remove the selected answer", Toast.LENGTH_SHORT).show();
                return;
            } else if (position < selectedAnswerIndex) {
                selectedAnswerIndex--;
                sharedPreferences.edit().putInt(AnswersFragment.SELECTED_ANSWER_KEY, selectedAnswerIndex).apply();
            }

            answersList.remove(answer);

            sharedPreferences.edit().putString(AnswersFragment.ANSWERS_LIST_KEY, TextUtils.join("|", answersList)).apply();

            notifyDataSetChanged();
        });

        selectedAnswerIndex = sharedPreferences.getInt(AnswersFragment.SELECTED_ANSWER_KEY, 0);

        RadioButton answerSelected = view.findViewById(R.id.answer_selected);
        answerSelected.setChecked(position == selectedAnswerIndex);

        answerSelected.setOnClickListener(view1 -> {
            selectedAnswerIndex = position;
            sharedPreferences.edit().putInt(AnswersFragment.SELECTED_ANSWER_KEY, selectedAnswerIndex).apply();
            notifyDataSetChanged();
        });

        return view;
    }
}