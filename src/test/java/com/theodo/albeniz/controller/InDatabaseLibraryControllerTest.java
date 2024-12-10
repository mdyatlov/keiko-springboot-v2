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
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collection;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LibraryController.class)
@AutoConfigureMockMvc
@ActiveProfiles("database")
@Import(value = { InDatabaseLibraryService.class, ApplicationConfig.class })
class InDatabaseLibraryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
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

    insertOneTune("{\"title\": \"ABC\",\"author\": \"Jackson5\"}");

    insertOneTune("{\"title\": \"Highway to Hell\",\"author\": \"AC-DC\"}");

    mockMvc.perform(get("/library/music").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(
            """
                     [
                       { "title": "ABC", "author": "Jackson5" },
                       { "title": "Highway to Hell", "author": "AC-DC" }
                     ]
                """));
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

    UUID uuid1 = insertOneTune("{ \"title\": \"ABC\", \"author\": \"Jackson5\" }");

    insertOneTune("{ \"title\": \"Highway to Hell\", \"author\": \"AC-DC\" }");

    mockMvc.perform(get("/library/music").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(
            """
                 [
                   { "title": "ABC", "author": "Jackson5" },
                   { "title": "Highway to Hell", "author": "AC-DC" }
                 ]
                """));

    mockMvc
        .perform(
            MockMvcRequestBuilders.delete("/library/music/" + uuid1.toString()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());

    mockMvc.perform(get("/library/music").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(content().json(
            """
                 [
                   { "title": "Highway to Hell", "author": "AC-DC" }
                 ]
                """));
  }

  @Test
  public void testDeleteTuneNotExist() throws Exception {
    mockMvc
        .perform(MockMvcRequestBuilders.delete("/library/music/" + UUID.randomUUID().toString())
            .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  private UUID insertOneTune(@Language("json") final String tune) throws Exception {
    MvcResult mvcResult = mockMvc.perform(post("/library/music").contentType(MediaType.APPLICATION_JSON)
        .content(tune))
        .andExpect(status().isCreated())
        .andReturn();

    String bodyAsString = mvcResult.getResponse().getContentAsString();
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
