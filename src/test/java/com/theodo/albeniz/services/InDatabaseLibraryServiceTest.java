package com.theodo.albeniz.services;

import com.theodo.albeniz.dto.Tune;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class InDatabaseLibraryServiceTest {
    private final InDatabaseLibraryService libraryService = new InDatabaseLibraryService();

    @Test
    public void testAllMethods() {
        assertEquals(0, libraryService.getAll(null).size());

        libraryService.addTune(new Tune(17, "Hello", "World Singers"));
        libraryService.addTune(new Tune(18, "Hello !!!", "World Singers !!!"));
        libraryService.addTune(new Tune(25, "Hello !!!!!", "World Singers !!!!!"));

        assertEquals(3, libraryService.getAll(null).size());
        assertEquals("World Singers", libraryService.getOne(17).getAuthor());
        assertEquals("World Singers !!!", libraryService.getOne(18).getAuthor());
        assertEquals("World Singers !!!!!", libraryService.getOne(25).getAuthor());
        assertNull(libraryService.getOne(1000));
    }
}
