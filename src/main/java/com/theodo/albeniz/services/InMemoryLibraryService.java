package com.theodo.albeniz.services;

import com.theodo.albeniz.dto.Tune;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Profile("memory")
public class InMemoryLibraryService implements LibraryService {
    private final static Map<UUID, Tune> LIBRARY = new HashMap<>();

    static {
        // ADD static values (temporary)
        addStaticTune(new Tune(UUID.fromString("ebc42f50-ea5e-4f4d-bf30-7755d382eb21"), "Thriller", "MJ"));
        addStaticTune(new Tune(UUID.fromString("f1c236cb-3ee5-47e8-9034-d3ebf85a6b76"), "Prelude and Fugue in C minor", "Bach"));
        addStaticTune(new Tune(UUID.fromString("e72750f9-76b8-4cdf-8469-45ed9edc5270"), "The Little Foam Man", "Patrick S."));
    }

    private static void addStaticTune(final Tune tune) {
        LIBRARY.put(tune.getId(), tune);
    }

    @Override
    public Collection<Tune> getAll(String query) {
        // In order to return the list of tunes in a given order (and to be sure that tests will always be OK)
        return LIBRARY.values().stream()
                .sorted(Comparator.comparing(Tune::getId))
                .filter(tune -> query == null || tune.getTitle().contains(query))
                .collect(Collectors.toList());
    }

    @Override
    public Tune getOne(UUID id){
        return LIBRARY.get(id);
    }

    @Override
    public UUID addTune(Tune tune) {
        // DO NOTHING in static memory version
        return null;
    }

    @Override
    public void removeTune(UUID id) {
        // DO NOTHING in static memory version
    }
}
