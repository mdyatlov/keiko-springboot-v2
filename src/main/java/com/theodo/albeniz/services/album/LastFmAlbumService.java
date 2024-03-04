package com.theodo.albeniz.services.album;

import com.theodo.albeniz.clients.lastfm.LastFMApiClient;
import com.theodo.albeniz.clients.lastfm.TrackDto;
import com.theodo.albeniz.config.ApplicationConfig;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.stereotype.Service;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class LastFmAlbumService implements AlbumService {
    private final LastFMApiClient lastFMApiClient;
    private final ApplicationConfig applicationConfig;

    @Override
    public String getAlbum(String title, String author) {
        try {
            Call<TrackDto> trackDtoCall = lastFMApiClient.getProjects(
                    applicationConfig.getLastFm().getApiKey(),
                    author, title);
            Response<TrackDto> dtoResponse = trackDtoCall.execute();
            return getAlbumNameFromDto(dtoResponse);
        } catch (IOException e) {
            throw new RuntimeException("Can't call LastFM", e);
        }
    }

    @Nullable
    private static String getAlbumNameFromDto(Response<TrackDto> dtoResponse) {
        if(dtoResponse.isSuccessful()){
            TrackDto trackDto = dtoResponse.body();
            if(trackDto == null) return null;
            return trackDto.getAlbum();
        }
        return null;
    }
}
