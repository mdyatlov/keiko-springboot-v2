package com.theodo.albeniz.services;

import com.theodo.albeniz.dto.Tune;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LibraryService {
    private final static Map<Integer, Tune> LIBRARY = new HashMap<>();

    static {
        // ADD static values (temporary)
        LIBRARY.put(1, new Tune(1, "Thriller", "MJ"));
        LIBRARY.put(2, new Tune(2, "Prelude and Fugue in C minor", "Bach"));
        LIBRARY.put(3, new Tune(3, "The Little Foam Man", "Patrick S."));
    }
    public Collection<Tune> getAll(String query) {
        // In order to return the list of tunes in a given order (and to be sure that tests will always be OK)
        return LIBRARY.values().stream()
                .sorted(Comparator.comparing(Tune::getId))
                .filter(tune -> query == null || tune.getTitle().contains(query))
                .collect(Collectors.toList());
    }

    public Tune getOne(int id){
        return LIBRARY.get(id);
    }
}
