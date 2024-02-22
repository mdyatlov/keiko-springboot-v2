package com.theodo.albeniz.services;

import com.theodo.albeniz.config.ApplicationConfig;
import com.theodo.albeniz.dto.Tune;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Profile("!memory")
@RequiredArgsConstructor
public class InDatabaseLibraryService implements LibraryService {
    private final Map<UUID, Tune> library = new HashMap<>();

    private final ApplicationConfig applicationConfig;

    @Override
    public Collection<Tune> getAll(String query) {
        return library.values().stream()
                .sorted(getComparator(applicationConfig.getApi().isAscending()))
                .limit(applicationConfig.getApi().getMaxCollection())
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

    @Override
    public boolean removeTune(UUID id) {
        Tune removed = library.remove(id);
        return removed != null;
    }

    @Override
    public boolean isExist(UUID id) {
        return library.containsKey(id);
    }

    @Override
    public boolean modifyTune(Tune tune) {
        if(isExist(tune.getId())){
            library.put(tune.getId(), tune);
            return true;
        }
        return false;
    }

    private Comparator<? super Tune> getComparator(boolean asc) {
        return asc ? Comparator.comparing(Tune::getTitle) : Comparator.comparing(Tune::getTitle).reversed();
    }
}
