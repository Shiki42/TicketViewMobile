package com.example.tickview_mobile.ui.search;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DetailViewPagerAdapter extends FragmentStateAdapter {

    public DetailViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new DetailTab1Fragment(); // Tab1
            case 1:
                //return new DetailTab2Fragment();
            case 2:
                //return new DetailTab3Fragment();
            default:
                return new DetailTab1Fragment(); // Default to Tab1 if position is not valid
        }
    }

    @Override
    public int getItemCount() {
        // TODO: Return the number of tabs
        // For example: return 3;
        return 3;
    }
}
