package com.theodo.albeniz.services;

import com.theodo.albeniz.dto.Tune;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public interface LibraryService {
    Collection<Tune> getAll(String query);

    Collection<Tune> getAllByAuthor(String author);

    Tune getOne(UUID id);

    UUID addTune(Tune tune);

    boolean removeTune(UUID id);

    boolean isExist(UUID id);

    boolean modifyTune(Tune tune);
}
