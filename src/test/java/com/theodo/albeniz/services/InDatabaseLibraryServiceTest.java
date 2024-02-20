package com.theodo.albeniz.services;

import com.theodo.albeniz.dto.Tune;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class InDatabaseLibraryServiceTest {
    private final InDatabaseLibraryService libraryService = new InDatabaseLibraryService();

    @Test
    public void testAllMethods() {
        assertEquals(0, libraryService.getAll(null).size());

        UUID uuid1 = libraryService.addTune(new Tune(null, "Hello", "World Singers"));
        UUID uuid2 = libraryService.addTune(new Tune(null, "Hello !!!", "World Singers !!!"));
        UUID uuid3 = libraryService.addTune(new Tune(null, "Hello !!!!!", "World Singers !!!!!"));

        assertEquals(3, libraryService.getAll(null).size());
        assertEquals("World Singers", libraryService.getOne(uuid1).getAuthor());
        assertEquals("World Singers !!!", libraryService.getOne(uuid2).getAuthor());
        assertEquals("World Singers !!!!!", libraryService.getOne(uuid3).getAuthor());
        assertNull(libraryService.getOne(UUID.randomUUID()));
    }
}
