package com.example.tickview_mobile.ui.search;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.tickview_mobile.R;
public class DetailFragment extends Fragment {

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the ActionBar
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Detail Page");
            actionBar.setDisplayHomeAsUpEnabled(true);
            // You can set your social icons here using actionBar.setCustomView()
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up the ViewPager
        ViewPager2 detailViewPager = view.findViewById(R.id.detail_view_pager);

        // You need to create a FragmentStateAdapter to provide fragments for the ViewPager
        // DetailViewPagerAdapter should extend FragmentStateAdapter
        DetailViewPagerAdapter detailViewPagerAdapter = new DetailViewPagerAdapter(this);
        detailViewPager.setAdapter(detailViewPagerAdapter);
    }
}