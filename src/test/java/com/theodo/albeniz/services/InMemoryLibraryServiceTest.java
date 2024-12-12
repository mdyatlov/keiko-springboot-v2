package com.theodo.albeniz.services;

import com.theodo.albeniz.dto.Tune;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryLibraryServiceTest {

    private final LibraryService libraryService = new InMemoryLibraryService();

    @Test
    public void testGetAllWithoutFilter(){
        Collection<Tune> all = libraryService.getAll(null);
        assertEquals(3, all.size());
    }
    @Test
    public void testGetAllWithMatchingFilter(){
        Collection<Tune> all = libraryService.getAll("Thrill");
        assertEquals(1, all.size());
    }
    @Test
    public void testGetAllWithNoMatchFilter(){
        Collection<Tune> all = libraryService.getAll("ACDC");
        assertEquals(0, all.size());
    }

    @Test
    public void testGetOne(){
        Tune tune = libraryService.getOne(UUID.fromString("ebc42f50-ea5e-4f4d-bf30-7755d382eb21"));
        assertEquals("Thriller", tune.getTitle());
    }
    @Test
    public void testGetOneNoMatch(){
        Tune tune = libraryService.getOne(UUID.randomUUID());
        assertNull(tune);
    }

    @Test
    public void testGetAllByAuthor(){
        Collection<Tune> all = libraryService.getAllByAuthor("MJ");
        assertEquals(1, all.size());
    }
}
