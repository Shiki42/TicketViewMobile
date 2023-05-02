package com.example.tickview_mobile;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.tickview_mobile.databinding.ActivityMainBinding;
import com.example.tickview_mobile.ui.search.DetailFragment;
import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set up TabLayout
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        NavController navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main)).getNavController();

        // Create the tabs
        tabLayout.addTab(tabLayout.newTab().setText(R.string.title_search));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.title_favorites));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        navController.navigate(R.id.navigation_search);
                        break;
                    case 1:
                        navController.navigate(R.id.navigation_favorites);
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.navigation_detail) {
                    tabLayout.setVisibility(View.GONE);
                } else {
                    tabLayout.setVisibility(View.VISIBLE);
                }
            }
        });

    }

//    public void showDetailFragmentAndFetchData(String eventId) {
//        // Create a new instance of DetailFragment
//        // Get the NavController from the NavHostFragment
//        NavController navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_activity_main)).getNavController();
//
//        // Create a new Bundle to hold the eventId
//        Bundle args = new Bundle();
//        args.putString("eventId", eventId);
//
//        // Navigate to the DetailFragment using NavController
//        navController.navigate(R.id.action_navigation_search_results_to_navigation_detail, args);
//    }
}