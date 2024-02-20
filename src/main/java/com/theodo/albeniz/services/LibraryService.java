package com.theodo.albeniz.services;

import com.theodo.albeniz.dto.Tune;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public interface LibraryService {
    Collection<Tune> getAll(String query);

    Tune getOne(int id);
}
