package com.example.automessages;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.automessages.fragments.AnswersFragment;
import com.example.automessages.fragments.ContactsFragment;
import com.example.automessages.fragments.SendFragment;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {

    private final ArrayList<Page> pages = new ArrayList<>();

    public PagerAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
        super(fm, behavior);

        String[] fragmentsNames = context.getResources().getStringArray(R.array.fragments_names);

        pages.add(new Page(fragmentsNames[0], new ContactsFragment()));
        pages.add(new Page(fragmentsNames[1], new AnswersFragment()));
        pages.add(new Page(fragmentsNames[2], new SendFragment()));
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return pages.get(position).getFragment();
    }

    @Override
    public int getCount() {
        return pages.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pages.get(position).getTitle();
    }

    private static class Page {
        private final String title;
        private final Fragment fragment;

        public Page(String title, Fragment fragment) {
            this.title = title;
            this.fragment = fragment;
        }

        public String getTitle() {
            return title;
        }

        public Fragment getFragment() {
            return fragment;
        }
    }
}

