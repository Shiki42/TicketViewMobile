package com.example.tickview_mobile.ui.search;

import android.content.Context;
import android.content.SharedPreferences;
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

import java.util.ArrayList;
import java.util.List;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.SearchResultViewHolder> {

    public List<Event> events;
    private Context context;
    private OnResultItemClickListener onResultItemClickListener;
    private OnFavoriteClickListener onFavoriteClickListener;

    public interface OnResultItemClickListener {
        void onResultItemClick(String eventId);
    }

    public interface OnFavoriteClickListener {
        void onFavoriteClick(Event event);
    }

    public SearchResultAdapter(Context context, List<Event> events, OnResultItemClickListener onResultItemClickListener) {
        this.context = context;
        this.events = events;
        this.onResultItemClickListener = onResultItemClickListener;
    }

    public SearchResultAdapter(Context context, List<Event> events, OnResultItemClickListener onResultItemClickListener, OnFavoriteClickListener onFavoriteClickListener) {
        this.context = context;
        this.events = events;
        this.onResultItemClickListener = onResultItemClickListener;
        this.onFavoriteClickListener = onFavoriteClickListener;
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
            eventName.setSelected(true);
            eventDate.setSelected(true);
            eventCategory.setSelected(true);
            eventTime.setSelected(true);
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

            updateFavoriteButtonImage(event);

            favoriteButton.setOnClickListener(view -> {
                if (isEventInFavorites(event)) {
                    removeEventFromFavorites(event);
                    if (onFavoriteClickListener != null) {
                        onFavoriteClickListener.onFavoriteClick(event);
                    }
                } else {
                    addEventToFavorites(event);
                }
                updateFavoriteButtonImage(event);
            });
        }
        private void updateFavoriteButtonImage(Event event) {
            if (isEventInFavorites(event)) {
                favoriteButton.setImageResource(R.drawable.heart_filled);
            } else {
                favoriteButton.setImageResource(R.drawable.heart_outline);
            }
        }
    }

    private List<Event> getFavoriteEventsList() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("favorite_list", Context.MODE_PRIVATE);
        String favoriteEventsJson = sharedPreferences.getString("favorite_list", null);
        if (favoriteEventsJson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Event>>() {}.getType();
            return gson.fromJson(favoriteEventsJson, type);
        } else {
            return new ArrayList<>();
        }
    }

    private void saveFavoriteEventsList(List<Event> favoriteEventsList) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("favorite_list", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String favoriteEventsJson = gson.toJson(favoriteEventsList);
        editor.putString("favorite_list", favoriteEventsJson);
        editor.apply();
    }

    private boolean isEventInFavorites(Event event) {
        List<Event> favoriteEventsList = getFavoriteEventsList();
        return favoriteEventsList.stream().anyMatch(e -> e.getId().equals(event.getId()));
    }

    private void addEventToFavorites(Event event) {
        List<Event> favoriteEventsList = getFavoriteEventsList();
        favoriteEventsList.add(event);
        saveFavoriteEventsList(favoriteEventsList);
    }

    private void removeEventFromFavorites(Event event) {
        List<Event> favoriteEventsList = getFavoriteEventsList();
        favoriteEventsList.removeIf(e -> e.getId().equals(event.getId()));
        saveFavoriteEventsList(favoriteEventsList);
    }
}