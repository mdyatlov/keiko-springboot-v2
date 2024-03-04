package com.theodo.albeniz.services.album;

import org.springframework.lang.Nullable;

public interface AlbumService {
    @Nullable
    String getAlbum(String title, String author);
}
