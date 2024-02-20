package com.theodo.albeniz.services;

import com.theodo.albeniz.dto.Tune;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Profile("!memory")
public class InDatabaseLibraryService implements LibraryService {
    private final Map<UUID, Tune> library = new HashMap<>();

    @Override
    public Collection<Tune> getAll(String query) {
        return library.values().stream()
                .sorted(Comparator.comparing(Tune::getTitle))
                .filter(tune -> query == null || tune.getTitle().contains(query))
                .collect(Collectors.toList());
    }

    @Override
    public Tune getOne(UUID id) {
        return library.get(id);
    }

    @Override
    public UUID addTune(Tune tune) {
        UUID uuid = UUID.randomUUID();
        tune.setId(uuid);
        library.put(tune.getId(), tune);
        return uuid;
    }
}
