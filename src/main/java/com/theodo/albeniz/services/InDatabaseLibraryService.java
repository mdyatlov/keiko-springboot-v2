package com.theodo.albeniz.services;

import com.theodo.albeniz.dto.Tune;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@Profile("!memory")
public class InDatabaseLibraryService implements LibraryService {
    @Override
    public Collection<Tune> getAll(String query) {
        return Collections.emptyList();
    }

    @Override
    public Tune getOne(int id) {
        return null;
    }
}
