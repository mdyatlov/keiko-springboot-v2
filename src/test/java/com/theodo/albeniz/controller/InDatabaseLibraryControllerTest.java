package com.theodo.albeniz.controller;

import com.theodo.albeniz.services.InDatabaseLibraryService;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LibraryController.class)
@AutoConfigureMockMvc
@ActiveProfiles("database")
@Import(value = {InDatabaseLibraryService.class})
class InDatabaseLibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAddTune() throws Exception {
        checkThatThereIsNoTuneInLibrary();

        insertOneTune("""
                {
                    "title": "ABC",
                    "author": "Jackson5"
                }
                """);

        insertOneTune("""
        {
            "title": "Highway to Hell",
            "author": "AC-DC"
        }
        """);

        mockMvc.perform(get("/library/music").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                  {
                                    "title": "ABC",
                                    "author": "Jackson5"
                                  },
                                  {
                                    "title": "Highway to Hell",
                                    "author": "AC-DC"
                                  }                                  
                                ]
                               """
                ));
    }

    @Test
    public void testAddTuneValidation() throws Exception {
        mockMvc.perform(post("/library/music").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                        {
                                            "author": "Jackson5"
                                        }
                                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testComplexValidation() throws Exception {
        mockMvc.perform(post("/library/music").contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                        {
                                            "author": "Chantal G.",
                                            "title": "Pandi Panda"
                                        }
                                        """))
                .andExpect(status().isBadRequest());
    }

    private void insertOneTune(@Language("json") final String tune) throws Exception {
        mockMvc.perform(post("/library/music").contentType(MediaType.APPLICATION_JSON)
                        .content(tune))
                .andExpect(status().isOk());
    }

    private void checkThatThereIsNoTuneInLibrary() throws Exception {
        mockMvc.perform(get("/library/music").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                ]
                                """
                ));
    }
}
