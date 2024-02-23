package com.theodo.albeniz.controller;

import com.theodo.albeniz.repositories.UserEntityRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles(profiles = "data")   // NEEDED TO DEFINE IN WHICH CONTEXT THE SERVER MUST BE LAUNCHED
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Test
    public void testSignUpUser() throws Exception {
        mockMvc.perform(post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"user1@gmail.com\", \"password\": \"my12345pwd\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{'username': 'user1@gmail.com'}"));

        assertEquals(1, userEntityRepository.count());

        mockMvc.perform(post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"user1@gmail.com\", \"password\": \"my12345pwd\"}"))
                .andExpect(status().isBadRequest());

        assertEquals(1, userEntityRepository.count());

        mockMvc.perform(post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"user2@gmail.com\", \"password\": \"my12345pwd\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{'username': 'user2@gmail.com'}"));

        assertEquals(2, userEntityRepository.count());
    }

}
