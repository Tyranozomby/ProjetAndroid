package com.example.automessages.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.automessages.R;
import com.example.automessages.adapters.AnswersAdapter;

import java.util.ArrayList;
import java.util.Arrays;

public class AnswersFragment extends Fragment {

    public static final String PREFS_KEY = "ANSWERS_PREFS";
    public static final String ANSWERS_LIST_KEY = "answers_list";
    public static final String SELECTED_ANSWER_KEY = "selected_answer";

    private ArrayList<String> answers;

    private EditText input;

    private AnswersAdapter answersAdapter;

    private SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.answers_fragment, container, false);

        ListView answersListView = view.findViewById(R.id.answers_list);
        input = view.findViewById(R.id.answer_input);

        sharedPreferences = requireContext().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);
        String answersString = sharedPreferences.getString(ANSWERS_LIST_KEY, null);

        if (answersString != null) {
            if (answersString.isEmpty())
                answers = new ArrayList<>();
            else
                answers = new ArrayList<>(Arrays.asList(answersString.split("\\|")));
        } else {
            answers = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.default_answers)));
            sharedPreferences.edit().putString(ANSWERS_LIST_KEY, TextUtils.join("|", answers)).apply();
        }

        answersAdapter = new AnswersAdapter(getContext(), answers);
        answersListView.setAdapter(answersAdapter);

        Button addButton = view.findViewById(R.id.answer_add_button);

        addButton.setOnClickListener(v -> addNewAnswer());

        return view;
    }

    private void addNewAnswer() {
        String text = input.getText().toString().trim();

        if (text.isEmpty()) {
            return;
        }

        answers.add(text);

        // Reset the input
        input.setText("");

        // Save the answers
        sharedPreferences.edit().putString(ANSWERS_LIST_KEY, TextUtils.join("|", answers)).apply();

        // Update the list
        answersAdapter.notifyDataSetChanged();
    }
}
