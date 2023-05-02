package com.example.tickview_mobile.ui.search;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tickview_mobile.MainActivity;
import com.example.tickview_mobile.R;
import com.example.tickview_mobile.databinding.FragmentSearchResultsBinding;
import com.example.tickview_mobile.models.Event;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsFragment extends Fragment {
    private FragmentSearchResultsBinding binding;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchResultsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set up your RecyclerView or ListView here
        List<Event> events = getEventsFromArguments();
        updateSearchResults(events);
    }

    public void showProgressBar() {
        ProgressBar progressBar = getView().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    private List<Event> getEventsFromArguments() {
        List<Event> events = new ArrayList<>();
        if (getArguments() != null) {
            events = getArguments().getParcelableArrayList("events");
        }
        return events;
    }

    public void updateSearchResults(List<Event> events) {
        ProgressBar progressBar = getView().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        RecyclerView recyclerView = getView().findViewById(R.id.search_result_recyclerview);
        TextView noDataTextView = getView().findViewById(R.id.no_data_textview);

        if (events == null || events.isEmpty()) {
            noDataTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noDataTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            SearchResultAdapter searchResultAdapter = new SearchResultAdapter(getContext(), events, eventId -> {
                // Call the showDetailFragmentAndFetchData() method in the parent activity
                Bundle args = new Bundle();
                args.putString("eventId", eventId);
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_navigation_search_results_to_navigation_detail, args);
            });
            recyclerView.setAdapter(searchResultAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
    }

}