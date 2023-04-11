package com.example.tickview_mobile.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tickview_mobile.R;

import android.widget.ArrayAdapter;

import org.json.JSONArray;
import org.json.JSONObject;
import com.example.tickview_mobile.databinding.FragmentSearchFormBinding;
import com.example.tickview_mobile.ui.search.SearchResultsFragment;
import com.example.tickview_mobile.models.Event;

import java.util.ArrayList;
import java.util.List;

public class SearchFormFragment extends Fragment {
    private VolleyService volleyService;
    private ArrayAdapter<String> autoCompleteAdapter;

    private FragmentSearchFormBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        volleyService = new VolleyService(requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchFormBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    // ... onCreateView, onViewCreated ...

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Your previous onViewCreated code...

        setupAutoComplete();
    }
    private void searchEvent(String keyword, int distance, String category, String location, boolean autoDetect) {
        SearchResultsFragment searchResultFragment = (SearchResultsFragment) getParentFragmentManager().findFragmentById(R.id.fragment_search_results);
        searchResultFragment.showProgressBar();
        volleyService.fetchLocation(autoDetect, location, new VolleyService.FetchLocationCallback() {
            @Override
            public void onSuccess(String geoPoint) {
                volleyService.searchEvent(keyword, distance, category, geoPoint, new VolleyService.SearchEventCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        // Handle the search event response
                        Bundle args = new Bundle();
                        args.putSerializable("response", response.toString());

                        List<Event> events = parseEventsFromResponse(response);
                        searchResultFragment.updateSearchResults(events);
                    }

                    @Override
                    public void onError(String message) {
                        searchResultFragment.showNoDataMessage();
                    }
                });
            }

            @Override
            public void onError(String message) {
                // Handle the error
            }
        });
    }

    public List<Event> parseEventsFromResponse(JSONObject response) {
        List<Event> events = new ArrayList<>();
        if (response.has("_embedded") && response.optJSONObject("_embedded").has("events")) {
            JSONArray eventsArray = response.optJSONObject("_embedded").optJSONArray("events");
            for (int i = 0; i < eventsArray.length(); i++) {
                JSONObject eventJson = eventsArray.optJSONObject(i);
                Event event = new Event();

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
    // Add this method in onViewCreated
    private void setupAutoComplete() {
        autoCompleteAdapter = new ArrayAdapter<>(requireContext(), R.layout.custom_dropdown_item);
        binding.keywordField.setAdapter(autoCompleteAdapter);
        binding.keywordField.setThreshold(3);
        binding.keywordField.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = parent.getItemAtPosition(position).toString();
            binding.keywordField.setText(selectedItem);
        });

        binding.keywordField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String keyword = s.toString();
                if (keyword.length() >= 3) {
                    volleyService.autoComplete(keyword, autoCompleteAdapter, new VolleyService.AutoCompleteCallback() {
                        @Override
                        public void onSuccess(List<String> results) {
                            autoCompleteAdapter.clear();
                            autoCompleteAdapter.addAll(results);
                            autoCompleteAdapter.notifyDataSetChanged();
                            autoCompleteAdapter.getFilter().filter(keyword);
                        }

                        @Override
                        public void onError(String message) {
                            // Handle the error
                        }
                    });
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
}