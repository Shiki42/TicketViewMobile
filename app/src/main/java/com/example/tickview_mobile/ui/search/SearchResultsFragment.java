package com.example.tickview_mobile.ui.search;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tickview_mobile.databinding.FragmentSearchResultsBinding;

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
    }

    public void showProgressBar() {
        ProgressBar progressBar = getView().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void updateSearchResults(List<Event> events) {
        ProgressBar progressBar = getView().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        RecyclerView recyclerView = getView().findViewById(R.id.search_result_recyclerview);
        SearchResultAdapter searchResultAdapter = new SearchResultAdapter(getContext(), events);
        recyclerView.setAdapter(searchResultAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void showNoDataMessage() {
        ProgressBar progressBar = getView().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        TextView noDataTextView = getView().findViewById(R.id.no_data_textview);
        noDataTextView.setVisibility(View.VISIBLE);
    }
}