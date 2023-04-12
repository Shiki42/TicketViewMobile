package com.example.tickview_mobile.ui.search;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.tickview_mobile.R;

public class SearchFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SearchFormFragment searchFormFragment = new SearchFormFragment();
        SearchResultsFragment searchResultsFragment = new SearchResultsFragment();

        getChildFragmentManager().beginTransaction()
                .add(R.id.fragment_container_search_form, searchFormFragment, "searchFormFragment")
                .add(R.id.fragment_container_search_results, searchResultsFragment, "searchResultsFragment")
                .commit();

        Log.d("SearchFragment", "SearchFormFragment: " + getChildFragmentManager().findFragmentByTag("searchFormFragment"));
        Log.d("SearchFragment", "SearchResultsFragment: " + getChildFragmentManager().findFragmentByTag("searchResultsFragment"));
    }
}