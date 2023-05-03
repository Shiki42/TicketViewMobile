package com.example.tickview_mobile.ui.search;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.tickview_mobile.R;
import com.example.tickview_mobile.models.EventDetailData;

public class DetailTab1Fragment extends Fragment {

    public DetailTab1Fragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detail_tab1, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Call updateData() initially to set data to the views
        updateData();
    }

    public void updateData() {
        View view = getView();
        if (view == null) {
            return;
        }

        // Get the EventDetailData object from the arguments
        Bundle args = getArguments();
        if (args != null) {
            EventDetailData eventDetailData = args.getParcelable("eventDetailData");

            // Use eventDetailData to set the contents of the views
            // Replace these ids with the actual ids of your views
            TextView dateTextView = view.findViewById(R.id.cardDateValue);
            TextView artistTextView = view.findViewById(R.id.cardArtistValue);
            TextView venueTextView = view.findViewById(R.id.cardVenueValue);
            TextView genresTextView = view.findViewById(R.id.cardGenresValue);
            TextView priceRangesTextView = view.findViewById(R.id.cardPriceRangesValue);
            TextView ticketStatusTextView = view.findViewById(R.id.cardTicketStatusValue);
            TextView buyTicketsTextView = view.findViewById(R.id.cardBuyTicketsValue);
            ImageView seatMapImageView = view.findViewById(R.id.cardSeatMap);

            // Set the values from eventDetailData to the views
            // You may need to use the helper functions you've created to format the data (e.g. getGenres(), getTicketStatusMessage(), getPriceRanges())
            if (eventDetailData != null) {
                dateTextView.setText(eventDetailData.getLocalDate() + " " + eventDetailData.getLocalTime());
                artistTextView.setText(eventDetailData.getAttractions());
                venueTextView.setText(eventDetailData.getVenueName());
                genresTextView.setText(eventDetailData.getGenres());
                priceRangesTextView.setText(eventDetailData.getPriceRanges());
                ticketStatusTextView.setText(eventDetailData.getTicketStatus());
                buyTicketsTextView.setText(eventDetailData.getTicketUrl());

                // Load the seat map image using an image loading library like Glide or Picasso
                Glide.with(this).load(eventDetailData.getSeatMapUrl()).into(seatMapImageView);
            }
        }
    }
}