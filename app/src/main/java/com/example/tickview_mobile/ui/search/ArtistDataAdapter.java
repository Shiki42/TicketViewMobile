package com.example.tickview_mobile.ui.search;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tickview_mobile.R;
import com.example.tickview_mobile.models.ArtistData;

import java.util.List;

import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

public class ArtistDataAdapter extends RecyclerView.Adapter<ArtistDataAdapter.ArtistDataViewHolder> {

    private List<ArtistData> artistDataList;
    private Context context;

    public ArtistDataAdapter(Context context, List<ArtistData> artistDataList) {
        this.context = context;
        this.artistDataList = artistDataList;
        Log.d("ArtistDataAdapter", "artistDataList size: " + artistDataList.size());
        Log.d("ArtistDataList", "List contents: " + artistDataList.toString());
        for (ArtistData artistData : artistDataList) {
            Log.d("ArtistDataList", "Artist Name: " + artistData.getName());
            Log.d("ArtistDataList", "Image: " + artistData.getImage());
            Log.d("ArtistDataList", "Followers: " + artistData.getFollowers());
            Log.d("ArtistDataList", "Spotify URL: " + artistData.getSpotifyUrl());
            Log.d("ArtistDataList", "Popularity: " + artistData.getPopularity());
            Log.d("ArtistDataList", "Album Cover Images: " + artistData.getAlbumCoverImages().toString());
        }
    }

    @NonNull
    @Override
    public ArtistDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("ArtistDataAdapter", "onCreateViewHolder");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.artist_item, parent, false);
        return new ArtistDataViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistDataViewHolder holder, int position) {
        Log.d("ArtistDataAdapter", "onBindViewHolder position: " + position);
        ArtistData artistData = artistDataList.get(position);
        holder.bind(artistData);
    }

    @Override
    public int getItemCount() {
        return artistDataList.size();
    }

    public class ArtistDataViewHolder extends RecyclerView.ViewHolder {

        ImageView artistImage;
        TextView artistName, followers, spotifyUrl, popularityNumber;
        MaterialProgressBar progressView;

        ImageView albumImage1;
        ImageView albumImage2;
        ImageView albumImage3;

        public ArtistDataViewHolder(@NonNull View itemView) {
            super(itemView);
            artistImage = itemView.findViewById(R.id.artist_image);
            artistName = itemView.findViewById(R.id.artist_name);
            followers = itemView.findViewById(R.id.artist_followers);
            spotifyUrl = itemView.findViewById(R.id.spotify_link);
            popularityNumber = itemView.findViewById(R.id.popularity_number);
            progressView = itemView.findViewById(R.id.popularity_progress);
            albumImage1 = itemView.findViewById(R.id.album_image1);
            albumImage2 = itemView.findViewById(R.id.album_image2);
            albumImage3 = itemView.findViewById(R.id.album_image3);
        }

        public void bind(ArtistData artistData) {
            //Log.d("adapter_artistDataList", artistData.getName());
            Glide.with(context)
                    .load(artistData.getImage())
                    .into(artistImage);
            artistName.setText(artistData.getName());
            followers.setText(String.format("%d followers", artistData.getFollowers()));
            Log.d("getSpotifyUrl", artistData.getSpotifyUrl());
            spotifyUrl.setText(artistData.getSpotifyUrl());
            progressView.setMax(100);
            Log.d("getPopularity", String.valueOf(artistData.getPopularity()));
            progressView.setProgress(artistData.getPopularity());
            popularityNumber.setText(String.valueOf(artistData.getPopularity()));

            List<String> albumCoverImages = artistData.getAlbumCoverImages();

            if (albumCoverImages.size() > 0) {
                Glide.with(context)
                        .load(albumCoverImages.get(0))
                        .into(albumImage1);
            }

            if (albumCoverImages.size() > 1) {
                Glide.with(context)
                        .load(albumCoverImages.get(1))
                        .into(albumImage2);
            }

            if (albumCoverImages.size() > 2) {
                Glide.with(context)
                        .load(albumCoverImages.get(2))
                        .into(albumImage3);
            }
            // Set up popularAlbums RecyclerView with an appropriate adapter
            // popularAlbums.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            // popularAlbums.setAdapter(new PopularAlbumsAdapter(context, artistData.getAlbumCoverImages()));
        }
    }
}