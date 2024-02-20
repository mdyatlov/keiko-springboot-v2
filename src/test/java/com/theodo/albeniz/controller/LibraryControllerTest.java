package com.theodo.albeniz.controller;

import com.theodo.albeniz.services.InMemoryLibraryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LibraryController.class)
@AutoConfigureMockMvc
@ActiveProfiles("memory")
@Import(value = {InMemoryLibraryService.class})
class LibraryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testGetLibraryRoute() throws Exception {
        mockMvc.perform(get("/library/music").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                    {'title':'Thriller','author':'MJ'},
                                    {'title':'Prelude and Fugue in C minor','author':'Bach'},
                                    {'title':'The Little Foam Man','author':'Patrick S.'}
                                ]
                                """
                ));
    }

    @Test
    public void testGetOneTune() throws Exception {
        mockMvc.perform(get("/library/music/f1c236cb-3ee5-47e8-9034-d3ebf85a6b76").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                    {'title':'Prelude and Fugue in C minor','author':'Bach'}
                                """
                ));
    }

    @Test
    public void testGetOneTuneNotExisting() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/library/music/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertEquals("", contentAsString);
    }

    @Test
    public void findMusic() throws Exception {
        mockMvc.perform(get("/library/music?query=iller")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                    {'title':'Thriller','author':'MJ'}
                                ]
                                """));
    }
}
