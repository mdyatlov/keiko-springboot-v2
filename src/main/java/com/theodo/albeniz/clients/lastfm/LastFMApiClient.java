package com.theodo.albeniz.clients.lastfm;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface LastFMApiClient {

    @GET("/2.0/?method=track.getInfo&format=json")
    Call<TrackDto> getProjects(
        @Query("api_key") String api_key,
        @Query("artist") String artist,
        @Query("track") String track);
}
