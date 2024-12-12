package com.theodo.albeniz.repositories;

import com.theodo.albeniz.model.TuneEntity;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface TuneRepository extends JpaRepository<TuneEntity, UUID> {
  @Query("SELECT tune FROM TuneEntity tune WHERE tune.title LIKE %:query%")
  List<TuneEntity> searchBy(@Param("query") String query, Pageable pageable);

  List<TuneEntity> findByAuthor(String author);
}
