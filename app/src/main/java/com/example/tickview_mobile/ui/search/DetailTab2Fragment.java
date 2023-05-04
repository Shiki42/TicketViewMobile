package com.example.tickview_mobile.ui.search;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tickview_mobile.R;
import com.example.tickview_mobile.models.ArtistData;
import com.example.tickview_mobile.models.EventDetailData;

import java.util.ArrayList;
import java.util.List;

public class DetailTab2Fragment extends Fragment {

    private List<ArtistData> artistDataList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail_tab2, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        showProgressBar();
        // Call updateData() initially to set data to the views
        Bundle args = getArguments();
        if (args != null) {
            updateArtistData();
        }
    }


    public void showProgressBar() {
        ProgressBar progressBar = getView().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void updateArtistData() {

        View view = getView();
        if (view == null) {
            return;
        }

        ProgressBar progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        Bundle args = getArguments();
        if (args != null) {
            artistDataList = getArguments().getParcelableArrayList("artist_data");





            RecyclerView recyclerView = view.findViewById(R.id.artist_data_recyclerview);
            TextView noDataTextView = view.findViewById(R.id.no_data_textview);

            if (artistDataList == null || artistDataList.isEmpty()) {
                noDataTextView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            } else {
                noDataTextView.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                //Log.d("tab2_artistDataList", artistDataList.toString());
                    ArtistDataAdapter artistDataAdapter = new ArtistDataAdapter(getContext(), artistDataList);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    recyclerView.setAdapter(artistDataAdapter);

            }
        }
    }
}