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
        // TODO: Return the appropriate fragment for each tab based on the position
        // For example: return new DetailsTabFragment();
        return null;
    }

    @Override
    public int getItemCount() {
        // TODO: Return the number of tabs
        // For example: return 3;
        return 0;
    }
}
