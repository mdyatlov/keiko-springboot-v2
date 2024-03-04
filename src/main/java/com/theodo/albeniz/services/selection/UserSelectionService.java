package com.theodo.albeniz.services.selection;

import com.theodo.albeniz.dto.Tune;
import com.theodo.albeniz.mappers.TuneMapper;
import com.theodo.albeniz.model.TuneEntity;
import com.theodo.albeniz.model.UserEntity;
import com.theodo.albeniz.repositories.TuneRepository;
import com.theodo.albeniz.repositories.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UserSelectionService {

    private final UserEntityRepository userRepository;
    private final TuneRepository tuneRepository;
    private final TuneMapper tuneMapper;

    public UUID addToSelection(UUID musicId, String userName) {
        UserEntity userEntity = getCurrentUser(userName);
        TuneEntity tune = getRequestedTune(musicId);
        userEntity.addSelection(tune);
        userRepository.save(userEntity);
        return musicId;
    }

    public UUID removeFromSelection(UUID musicId, String userName) {
        UserEntity userEntity = getCurrentUser(userName);
        TuneEntity tune = getRequestedTune(musicId);
        userEntity.removeSelection(tune);
        userRepository.save(userEntity);
        return musicId;
    }

    private TuneEntity getRequestedTune(UUID musicId) {
        Optional<TuneEntity> tune = tuneRepository.findById(musicId);
        if (tune.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tune could not be found");
        }
        return tune.get();
    }

    private UserEntity getCurrentUser(String userName) {
        UserEntity userEntity = userRepository.findByUsername(userName);
        if (userEntity == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User could not be found");
        }
        return userEntity;
    }

    public List<Tune> getSelection(String userName) {
        UserEntity currentUser = getCurrentUser(userName);
        if(currentUser.getSelection() == null) return Collections.emptyList();
        return currentUser.getSelection().stream()
                .map(tuneMapper::from)
                .sorted(Comparator.comparing(Tune::getTitle))
                .collect(Collectors.toList());
    }
}
