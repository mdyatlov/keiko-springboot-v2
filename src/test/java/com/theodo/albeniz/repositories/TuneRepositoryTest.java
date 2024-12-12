package com.theodo.albeniz.repositories;

import com.theodo.albeniz.model.TuneEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@Sql(
        statements = "INSERT INTO TUNE(ID, TITLE, AUTHOR) " +
        "VALUES " +
        "   ('f48ae564-9574-417d-b458-7933cc824f56', 'AABCC', '1111')," +
        "   ('4577650d-b40d-4c72-9d39-c6916ad063c7', 'ABC', '2222')," +
        "   ('a9900fc6-e4f1-4a66-aa17-d66fc10339c0', 'XXXXX', 1111)" +
        ";"
    )
@DataJpaTest
class TuneRepositoryTest {

    @Autowired
    private TuneRepository tuneRepository;

    @Test
    public void testCRUD() {
        assertThat(tuneRepository.count()).isEqualTo(3);
        TuneEntity savedTune = tuneRepository.save(new TuneEntity(null, "e", "f", "g"));
        
        assertThat(savedTune.getId()).isNotNull();
        
        assertThat(tuneRepository.count()).isEqualTo(4);
        assertThat(tuneRepository.existsById(savedTune.getId())).isTrue();
        assertThat(tuneRepository.existsById(UUID.randomUUID())).isFalse();

        tuneRepository.save(new TuneEntity(savedTune.getId(), "E", "F", "G"));

        Optional<TuneEntity> optional = tuneRepository.findById(savedTune.getId());
        assertThat(optional.isPresent()).isTrue();
        assertThat(optional.get().getTitle()).isEqualTo("E");

        tuneRepository.deleteById(savedTune.getId());
        assertThat(tuneRepository.count()).isEqualTo(3);
        assertThat(tuneRepository.existsById(savedTune.getId())).isFalse();
    }

    @Test
    public void testSearchBy() {
        assertThat(tuneRepository.searchBy("AABCC", null)).hasSize(1);
        assertThat(tuneRepository.searchBy("ABC", null)).hasSize(2);
        assertThat(tuneRepository.searchBy("ZZZ", null)).isEmpty();
    }

    @Test
    public void testFindByAuthor() {
        assertThat(tuneRepository.findByAuthor("1111")).hasSize(2);
        assertThat(tuneRepository.findByAuthor("2222")).hasSize(1);
        assertThat(tuneRepository.findByAuthor("3333")).isEmpty();
    }
}
