package com.example.automessages.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.automessages.Contact;
import com.example.automessages.R;
import com.example.automessages.adapters.ContactsAdapter;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class ContactsFragment extends Fragment {
    public static final String PREFS_KEY = "CONTACTS_PREFS";
    public static final String SELECTED_CONTACTS_KEY = "contact_";

    private ListView contactsListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts_fragment, container, false);

        EditText searchEditText = view.findViewById(R.id.contact_search);
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Search by name or number
                String query = charSequence.toString();
                ArrayList<Contact> contactsList = Contact.getAll(requireContext())
                        .stream()
                        .filter(contact -> contact.getName().toLowerCase().contains(query.toLowerCase()) || TextUtils.join("", contact.getPhoneNumber().split(" ")).contains(query))
                        .collect(Collectors.toCollection(ArrayList::new));

                ContactsAdapter adapter = new ContactsAdapter(getActivity(), contactsList);
                contactsListView.setAdapter(adapter);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        contactsListView = view.findViewById(R.id.contacts_list);

        // Check for permission to read contacts
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS}, 0);
        } else {
            loadContacts();
        }

        return view;
    }

    private void loadContacts() {
        ArrayList<Contact> contactsList = Contact.getAll(requireContext());

        ContactsAdapter adapter = new ContactsAdapter(getActivity(), contactsList);
        contactsListView.setAdapter(adapter);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadContacts();
            } else {
                // Quit app
                Toast.makeText(requireContext(), "You need to accept the permission for the app to work", Toast.LENGTH_SHORT).show();
                requireActivity().finish();
            }
        }
    }
}
