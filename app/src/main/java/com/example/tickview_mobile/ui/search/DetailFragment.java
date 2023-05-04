package com.example.tickview_mobile.ui.search;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager2.widget.ViewPager2;
import com.example.tickview_mobile.R;
import com.example.tickview_mobile.models.ArtistData;
import com.example.tickview_mobile.models.EventDetailData;
import com.example.tickview_mobile.models.VenueData;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import android.view.MenuInflater;
public class DetailFragment extends Fragment {
    private VolleyService volleyService;
    private ViewPager2 detailViewPager;
    private TabLayout detailTabLayout;

    private String eventName;
    private String eventUrl;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.action_icon, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    private void shareOnTwitter(String eventName, String eventUrl) {
        String tweetText = "Check " + eventName + " on Ticketmaster: " + eventUrl;
        String twitterUrl = "https://twitter.com/intent/tweet?text=" + Uri.encode(tweetText);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(twitterUrl));
        startActivity(intent);
    }

    private void shareOnFacebook(String eventUrl) {
        String facebookUrl = "https://www.facebook.com/sharer/sharer.php?u=" + Uri.encode(eventUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl));
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_facebook:
                shareOnFacebook(eventUrl);
                return true;
            case R.id.action_twitter:
                shareOnTwitter(eventName, eventUrl);
                return true;
            case android.R.id.home:
                // Navigate back to the SearchResultFragment
                NavHostFragment.findNavController(this).navigateUp();
                ActionBar actionBar = ((AppCompatActivity) requireActivity()).getSupportActionBar();
                if (actionBar != null) {
                    actionBar.setDisplayHomeAsUpEnabled(false);
                    actionBar.hide();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
            actionBar.show();
            actionBar.setTitle("Detail Page");
            actionBar.setDisplayHomeAsUpEnabled(true);
            // You can set your social icons here using actionBar.setCustomView()
        }
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up the ViewPager
        detailViewPager = view.findViewById(R.id.detail_view_pager);
        detailTabLayout = view.findViewById(R.id.detail_tab_layout);
        // You need to create a FragmentStateAdapter to provide fragments for the ViewPager
        // DetailViewPagerAdapter should extend FragmentStateAdapter
        DetailViewPagerAdapter detailViewPagerAdapter = new DetailViewPagerAdapter(this);
        detailViewPager.setAdapter(detailViewPagerAdapter);

        // Set up the TabLayout
        new TabLayoutMediator(detailTabLayout, detailViewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("DETAILS");
                    break;
                case 1:
                    tab.setText("ARTIST(S)");
                    break;
                case 2:
                    tab.setText("VENUE");
                    break;
            }
        }).attach();

        String eventId = getArguments().getString("eventId");

        fetchData(eventId);
    }

    private void setEventDetails(JSONObject eventDetailData) {
        try {
            eventName = eventDetailData.getString("name");
            eventUrl = eventDetailData.getString("url");
        } catch (JSONException e) {
            e.printStackTrace();
            // Handle the exception, e.g., show an error message or set default values for eventName and eventUrl
        }
    }

    public void fetchData(String eventId) {
        volleyService.fetchEventDetails(eventId, new VolleyService.FetchEventDetailsCallback() {
            @Override
            public void onSuccess(JSONObject eventDetailData) {

                setEventDetails(eventDetailData);
                EventDetailData parsedEventDetailData = parseEventDetailFromResponse(eventDetailData);
                Bundle bundle = new Bundle();

                bundle.putParcelable("eventDetailData", parsedEventDetailData); // Replace with the actual fetched data
                // Add more data to the bundle if needed

                // Get the existing DetailTab1Fragment instance from the ViewPager's adapter
                DetailViewPagerAdapter detailViewPagerAdapter = (DetailViewPagerAdapter) detailViewPager.getAdapter();
                DetailTab1Fragment detailTab1Fragment = detailViewPagerAdapter.getDetailTab1Fragment();

                // Update the data in the DetailTab1Fragment
                detailTab1Fragment.setArguments(bundle);
                detailTab1Fragment.updateData(); // You need to create this method in the DetailTab1Fragment to handle the data update

                // Notify the adapter that the data has changed
                detailViewPagerAdapter.notifyItemChanged(0);
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
                        try {
                            Log.e("venueDataRes:", venueDataRes.toString());
                            if (venueDataRes.has("_embedded")) {
                                JSONObject venueData = venueDataRes.getJSONObject("_embedded").getJSONArray("venues").getJSONObject(0);
                                VenueData venueDataParsed = parseVenueFromResponse(venueData);
                                updateDetailTab3Fragment(venueDataParsed);
                            } else {
                                Log.e("fetchVenueDetails", "Error: '_embedded' key not found in the JSONObject");
                            }
                        } catch (JSONException e) {
                            String venueData = "no venue data";
                            e.printStackTrace();
                        }                        // Handle venue data here
                    }

                    @Override
                    public void onError(String error) {
                        Log.e("Error fetching venue details:", error);
                        // Handle error here
                    }
                });

                ArrayList<ArtistData> artistDataList = new ArrayList<>();
                JSONArray attractions;
                try {
                    attractions = eventDetailData.getJSONObject("_embedded").getJSONArray("attractions");
                } catch (JSONException e) {
                    e.printStackTrace();
                    attractions = new JSONArray();
                }
                //Log.d("attractions in detail_fragment", attractions.toString());


                AtomicInteger artistCounter = new AtomicInteger(0);

                for (int i = 0; i < attractions.length(); i++) {
                    try {
                        JSONObject artist = attractions.getJSONObject(i);
                        JSONArray classifications = artist.getJSONArray("classifications");
                        String segmentName = classifications.getJSONObject(0).getJSONObject("segment").getString("name");
                        //Log.d("segmentName in detail_fragment", segmentName);
                        if ("Music".equals(segmentName)) {
                            String artistName = artist.getString("name");
                            volleyService.fetchArtistDetails(artistName, new VolleyService.FetchArtistDetailsCallback() {
                                @Override
                                public void onSuccess(JSONObject artistResponse) {
                                    try {
                                        JSONObject firstResult = artistResponse.getJSONObject("artists").getJSONArray("items").getJSONObject(0);
                                        if (firstResult != null) {
                                            //Log.d("firstResult in detail_fragment", firstResult.toString());
                                            String artistId = firstResult.getString("id");
                                            volleyService.fetchArtistAlbum(artistId, new VolleyService.FetchArtistAlbumCallback() {
                                                @Override
                                                public void onSuccess(JSONObject albums) {
                                                    // Handle artist data and albums here
                                                    //og.d("albums in detail_fragment", albums.toString());
                                                    ArtistData artistData = parseArtistFromResponse(firstResult, albums);
                                                    artistDataList.add(artistData);
                                                    int remaining = artistCounter.decrementAndGet();
                                                    if (remaining == 0) {
                                                        updateDetailTab2Fragment(artistDataList);
                                                    }
                                                }

                                                @Override
                                                public void onError(String error) {
                                                    // Handle error here
                                                    int remaining = artistCounter.decrementAndGet();
                                                    if (remaining == 0) {
                                                        updateDetailTab2Fragment(artistDataList);
                                                    }
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
                            artistCounter.incrementAndGet();
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

    private void updateDetailTab3Fragment(VenueData venueData) {
        Bundle bundle3 = new Bundle();

        bundle3.putParcelable("venueData", venueData);
        DetailViewPagerAdapter detailViewPagerAdapter = (DetailViewPagerAdapter) detailViewPager.getAdapter();
        DetailTab3Fragment detailTab3Fragment = detailViewPagerAdapter.getDetailTab3Fragment();
        detailTab3Fragment.setArguments(bundle3);
        detailTab3Fragment.updateVenueData();
        detailViewPagerAdapter.notifyItemChanged(2);
    }

    private void updateDetailTab2Fragment(ArrayList<ArtistData> artistDataList) {
        Bundle bundle2 = new Bundle();

        bundle2.putParcelableArrayList("artist_data", artistDataList);
        DetailViewPagerAdapter detailViewPagerAdapter = (DetailViewPagerAdapter) detailViewPager.getAdapter();
        DetailTab2Fragment detailTab2Fragment = detailViewPagerAdapter.getDetailTab2Fragment();
        detailTab2Fragment.setArguments(bundle2);
        detailTab2Fragment.updateArtistData();
        detailViewPagerAdapter.notifyItemChanged(1);
    }

    public EventDetailData parseEventDetailFromResponse(JSONObject response) {
        EventDetailData eventDetailData = new EventDetailData();

        JSONObject dates = response.optJSONObject("dates");
        if (dates != null) {
            JSONObject start = dates.optJSONObject("start");
            if (start != null) {
                eventDetailData.setLocalDate(start.optString("localDate"));
                eventDetailData.setLocalTime(start.optString("localTime"));
            }
            JSONObject status = dates.optJSONObject("status");
            if (status != null) {
                eventDetailData.setTicketStatus(status.optString("code"));
            }
        }

        JSONArray attractionsArray = response.optJSONObject("_embedded").optJSONArray("attractions");
        if (attractionsArray != null) {
            StringBuilder attractions = new StringBuilder();
            for (int i = 0; i < attractionsArray.length(); i++) {
                JSONObject attraction = attractionsArray.optJSONObject(i);
                if (i > 0) {
                    attractions.append(" | ");
                }
                attractions.append(attraction.optString("name"));
            }
            eventDetailData.setAttractions(attractions.toString());
        }

        JSONObject venue = response.optJSONObject("_embedded").optJSONArray("venues").optJSONObject(0);
        if (venue != null) {
            eventDetailData.setVenueName(venue.optString("name"));
        }

        JSONArray classifications = response.optJSONArray("classifications");
        if (classifications != null) {
            // Use the getGenres function to parse the genres.
            eventDetailData.setGenres(getGenres(classifications.optJSONObject(0)));
        }

        JSONArray priceRanges = response.optJSONArray("priceRanges");
        if (priceRanges != null) {
            // Use the getPriceRanges function to parse the price ranges.
            eventDetailData.setPriceRanges(getPriceRanges(priceRanges));
        }

        eventDetailData.setTicketUrl(response.optString("url"));

        JSONObject seatMap = response.optJSONObject("seatmap");
        if (seatMap != null) {
            eventDetailData.setSeatMapUrl(seatMap.optString("staticUrl"));
        }

        return eventDetailData;
    }

    public ArtistData parseArtistFromResponse(JSONObject firstResult, JSONObject albums) {
        ArtistData artistData = new ArtistData();

        try {
            if (firstResult.has("images")) {
                JSONArray images = firstResult.getJSONArray("images");
                if (images.length() > 0) {
                    artistData.setImage(images.getJSONObject(0).getString("url"));
                }
            }

            artistData.setName(firstResult.getString("name"));
            artistData.setPopularity(firstResult.getInt("popularity"));
            artistData.setFollowers(firstResult.getJSONObject("followers").getInt("total"));
            artistData.setSpotifyUrl(firstResult.getJSONObject("external_urls").getString("spotify"));

            if (albums.has("items")) {
                JSONArray albumItems = albums.getJSONArray("items");
                List<String> albumCoverImages = new ArrayList<>();

                for (int i = 0; i < albumItems.length(); i++) {
                    JSONObject album = albumItems.getJSONObject(i);
                    if (album.has("images")) {
                        JSONArray albumImages = album.getJSONArray("images");
                        if (albumImages.length() > 0) {
                            albumCoverImages.add(albumImages.getJSONObject(0).getString("url"));
                        }
                    }
                }

                artistData.setAlbumCoverImages(albumCoverImages);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return artistData;
    }

    public VenueData parseVenueFromResponse(JSONObject response) {
        VenueData venueData = new VenueData();

        JSONObject venue = response;
        if (venue != null) {
            venueData.setName(venue.optString("name", ""));

            JSONObject address = venue.optJSONObject("address");
            if (address != null) {
                venueData.setAddress(address.optString("line1", ""));
            } else {
                venueData.setAddress("");
            }

            JSONObject city = venue.optJSONObject("city");
            JSONObject state = venue.optJSONObject("state");
            if (city != null && state != null) {
                venueData.setCityState(city.optString("name", "") + " / " + state.optString("name", ""));
            } else {
                venueData.setCityState("");
            }

            JSONObject boxOfficeInfo = venue.optJSONObject("boxOfficeInfo");
            if (boxOfficeInfo != null) {
                venueData.setContactInfo(boxOfficeInfo.optString("phoneNumberDetail", ""));
                venueData.setOpenHours(boxOfficeInfo.optString("openHoursDetail", ""));
            } else {
                venueData.setContactInfo("");
                venueData.setOpenHours("");
            }

            JSONObject generalInfo = venue.optJSONObject("generalInfo");
            if (generalInfo != null) {
                venueData.setGeneralRule(generalInfo.optString("generalRule", ""));
                venueData.setChildRule(generalInfo.optString("childRule", ""));
            } else {
                venueData.setGeneralRule("");
                venueData.setChildRule("");
            }

            JSONObject location = venue.optJSONObject("location");
            if (location != null) {
                double latitude = location.optDouble("latitude", 0.0);
                double longitude = location.optDouble("longitude", 0.0);
                venueData.setLatitude(latitude);
                venueData.setLongitude(longitude);
            }
        }

        return venueData;
    }

    private String getGenres(JSONObject classification) {
        if (classification == null) {
            return "";
        }

        List<String> names = new ArrayList<>();
        JSONObject segment = classification.optJSONObject("segment");
        JSONObject genre = classification.optJSONObject("genre");
        JSONObject subGenre = classification.optJSONObject("subGenre");
        JSONObject type = classification.optJSONObject("type");
        JSONObject subType = classification.optJSONObject("subType");

        if (segment != null) {
            names.add(segment.optString("name"));
        }
        if (genre != null) {
            names.add(genre.optString("name"));
        }
        if (subGenre != null) {
            names.add(subGenre.optString("name"));
        }
        if (type != null) {
            names.add(type.optString("name"));
        }
        if (subType != null) {
            names.add(subType.optString("name"));
        }

        return names.stream().filter(name -> (name != null && !name.equals("Undefined"))).collect(Collectors.joining(" | "));
    }

    private String getPriceRanges(JSONArray priceRanges) {
        if (priceRanges == null) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < priceRanges.length(); i++) {
            JSONObject price = priceRanges.optJSONObject(i);
            if (price != null) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(price.optDouble("min")).append(" - ").append(price.optDouble("max")).append(" USD");
            }
        }

        return sb.toString();
    }

    private String getTicketStatusMessage(String statusCode) {
        if (statusCode == null) {
            return "";
        }

        switch (statusCode.toLowerCase()) {
            case "onsale":
                return "On Sale";
            case "offsale":
                return "Off Sale";
            case "cancelled":
                return "Cancelled";
            case "postponed":
                return "Postponed";
            case "rescheduled":
                return "Rescheduled";
            default:
                return "";
        }
    }


}