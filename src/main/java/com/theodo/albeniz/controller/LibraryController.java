package com.theodo.albeniz.controller;

import com.theodo.albeniz.dto.Tune;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("library")
public class LibraryController {
    private final static Map<Integer, Tune> LIBRARY = new HashMap<>();

    static {
        // ADD static values (temporary)
        LIBRARY.put(1, new Tune(1, "Thriller", "MJ"));
        LIBRARY.put(2, new Tune(2, "Prelude and Fugue in C minor", "Bach"));
        LIBRARY.put(3, new Tune(3, "The Little Foam Man", "Patrick S."));
    }

    @GetMapping("music")
    public Collection<Tune> getMusic(@RequestParam(name = "query", required = false) String query){
        return LIBRARY.values().stream()
                .sorted(Comparator.comparing(Tune::getId))
                .filter(tune -> query == null || tune.getTitle().contains(query))
                .collect(Collectors.toList());
    }

    @GetMapping("music/{id}")
    public Tune getMusic(@PathVariable int id){
        return LIBRARY.get(id);
    }

}
