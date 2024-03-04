package com.theodo.albeniz.controller;

import com.theodo.albeniz.repositories.UserEntityRepository;
import jakarta.servlet.http.Cookie;
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

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class ExternalApiAlbumIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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

        // ACT
        insertTunesInLibrary(cookie);

        // ASSERT
        mockMvc.perform(get("/library/music")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        """
                                [
                                    {"title": "Beat It","author":"Michael Jackson", "album":  "Thriller"}
                                ]
                                """
                ));

    }

    private void insertTunesInLibrary(Cookie cookie) throws Exception {
        mockMvc.perform(post("/library/music")
                        .cookie(cookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                         {
                                            "title" : "Beat It",
                                            "author" : "Michael Jackson"
                                         }
                                """))
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
}
