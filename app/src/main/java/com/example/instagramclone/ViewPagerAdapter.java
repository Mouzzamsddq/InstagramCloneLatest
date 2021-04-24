package com.example.instagramclone;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> fragmentList = new ArrayList();
    private final List<String> fragmentTitle = new ArrayList();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public Fragment getItem(int i) {
        return this.fragmentList.get(i);
    }

    public int getCount() {
        return this.fragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
        this.fragmentList.add(fragment);
        this.fragmentTitle.add(title);
    }

    public CharSequence getPageTitle(int position) {
        return this.fragmentTitle.get(position);
    }
}
