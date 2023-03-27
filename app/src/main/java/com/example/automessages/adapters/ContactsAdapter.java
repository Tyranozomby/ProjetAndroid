package com.example.automessages.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.automessages.Contact;
import com.example.automessages.R;
import com.example.automessages.fragments.ContactsFragment;

import java.util.ArrayList;

public class ContactsAdapter extends ArrayAdapter<Contact> {


    private final Context context;
    private final ArrayList<Contact> contactsList;

    public ContactsAdapter(Context context, ArrayList<Contact> contactsList) {
        super(context, 0, contactsList);
        this.context = context;
        this.contactsList = contactsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {
        if (view == null)
            view = LayoutInflater.from(context).inflate(R.layout.contact_item, parent, false);

        SharedPreferences sharedPreferences = context.getSharedPreferences(ContactsFragment.PREFS_KEY, Context.MODE_PRIVATE);

        Contact contact = contactsList.get(position);

        view.<TextView>findViewById(R.id.contact_name).setText(contact.getName());

        view.<TextView>findViewById(R.id.contact_number).setText(contact.getPhoneNumber());

        CheckBox checkbox = view.findViewById(R.id.contact_checkbox);
        checkbox.setChecked(sharedPreferences.getBoolean(ContactsFragment.SELECTED_CONTACTS_KEY + contact.getId(), false));

        checkbox.setOnClickListener(v -> {
            CheckBox checkBox = (CheckBox) v;
            ((Contact) checkBox.getTag()).setSelected(checkBox.isChecked());

            SharedPreferences.Editor editor = sharedPreferences.edit();
            // Clear all selected contacts
            for (Contact contact1 : contactsList) {
                editor.remove(ContactsFragment.SELECTED_CONTACTS_KEY + contact1.getId());
                if (contact1.isSelected())
                    editor.putBoolean(ContactsFragment.SELECTED_CONTACTS_KEY + contact1.getId(), true);
            }
            editor.apply();

        });

        checkbox.setTag(contact);

        return view;
    }
}