package com.theodo.albeniz.services;

import com.theodo.albeniz.config.ApplicationConfig;
import com.theodo.albeniz.dto.Tune;
import com.theodo.albeniz.mappers.TuneMapper;
import com.theodo.albeniz.model.TuneEntity;
import com.theodo.albeniz.repositories.TuneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Profile("!memory")
@RequiredArgsConstructor
public class InDatabaseLibraryService implements LibraryService {

    private final ApplicationConfig applicationConfig;
    private final TuneRepository tuneRepository;
    private final TuneMapper tuneMapper;

    @Override
    public Collection<Tune> getAll(String query) {
        Sort.Direction direction = applicationConfig.getApi().isAscending() ? Sort.Direction.ASC : Sort.Direction.DESC;
        PageRequest pageRequest = PageRequest.of(0, applicationConfig.getApi().getMaxCollection(),
                Sort.by(direction, "title"));

        if(query != null) {
            List<TuneEntity> tuneEntities = tuneRepository.searchBy(query, pageRequest);
            return tuneEntities.stream().map(tuneMapper::from).collect(Collectors.toList());
        } else {
            Page<TuneEntity> tuneEntities = tuneRepository.findAll(pageRequest);
            return tuneEntities.stream().map(tuneMapper::from).collect(Collectors.toList());
        }
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

    @Override
    public Collection<Tune> getAllFromAuthor(String author) {
        List<TuneEntity> byAuthor = tuneRepository.findByAuthor(author);
        return byAuthor.stream().map(tuneMapper::from).collect(Collectors.toList());
    }

}
