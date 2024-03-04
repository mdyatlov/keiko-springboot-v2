package com.theodo.albeniz.clients.lastfm;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class TrackDto {
    private String name;
    private String album;

    @SuppressWarnings("unchecked")
    @JsonProperty("track")
    private void unpackNested(Map<String, Object> track) {
        this.name = (String) track.get("name");
        Map<String, Object> album = (Map<String, Object>) track.get("album");
        if (album != null) {
            this.album = (String) album.get("title");
        }
    }
}
