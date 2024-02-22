package com.theodo.albeniz.services;

import com.theodo.albeniz.config.ApplicationConfig;
import com.theodo.albeniz.dto.Tune;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
class InDatabaseLibraryServiceTest {
    private final InDatabaseLibraryService libraryService = new InDatabaseLibraryService(createMockConfiguration());

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

    @Test
    public void testRemove() {
        assertEquals(0, libraryService.getAll(null).size());

        UUID uuid1 = libraryService.addTune(new Tune(null, "Hello", "World Singers"));
        UUID uuid2 = libraryService.addTune(new Tune(null, "Hello !!!", "World Singers !!!"));
        UUID uuid3 = libraryService.addTune(new Tune(null, "Hello !!!!!", "World Singers !!!!!"));

        assertEquals(3, libraryService.getAll(null).size());

        libraryService.removeTune(uuid2);
        assertEquals(2, libraryService.getAll(null).size());

        libraryService.removeTune(uuid1);
        assertEquals(1, libraryService.getAll(null).size());

        libraryService.removeTune(uuid3);
        assertEquals(0, libraryService.getAll(null).size());

        libraryService.removeTune(UUID.randomUUID());

    }

    @Test
    public void testLimitSize() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.getApi().setMaxCollection(4);
        InDatabaseLibraryService dataLibraryService = createLibraryWithManyTunes(applicationConfig);
        Collection<Tune> all = dataLibraryService.getAll(null);
        List<String> tuneNames = all.stream().map(Tune::getTitle).collect(Collectors.toList());
        assertThat(tuneNames, is(Arrays.asList("Tune:0", "Tune:1", "Tune:10", "Tune:11")));
    }

    @Test
    public void testOrder() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        applicationConfig.getApi().setMaxCollection(4);
        applicationConfig.getApi().setAscending(false);
        InDatabaseLibraryService dataLibraryService = createLibraryWithManyTunes(applicationConfig);
        Collection<Tune> all = dataLibraryService.getAll(null);
        List<String> tuneNames = all.stream().map(Tune::getTitle).collect(Collectors.toList());
        assertThat(tuneNames, is(Arrays.asList("Tune:99", "Tune:98", "Tune:97", "Tune:96")));
    }

    private static ApplicationConfig createMockConfiguration() {
        ApplicationConfig applicationConfig = new ApplicationConfig();
        ApplicationConfig.ApiConfiguration api = new ApplicationConfig.ApiConfiguration();
        api.setAscending(true);
        api.setMaxCollection(100);
        applicationConfig.setApi(api);
        return applicationConfig;
    }

    private InDatabaseLibraryService createLibraryWithManyTunes(ApplicationConfig applicationConfig) {
        InDatabaseLibraryService dataLibraryService = new InDatabaseLibraryService(applicationConfig);
        for (int i = 0; i < 100; i++) {
            dataLibraryService.addTune(new Tune(UUID.randomUUID(), "Tune:" + i, "Me"));
        }
        return dataLibraryService;
    }
}
