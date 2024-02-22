package com.theodo.albeniz.repositories;

import com.theodo.albeniz.model.TuneEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface TuneRepository extends CrudRepository<TuneEntity, UUID> {
}
