package com.theodo.albeniz.controller;

import com.theodo.albeniz.dto.Tune;
import com.theodo.albeniz.repositories.UserEntityRepository;
import com.theodo.albeniz.services.LibraryService;
import jakarta.servlet.http.Cookie;
import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;

import java.util.Collection;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserSelectionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LibraryService libraryService;

    @Autowired
    private UserEntityRepository userEntityRepository;

    private Cookie cookie;


    @BeforeEach
    public void loginUserBeforeTest() throws Exception {
        createUser();
        cookie = loginUser();
    }

    @AfterEach
    public void logoutUserAfterTest() throws Exception {
        mockMvc.perform(post("/logout")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        userEntityRepository.deleteAll();
    }

    @Test
    public void testAddRemoveSelection() throws Exception {
        // ARRANGE
        checkSelection("[]", cookie);

        insertTunesInLibrary(cookie);

        checkSelection("[]", cookie);

        // ACT
        Collection<Tune> allFromAuthor = libraryService.getAllFromAuthor("Claude N.");
        for (Tune tune : allFromAuthor) {
            mockMvc.perform(post("/selection/" + tune.getId())
                    .cookie(cookie)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

        // ASSERT
        checkSelection("""
                    [
                    {"title": "Nougayork", "author": "Claude N."},
                    {"title": "Cecile ma fille", "author": "Claude N."}
                    ]
                    """, cookie);

    }

    private void insertTunesInLibrary(Cookie cookie) throws Exception {
        insertOneTuneInLibrary(cookie, """
                         {
                            "title" : "Nougayork",
                            "author" : "Claude N."
                         }
                """);

        insertOneTuneInLibrary(cookie, """
                 {
                    "title" : "Into the Groove",
                    "author" : "Madonna"
                 }
        """);

        insertOneTuneInLibrary(cookie, """
                 {
                    "title" : "Le petit bonhomme en mousse",
                    "author" : "Patrick S."
                 }
        """);

        insertOneTuneInLibrary(cookie, """
                 {
                    "title" : "Cecile ma fille",
                    "author" : "Claude N."
                 }
        """);
    }

    private void insertOneTuneInLibrary(Cookie cookie, @Language("json") String tuneContent) throws Exception {
        mockMvc.perform(post("/library/music")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(tuneContent))
                .andExpect(status().isCreated());
    }

    private Cookie loginUser() throws Exception {
        RequestBuilder requestBuilder = formLogin().user("user2@gmail.com").password("pwd987654");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse();
        return response.getCookie("SESSION");
    }

    private void createUser() throws Exception {
        mockMvc.perform(post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"user2@gmail.com\", \"password\": \"pwd987654\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{'username': 'user2@gmail.com'}"));
    }

    private void checkSelection(@Language("json") final String expectedJson, Cookie cookie) throws Exception {
        mockMvc.perform(get("/selection")
                .cookie(cookie)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }
}
