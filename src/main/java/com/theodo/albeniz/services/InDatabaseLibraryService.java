package com.theodo.albeniz.services;

import com.theodo.albeniz.config.ApplicationConfig;
import com.theodo.albeniz.dto.Tune;
import com.theodo.albeniz.mappers.TuneMapper;
import com.theodo.albeniz.model.TuneEntity;
import com.theodo.albeniz.repositories.TuneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Service
@Profile("!memory")
@RequiredArgsConstructor
public class InDatabaseLibraryService implements LibraryService {

    private final ApplicationConfig applicationConfig;
    private final TuneRepository tuneRepository;
    private final TuneMapper tuneMapper;

    @Override
    public Collection<Tune> getAll(String query) {
        Spliterator<TuneEntity> spliterator = tuneRepository.findAll().spliterator();
        Stream<TuneEntity> stream = StreamSupport.stream(spliterator, false);

        return stream
                .map(tuneMapper::from)
                .sorted(getComparator(applicationConfig.getApi().isAscending()))
                .limit(applicationConfig.getApi().getMaxCollection())
                .filter(tune -> query == null || tune.getTitle().contains(query))
                .collect(Collectors.toList());
    }

    @Override
    public Tune getOne(UUID id) {
        Optional<TuneEntity> optionalTuneEntity = tuneRepository.findById(id);
        return optionalTuneEntity.map(tuneMapper::from).orElse(null);
    }

    @Override
    public UUID addTune(Tune tune) {
        TuneEntity save = tuneRepository.save(tuneMapper.from(tune));
        return save.getId();
    }

    @Override
    public boolean removeTune(UUID id) {
        if (tuneRepository.existsById(id)) {
            tuneRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public boolean isExist(UUID id) {
        return tuneRepository.existsById(id);
    }

    @Override
    public boolean modifyTune(Tune tune) {
        if(isExist(tune.getId())){
            tuneRepository.save(tuneMapper.from(tune));
            return true;
        }
        return false;
    }

    private Comparator<? super Tune> getComparator(boolean asc) {
        return asc ? Comparator.comparing(Tune::getTitle) : Comparator.comparing(Tune::getTitle).reversed();
    }
}
