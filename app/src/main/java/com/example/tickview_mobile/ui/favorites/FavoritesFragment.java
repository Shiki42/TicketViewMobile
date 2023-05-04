package com.example.tickview_mobile.ui.favorites;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.tickview_mobile.R;
import com.example.tickview_mobile.models.Event;
import com.example.tickview_mobile.ui.search.SearchResultAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoritesFragment extends Fragment {

    private RecyclerView favoritesRecyclerView;
    private TextView noDataTextView;
    private ProgressBar progressBar;
    private SearchResultAdapter searchResultAdapter;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.favorites, container, false);

        favoritesRecyclerView = view.findViewById(R.id.favorites_recyclerview);
        noDataTextView = view.findViewById(R.id.no_data_textview);
        progressBar = view.findViewById(R.id.progress_bar);

        setupRecyclerView();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadFavoriteEvents();
    }

    private void setupRecyclerView() {
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultAdapter = new SearchResultAdapter(getContext(), new ArrayList<>(), eventId -> {
            // Handle item click if needed
        }, this::removeEventAndUpdateList);
        favoritesRecyclerView.setAdapter(searchResultAdapter);
    }

    private void loadFavoriteEvents() {
        progressBar.setVisibility(View.VISIBLE);
        List<Event> favoriteEvents = getFavoriteEventsList();
        if (!favoriteEvents.isEmpty()) {
            searchResultAdapter.events = favoriteEvents;
            searchResultAdapter.notifyDataSetChanged();
            noDataTextView.setVisibility(View.GONE);
        } else {
            noDataTextView.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }

    private List<Event> getFavoriteEventsList() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("favorite_list", Context.MODE_PRIVATE);
        String favoriteEventsJson = sharedPreferences.getString("favorite_list", null);
        if (favoriteEventsJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Event>>() {}.getType();
            return gson.fromJson(favoriteEventsJson, type);
        } else {
            return new ArrayList<>();
        }
    }

    private void removeEventAndUpdateList(Event event) {
        searchResultAdapter.events.remove(event);
        searchResultAdapter.notifyDataSetChanged();

        if (searchResultAdapter.events.isEmpty()) {
            noDataTextView.setVisibility(View.VISIBLE);
        }
    }
}