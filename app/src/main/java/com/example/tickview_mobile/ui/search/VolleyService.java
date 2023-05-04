package com.example.tickview_mobile.ui.search;
import android.content.Context;
import android.location.Location;
import android.net.Uri;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ch.hsr.geohash.GeoHash;
public class VolleyService {

    private final RequestQueue requestQueue;
    private final Context context;

    public VolleyService(Context context) {
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    public interface FetchLocationCallback {
        void onSuccess(String geohash);
        void onError(String message);
    }
    public interface AutoCompleteCallback {
        void onSuccess(List<String> results);

        void onError(String message);
    }

    public interface SearchEventCallback {
        void onSuccess(JSONObject response);

        void onError(String message);
    }

    public interface FetchEventDetailsCallback {
        void onSuccess(JSONObject response);

        void onError(String message);
    }
    public interface FetchVenueDetailsCallback {
        void onSuccess(JSONObject response);

        void onError(String message);
    }
    public interface FetchArtistDetailsCallback {
        void onSuccess(JSONObject response);

        void onError(String message);
    }
    public interface FetchArtistAlbumCallback {
        void onSuccess(JSONObject response);

        void onError(String message);
    }
    public void fetchLocation(boolean autoDetect, String address, FetchLocationCallback callback) {
        String url;

        if (autoDetect) {
            url = "https://ipinfo.io/?token=508efef6f3e453";
        } else {
            url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encodeURIComponent(address) + "&key=" + ServerConfig.GAPI_KEY;
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        double latitude;
                        double longitude;

                        if (autoDetect) {
                            String[] loc = response.getString("loc").split(",");
                            latitude = Double.parseDouble(loc[0]);
                            longitude = Double.parseDouble(loc[1]);
                        } else {
                            JSONObject geometry = response.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                            latitude = geometry.getDouble("lat");
                            longitude = geometry.getDouble("lng");
                        }

                        GeoHash geoHash = GeoHash.withBitPrecision(latitude, longitude, 9 * 5);
                        String geohashString = geoHash.toBase32();
                        callback.onSuccess(geohashString);

                    } catch (Exception e) {
                        callback.onError(e.getMessage());
                    }
                }, error -> callback.onError(error.getMessage()));

        requestQueue.add(jsonObjectRequest);
    }

    public void searchEvent(String keyword, int distance, String category, String geoPoint, SearchEventCallback callback) {
        String url = ServerConfig.SERVER_URL + "/events?keyword=" + encodeURIComponent(keyword) + "&distance=" + distance + "&category=" + encodeURIComponent(category) + "&geoPoint=" + encodeURIComponent(geoPoint);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    callback.onSuccess(response);
                }, error -> callback.onError(error.getMessage()));

        requestQueue.add(jsonObjectRequest);
    }

    public void autoComplete(String keyword, ArrayAdapter<String> adapter, AutoCompleteCallback callback) {
        String url = ServerConfig.SERVER_URL + "/autoComplete?keyword=" + encodeURIComponent(keyword);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    try {
                        if (response.has("_embedded") && response.getJSONObject("_embedded").has("attractions")) {
                            JSONArray attractions = response.getJSONObject("_embedded").getJSONArray("attractions");
                            List<String> results = new ArrayList<>();
                            for (int i = 0; i < attractions.length(); i++) {
                                results.add(attractions.getJSONObject(i).getString("name"));
                            }
                            Log.d("AutoComplete", "Results: " + results);
                            callback.onSuccess(results);
                        }
                    } catch (Exception e) {
                        callback.onError(e.getMessage());
                    }
                }, error -> callback.onError(error.getMessage()));

        requestQueue.add(jsonObjectRequest);
    }

    public void fetchEventDetails(String eventId, FetchEventDetailsCallback callback) {
        String url = ServerConfig.SERVER_URL + "/events/details?event_id=" + eventId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    callback.onSuccess(response);
                }, error -> callback.onError(error.getMessage()));

        requestQueue.add(jsonObjectRequest);
    }

//    public void fetchVenueDetails(String venueId, FetchVenueDetailsCallback callback) {
//        String url = ServerConfig.SERVER_URL + "/venues?id=" + venueId;
//
//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
//                (Request.Method.GET, url, null, response -> {
//                    callback.onSuccess(response);
//                }, error -> callback.onError(error.getMessage()));
//
//        requestQueue.add(jsonObjectRequest);
//    }

    public void fetchVenueDetails(String venueId, FetchVenueDetailsCallback callback) {
        String url = ServerConfig.SERVER_URL + "/venues?id=" + venueId;
        Log.d("EventUrl", ":" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null,
                        response -> {
                            try {
                                callback.onSuccess(response);
                            } catch (Exception e) {
                                Log.e("fetchVenueDetails", "Error in onResponse: " + e.getMessage());
                                callback.onError("Error while processing response: " + e.getMessage());
                            }
                        },
                        error -> {
                            try {
                                callback.onError(error.getMessage());
                            } catch (Exception e) {
                                Log.e("fetchVenueDetails", "Error in onErrorResponse: " + e.getMessage());
                                callback.onError("Error while handling error response: " + e.getMessage());
                            }
                        });

        requestQueue.add(jsonObjectRequest);
    }
    public void fetchArtistDetails(String artistName, FetchArtistDetailsCallback callback) {
        String url = ServerConfig.SERVER_URL + "/artist?keyword=" + Uri.encode(artistName);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    callback.onSuccess(response);
                }, error -> callback.onError(error.getMessage()));

        requestQueue.add(jsonObjectRequest);
    }

    public void fetchArtistAlbum(String artistId, FetchArtistAlbumCallback callback) {
        String url = ServerConfig.SERVER_URL + "/artist/album?id=" + artistId;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, response -> {
                    callback.onSuccess(response);
                }, error -> callback.onError(error.getMessage()));

        requestQueue.add(jsonObjectRequest);
    }
    private String encodeURIComponent(String s) {
        return Uri.encode(s);
    }

}