package com.example.tickview_mobile.ui.search;
import android.os.Bundle;
import android.util.Log;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailFragment extends Fragment {
    private VolleyService volleyService;

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
        volleyService = new VolleyService(requireContext());
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

    private void fetchData(String eventId) {
        volleyService.fetchEventDetails(eventId, new VolleyService.FetchEventDetailsCallback() {
            @Override
            public void onSuccess(JSONObject eventDetailData) {
                String venueId = "";
                try {
                    JSONArray venues = eventDetailData.getJSONObject("_embedded").getJSONArray("venues");
                    venueId = venues.getJSONObject(0).getString("id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                volleyService.fetchVenueDetails(venueId, new VolleyService.FetchVenueDetailsCallback() {
                    @Override
                    public void onSuccess(JSONObject venueDataRes) {
                        // Handle venue data here
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("Error fetching venue details:", error);
                        // Handle error here
                    }
                });

                JSONArray attractions;
                try {
                    attractions = eventDetailData.getJSONObject("_embedded").getJSONArray("attractions");
                } catch (JSONException e) {
                    e.printStackTrace();
                    attractions = new JSONArray();
                }

                for (int i = 0; i < attractions.length(); i++) {
                    try {
                        JSONObject artist = attractions.getJSONObject(i);
                        JSONArray classifications = artist.getJSONArray("classifications");
                        String segmentName = classifications.getJSONObject(0).getJSONObject("segment").getString("name");

                        if ("Music".equals(segmentName)) {
                            String artistName = artist.getString("name");
                            volleyService.fetchArtistDetails(artistName, new VolleyService.FetchArtistDetailsCallback() {
                                @Override
                                public void onSuccess(JSONObject artistResponse) {
                                    try {
                                        JSONObject firstResult = artistResponse.getJSONObject("artists").getJSONArray("items").getJSONObject(0);
                                        if (firstResult != null) {
                                            String artistId = firstResult.getString("id");
                                            volleyService.fetchArtistAlbum(artistId, new VolleyService.FetchArtistAlbumCallback() {
                                                @Override
                                                public void onSuccess(JSONObject albums) {
                                                    // Handle artist data and albums here
                                                }

                                                @Override
                                                public void onError(String error) {
                                                    // Handle error here
                                                }
                                            });
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }

                                @Override
                                public void onError(String error) {
                                    // Handle error here
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onError(String error) {
                // Handle error here
            }
        });
    }
}