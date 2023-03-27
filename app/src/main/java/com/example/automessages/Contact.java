package com.example.automessages;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;

import androidx.annotation.NonNull;

import com.example.automessages.fragments.ContactsFragment;

import java.util.ArrayList;

public class Contact {
    private final String id;

    private final String name;

    private final String phoneNumber;

    private boolean selected;

    public Contact(String id, String name, String phoneNumber, boolean selected) {
        this.id = id;
        this.name = name;

        phoneNumber = phoneNumber.replace("+33", "0");
        this.phoneNumber = PhoneNumberUtils.formatNumber(phoneNumber, "FR");

        this.selected = selected;
    }

    @SuppressLint("Range")
    public static ArrayList<Contact> getAll(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ContactsFragment.PREFS_KEY, Context.MODE_PRIVATE);

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

        ArrayList<Contact> contactsList = new ArrayList<>();
        String lastId = "";
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

            // To avoid duplicates due to the usage of ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            if (!id.equals(lastId)) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));
                boolean selected = sharedPreferences.getBoolean(ContactsFragment.SELECTED_CONTACTS_KEY + id, false);
                contactsList.add(new Contact(id, name, number, selected));
            }

            lastId = id;
        }
        cursor.close();

        return contactsList;
    }

    @SuppressLint("Range")
    public static ArrayList<Contact> getAllSelected(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ContactsFragment.PREFS_KEY, Context.MODE_PRIVATE);

        ContentResolver contentResolver = context.getContentResolver();
        Cursor cursor = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, ContactsContract.Contacts.DISPLAY_NAME + " ASC");

        ArrayList<Contact> contactsList = new ArrayList<>();
        String lastId = "";
        while (cursor.moveToNext()) {
            String id = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID));

            // To avoid duplicates due to the usage of ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            if (!id.equals(lastId)) {
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NORMALIZED_NUMBER));

                if (sharedPreferences.getBoolean(ContactsFragment.SELECTED_CONTACTS_KEY + id, false)) {
                    contactsList.add(new Contact(id, name, number, true));
                }
            }

            lastId = id;
        }
        cursor.close();

        return contactsList;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    @NonNull
    @Override
    public String toString() {
        return "name=" + name + ", phoneNumber=" + phoneNumber;
    }
}

