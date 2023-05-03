package com.example.tickview_mobile.ui.search;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class DetailViewPagerAdapter extends FragmentStateAdapter {

    private DetailTab1Fragment detailTab1Fragment;
    private DetailTab2Fragment detailTab2Fragment;

    public DetailViewPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
        detailTab1Fragment = new DetailTab1Fragment();
        detailTab2Fragment = new DetailTab2Fragment();
        // If you have a third tab, initialize it here as well
    }

    public DetailTab1Fragment getDetailTab1Fragment() {
        return detailTab1Fragment;
    }

    public DetailTab2Fragment getDetailTab2Fragment() {
        return detailTab2Fragment;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return detailTab1Fragment;
            case 1:
                return detailTab2Fragment;
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
