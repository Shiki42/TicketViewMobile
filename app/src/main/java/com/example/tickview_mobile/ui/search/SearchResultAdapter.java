package com.example.tickview_mobile.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tickview_mobile.R;
import com.example.tickview_mobile.models.Event;

import java.util.List;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {

    private List<Event> events;
    private Context context;
    private OnResultItemClickListener onResultItemClickListener;

    public interface OnResultItemClickListener {
        void onResultItemClick(String eventId);
    }

    public SearchResultAdapter(Context context, List<Event> events, OnResultItemClickListener onResultItemClickListener) {
        this.context = context;
        this.events = events;
        this.onResultItemClickListener = onResultItemClickListener;
    }

    @NonNull
    @Override
    public SearchResultViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_result_item, parent, false);
        return new SearchResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultViewHolder holder, int position) {
        Event event = events.get(position);
        holder.bind(event);
        holder.itemView.setOnClickListener(v -> {
            if (onResultItemClickListener != null) {
                onResultItemClickListener.onResultItemClick(event.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class SearchResultViewHolder extends RecyclerView.ViewHolder {

        ImageView eventImage;
        TextView eventName, eventDate, venueName, eventTime, eventCategory;
        ImageView favoriteButton;

        public SearchResultViewHolder(@NonNull View itemView) {
            super(itemView);
            eventImage = itemView.findViewById(R.id.event_image);
            eventName = itemView.findViewById(R.id.event_name);
            eventDate = itemView.findViewById(R.id.event_date);
            venueName = itemView.findViewById(R.id.venue_name);
            eventTime = itemView.findViewById(R.id.event_time);
            eventCategory = itemView.findViewById(R.id.event_category);
            favoriteButton = itemView.findViewById(R.id.favorite_button);
        }

        public void bind(Event event) {
            // Load event image using an image loading library like Glide or Picasso
            // Example with Glide:
            // Glide.with(context).load(event.getImageUrl()).into(eventImage);
            Glide.with(context)
                    .load(event.getImageUrl())
                    //.placeholder(R.drawable.placeholder_image) // Replace with your placeholder image resource
                    .into(eventImage);
            eventName.setText(event.getName());
            eventDate.setText(event.getDate());
            venueName.setText(event.getVenueName());
            eventTime.setText(event.getTime());
            eventCategory.setText(event.getSegmentName());

            favoriteButton.setOnClickListener(view -> {
                // Call favorite() function
            });
        }
    }
}