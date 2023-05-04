package com.example.tickview_mobile.ui.search;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tickview_mobile.MainActivity;
import com.example.tickview_mobile.R;
import com.example.tickview_mobile.databinding.FragmentSearchResultsBinding;
import com.example.tickview_mobile.models.Event;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultsFragment extends Fragment {
    private FragmentSearchResultsBinding binding;
    private VolleyService volleyService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        volleyService = new VolleyService(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchResultsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set up your RecyclerView or ListView here
        Bundle args = getArguments();
        if (args != null) {
            String keyword = args.getString("keyword");
            int distance = args.getInt("distance");
            String category = args.getString("category");
            String location = args.getString("location");
            boolean autoDetect = args.getBoolean("autoDetect");

            fetchDataAndDisplayResults(keyword, distance, category, location, autoDetect);
        }

        Button backButton = view.findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(requireView());
            navController.navigate(R.id.action_navigation_search_results_to_navigation_search_form);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // Show the ActionBar
        ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
            actionBar.setTitle("Search Results");
            actionBar.setDisplayHomeAsUpEnabled(false);
            // You can set any other properties or custom views required for the header here
        }
    }

    private void fetchDataAndDisplayResults(String keyword, int distance, String category, String location, boolean autoDetect) {
        showProgressBar();

        volleyService.fetchLocation(autoDetect, location, new VolleyService.FetchLocationCallback() {
            @Override
            public void onSuccess(String geoPoint) {
                volleyService.searchEvent(keyword, distance, category, geoPoint, new VolleyService.SearchEventCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        List<Event> events = parseEventsFromResponse(response);
                        updateSearchResults(events);
                    }

                    @Override
                    public void onError(String message) {
                        // Handle the error
                    }
                });
            }

            @Override
            public void onError(String message) {
                // Handle the error
            }
        });
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
        noDataTextView.setVisibility(View.GONE);

        if (events == null || events.isEmpty()) {
            noDataTextView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            noDataTextView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            SearchResultAdapter searchResultAdapter = new SearchResultAdapter(getContext(), events, event -> {
                // Call the showDetailFragmentAndFetchData() method in the parent activity
                Bundle args = new Bundle();
                args.putParcelable("event", event);
                NavController navController = Navigation.findNavController(requireView());
                navController.navigate(R.id.action_navigation_search_results_to_navigation_detail, args);
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(searchResultAdapter);

        }
    }

    public List<Event> parseEventsFromResponse(JSONObject response) {
        List<Event> events = new ArrayList<>();
        if (response.has("_embedded") && response.optJSONObject("_embedded").has("events")) {
            JSONArray eventsArray = response.optJSONObject("_embedded").optJSONArray("events");
            for (int i = 0; i < eventsArray.length(); i++) {
                JSONObject eventJson = eventsArray.optJSONObject(i);
                Event event = new Event();

                event.setId(eventJson.optString("id"));
                event.setName(eventJson.optString("name"));
                event.setImageUrl(eventJson.optJSONArray("images").optJSONObject(0).optString("url"));
                event.setVenueName(eventJson.optJSONObject("_embedded").optJSONArray("venues").optJSONObject(0).optString("name"));
                event.setDate(eventJson.optJSONObject("dates").optJSONObject("start").optString("localDate"));
                event.setTime(eventJson.optJSONObject("dates").optJSONObject("start").optString("localTime"));
                event.setSegmentName(eventJson.optJSONArray("classifications").optJSONObject(0).optJSONObject("segment").optString("name"));

                events.add(event);
            }
        }
        return events;
    }

}