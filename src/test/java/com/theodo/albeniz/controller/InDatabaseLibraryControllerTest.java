package com.theodo.albeniz.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.theodo.albeniz.config.ApplicationConfig;
import com.theodo.albeniz.dto.Tune;
import com.theodo.albeniz.services.InDatabaseLibraryService;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LibraryController.class)
@AutoConfigureMockMvc
@ActiveProfiles("database")
@Import(value = ApplicationConfig.class)
class InDatabaseLibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @MockBean
    private InDatabaseLibraryService libraryService;

    @BeforeEach
    public void setUp() {
        Collection<Tune> all = libraryService.getAll(null);
        for (Tune tune : all) {
            libraryService.removeTune(tune.getId());
        }
    }

    @Test
    public void testAddTune() throws Exception {
        checkThatThereIsNoTuneInLibrary();

        Tune toBeInsertedTune = new Tune();
        toBeInsertedTune.setTitle("ABC");
        toBeInsertedTune.setAuthor("Jackson5");
        UUID toBeCreatedId = UUID.fromString("574bfb93-a5f7-48ab-9eee-3b46ed019dd5");
        System.out.println("very hehe" + toBeCreatedId);
        when(libraryService.addTune(any())).thenReturn(toBeCreatedId);

        Tune insertedTune = new Tune();
        insertedTune.setId(toBeCreatedId);
        insertedTune.setAuthor(toBeInsertedTune.getAuthor());
        insertedTune.setTitle(toBeInsertedTune.getTitle());
        System.out.println("very hehe" + insertedTune.getId());
        when(libraryService.getOne(toBeCreatedId)).thenReturn(insertedTune);

        UUID createdId = insertOneTune("""
                { "title": "ABC", "author": "Jackson5" }
                """);

        assertEquals(toBeCreatedId, createdId);
    }

    @Test
    public void testAddTuneValidation() throws Exception {
        mockMvc.perform(post("/library/music").contentType(MediaType.APPLICATION_JSON)
                        .content("\"author\": \"Jackson5\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testComplexValidation() throws Exception {
        mockMvc.perform(post("/library/music").contentType(MediaType.APPLICATION_JSON)
                        .content("{ \"author\": \"Chantal G.\", \"title\": \"Pandi Panda\" }"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteTune() throws Exception {

        UUID toBeDeleted = UUID.randomUUID();
        when(libraryService.removeTune(toBeDeleted)).thenReturn(true);

        mockMvc.perform(MockMvcRequestBuilders.delete("/library/music/" + toBeDeleted.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteTuneNotFound() throws Exception {
        UUID toBeDeleted = UUID.randomUUID();
        when(libraryService.removeTune(toBeDeleted)).thenReturn(false);

        mockMvc.perform(MockMvcRequestBuilders.delete("/library/music/" + toBeDeleted.toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteTuneNotExist() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/library/music/" + UUID.randomUUID().toString()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private UUID insertOneTune(@Language("json") final String tune) throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/library/music").contentType(MediaType.APPLICATION_JSON)
                        .content(tune))
                .andExpect(status().isCreated())
                .andReturn();

        String bodyAsString = mvcResult.getResponse().getContentAsString();
        System.out.println("hehe" + tune);
        System.out.println("hehe" + mvcResult);
        ObjectMapper objectMapper = new ObjectMapper();
        Tune insertedTune = objectMapper.reader().readValue(bodyAsString, Tune.class);
        return insertedTune.getId();
    }

    private void checkThatThereIsNoTuneInLibrary() throws Exception {
        System.out.println("hehe");
        mockMvc.perform(get("/library/music").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}
