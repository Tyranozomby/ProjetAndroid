package com.example.automessages.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.automessages.Answer;
import com.example.automessages.Contact;
import com.example.automessages.R;
import com.example.automessages.services.AutoResponseService;
import com.example.automessages.sms.SmsSender;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;

public class SendFragment extends Fragment {

    private static final String PREFS_KEY = "SEND_PREFS";
    private static final String SEND_TOGGLE_KEY = "send_toggle";

    private ArrayList<Contact> selectedContacts;

    private TextView sentence;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.send_fragment, container, false);

        // Request permission to send SMS
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);

            //
        } else {
            SharedPreferences sendSharedPreferences = requireContext().getSharedPreferences(PREFS_KEY, Context.MODE_PRIVATE);

            // Toggle service
            SwitchMaterial switchMaterial = view.findViewById(R.id.send_toggle);
            switchMaterial.setChecked(sendSharedPreferences.getBoolean(SEND_TOGGLE_KEY, false));
            switchMaterial.setOnCheckedChangeListener((buttonView, isChecked) -> {
                sendSharedPreferences.edit().putBoolean(SEND_TOGGLE_KEY, isChecked).apply();
                toggleService(isChecked);
            });

            // Send {answer} to {contacts}
            sentence = view.findViewById(R.id.send_sentence);
            initSentence();

            // Send now button
            Button sendNowButton = view.findViewById(R.id.send_button_now);
            sendNowButton.setOnClickListener(v -> {
                for (Contact contact : selectedContacts) {
                    SmsSender.sendSMS(contact.getPhoneNumber(), requireContext());
                }

                if (selectedContacts.size() > 0)
                    Toast.makeText(requireContext(), String.format("Message%s sent", selectedContacts.size() > 1 ? "s" : ""), Toast.LENGTH_SHORT).show();
            });
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
            if (sentence == null)
                sentence = requireView().findViewById(R.id.send_sentence);
            initSentence();
        }
    }

    private void initSentence() {
        String selectedAnswer = Answer.getAnswer(requireContext());
        selectedContacts = Contact.getAllSelected(requireContext());

        String sendingSentence = getString(R.string.sending_sentence, selectedAnswer, TextUtils.join(", ", selectedContacts.stream().map(Contact::getName).toArray(String[]::new)));
        sentence.setText(sendingSentence);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length <= 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                // Show the first fragment
                ViewPager viewPager = requireActivity().findViewById(R.id.view_pager);
                viewPager.setCurrentItem(0);
                Toast.makeText(requireContext(), "You need to accept the permission to send messages", Toast.LENGTH_SHORT).show();
            } else {
                // Force fragment to reload
                onResume();
            }
        }
    }

    private void toggleService(boolean isChecked) {
        Intent intent = new Intent(requireContext(), AutoResponseService.class);

        if (isChecked) {
            requireContext().startService(intent);
        } else {
            requireContext().stopService(intent);
        }
    }
}