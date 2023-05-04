package com.example.tickview_mobile.ui.search;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
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

        showProgressBar();
        // Call updateData() initially to set data to the views
        Bundle args = getArguments();
        if (args != null) {
            updateData();
        }
    }

    public void showProgressBar() {
        ProgressBar progressBar = getView().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
    }

    public void updateData() {

        View view = getView();
        if (view == null) {
            return;
        }

        ProgressBar progressBar = getView().findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        // Get the EventDetailData object from the arguments
        Bundle args = getArguments();
        if (args != null) {


            EventDetailData eventDetailData = args.getParcelable("eventDetailData");

            // Use eventDetailData to set the contents of the views
            // Replace these ids with the actual ids of your views
            TextView dateTextView = view.findViewById(R.id.cardDateValue);
            TextView timeTextView = view.findViewById(R.id.cardTimeValue);
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

                String buyTicketsUrl = eventDetailData.getTicketUrl();
                String buyTicketsText = buyTicketsUrl;
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(buyTicketsText);

                // Set the URLSpan with your desired link
                spannableStringBuilder.setSpan(new URLSpan(buyTicketsUrl), 0, buyTicketsText.length(), 0);

                // Set the ForegroundColorSpan with your desired text color
                spannableStringBuilder.setSpan(new ForegroundColorSpan(Color.parseColor("#FF4081")), 0, buyTicketsText.length(), 0);

                // Apply the SpannableStringBuilder to the TextView
                buyTicketsTextView.setText(spannableStringBuilder);

                // Apply the marquee effect to the TextView
                buyTicketsTextView.setSelected(true);

                // Set the onClick listener for the TextView
                buyTicketsTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(buyTicketsUrl));
                        startActivity(browserIntent);
                    }
                });

                dateTextView.setText(eventDetailData.getLocalDate());
                timeTextView.setText(eventDetailData.getLocalTime());
                artistTextView.setText(eventDetailData.getAttractions());
                venueTextView.setText(eventDetailData.getVenueName());
                genresTextView.setText(eventDetailData.getGenres());
                priceRangesTextView.setText(eventDetailData.getPriceRanges());
                ticketStatusTextView.setText(eventDetailData.getTicketStatus());
                buyTicketsTextView.setText(eventDetailData.getTicketUrl());
                artistTextView.setSelected(true);
                genresTextView.setSelected(true);
                venueTextView.setSelected(true);
                // Load the seat map image using an image loading library like Glide or Picasso
                Glide.with(this).load(eventDetailData.getSeatMapUrl()).into(seatMapImageView);
            }
        }
    }
}