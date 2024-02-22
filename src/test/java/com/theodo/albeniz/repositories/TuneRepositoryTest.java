package com.theodo.albeniz.repositories;

import com.theodo.albeniz.model.TuneEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;
import java.util.UUID;


import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
class TuneRepositoryTest {

    @Autowired
    private TuneRepository tuneRepository;

    @Test
     public void testCRUD() {
         assertThat(tuneRepository.count()).isEqualTo(0);
         TuneEntity save1 = tuneRepository.save(new TuneEntity(null, "b", "c", "d"));
         TuneEntity save2 =tuneRepository.save(new TuneEntity(null, "e", "f", "g"));
         TuneEntity save3 =tuneRepository.save(new TuneEntity(null, "h", "i", "j"));

         assertThat(save1.getId()).isNotNull();
         assertThat(save2.getId()).isNotNull();
         assertThat(save3.getId()).isNotNull();

         assertThat(tuneRepository.count()).isEqualTo(3);
         assertThat(tuneRepository.existsById(save2.getId())).isTrue();
         assertThat(tuneRepository.existsById(UUID.randomUUID())).isFalse();

         tuneRepository.save(new TuneEntity(save2.getId(), "E", "F", "G"));

         Optional<TuneEntity> optional = tuneRepository.findById(save2.getId());
         assertThat(optional.isPresent()).isTrue();
         assertThat(optional.get().getTitle()).isEqualTo("E");

         tuneRepository.deleteById(save2.getId());
         assertThat(tuneRepository.count()).isEqualTo(2);
         assertThat(tuneRepository.existsById(save2.getId())).isFalse();
     }
}
