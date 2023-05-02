package com.example.tickview_mobile.ui.search;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tickview_mobile.R;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import org.json.JSONArray;
import org.json.JSONObject;
import com.example.tickview_mobile.databinding.FragmentSearchFormBinding;
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

        Spinner categorySpinner = view.findViewById(R.id.category_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.category_spinner_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        Button searchButton = view.findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the values from the form fields
                String keyword = ((AutoCompleteTextView) view.findViewById(R.id.keywordField)).getText().toString();
                int distance = Integer.parseInt(((EditText) view.findViewById(R.id.distance_input)).getText().toString());
                String category = ((Spinner) view.findViewById(R.id.category_spinner)).getSelectedItem().toString();
                String location = ((EditText) view.findViewById(R.id.location_input)).getText().toString();
                boolean autoDetect = ((RadioButton) view.findViewById(R.id.auto_detect_location)).isChecked();

                // Call the searchEvent function with the form values
                searchEvent(keyword, distance, category, location, autoDetect);
            }
        });
    }
    private void searchEvent(String keyword, int distance, String category, String location, boolean autoDetect) {
        SearchResultsFragment searchResultsFragment = new SearchResultsFragment();
        volleyService.fetchLocation(autoDetect, location, new VolleyService.FetchLocationCallback() {
            @Override
            public void onSuccess(String geoPoint) {
                volleyService.searchEvent(keyword, distance, category, geoPoint, new VolleyService.SearchEventCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        // Handle the search event response
                        List<Event> events = parseEventsFromResponse(response);

                        Bundle args = new Bundle();
                        args.putParcelableArrayList("events", new ArrayList<>(events));
                        searchResultsFragment.setArguments(args);
                        NavController navController = Navigation.findNavController(requireView());
                        navController.navigate(R.id.action_navigation_search_form_to_navigation_search_results, args);
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