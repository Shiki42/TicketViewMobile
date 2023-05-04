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
import com.example.tickview_mobile.models.VenueData;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;
public class DetailTab3Fragment extends Fragment {

    public DetailTab3Fragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_tab3, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Call updateVenueData() initially to set data to the views
        updateVenueData();
    }

    private void toggleMaxLines(TextView textView) {
        if (textView.getMaxLines() == 3) {
            textView.setMaxLines(Integer.MAX_VALUE);
        } else {
            textView.setMaxLines(3);
        }
    }

    public void updateVenueData() {
        View view = getView();
        if (view == null) {
            return;
        }

        // Get the VenueData object from the arguments
        Bundle args = getArguments();
        if (args != null) {
            VenueData venueData = args.getParcelable("venueData");

            // Use venueData to set the contents of the views
            // Replace these ids with the actual ids of your views
            TextView nameTextView = view.findViewById(R.id.nameValue);
            TextView addressTextView = view.findViewById(R.id.addressValue);
            TextView cityStateTextView = view.findViewById(R.id.cityStateValue);
            TextView contactInfoTextView = view.findViewById(R.id.contactInfoValue);
            TextView openHoursTextView = view.findViewById(R.id.openHoursValue);
            TextView generalRuleTextView = view.findViewById(R.id.generalRuleValue);
            TextView childRuleTextView = view.findViewById(R.id.childRuleValue);

            // Set the values from venueData to the views
            if (venueData != null) {
                nameTextView.setText(venueData.getName());
                addressTextView.setText(venueData.getAddress());
                cityStateTextView.setText(venueData.getCityState());
                contactInfoTextView.setText(venueData.getContactInfo());
                openHoursTextView.setText(venueData.getOpenHours());
                generalRuleTextView.setText(venueData.getGeneralRule());
                childRuleTextView.setText(venueData.getChildRule());
                openHoursTextView.setOnClickListener(v -> toggleMaxLines((TextView) v));
                generalRuleTextView.setOnClickListener(v -> toggleMaxLines((TextView) v));
                childRuleTextView.setOnClickListener(v -> toggleMaxLines((TextView) v));
            }



            // Initialize the map
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            if (mapFragment != null) {
                mapFragment.getMapAsync(googleMap -> {
                    // Use venueData to set the map location
                    if (venueData != null && venueData.getLatitude() != null && venueData.getLongitude() != null) {
                        LatLng venueLocation = new LatLng(venueData.getLatitude(), venueData.getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(venueLocation).title(venueData.getName()));
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(venueLocation, 15));
                    }
                });
            }
        }
    }
}