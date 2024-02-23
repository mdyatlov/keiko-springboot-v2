package com.theodo.albeniz.controller;

import com.theodo.albeniz.model.UserEntity;
import com.theodo.albeniz.repositories.UserEntityRepository;
import jakarta.servlet.http.Cookie;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserEntityRepository userEntityRepository;

    @Test
    public void testLoginOk() throws Exception {
        insertOneUser();

        RequestBuilder requestBuilder = formLogin().user("user1@gmail.com").password("my12345pwd");
        MockHttpServletResponse response = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andReturn().getResponse();
        Cookie session = response.getCookie("SESSION");

        assertNotNull(session);

        mockMvc.perform(post("/logout").cookie(session))
                .andExpect(status().isOk());

    }

    @Test
    public void testLoginFailed() throws Exception {
        insertOneUser();

        RequestBuilder requestBuilder = formLogin().user("user1@gmail.com").password("XXXX");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testUserDoesNotExist() throws Exception {
        insertOneUser();

        RequestBuilder requestBuilder = formLogin().user("XXXX@gmail.com").password("XXXX");
        mockMvc.perform(requestBuilder)
                .andExpect(status().isUnauthorized());
    }

/*    private void insertOneUser() throws Exception {
        mockMvc.perform(post("/users/sign-up")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\": \"user1@gmail.com\", \"password\": \"my12345pwd\"}"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "{'username': 'user1@gmail.com'}"));

        assertEquals(1, userEntityRepository.count());
    }*/

    private void insertOneUser() throws Exception {
        UserEntity entity = new UserEntity();
        entity.setUsername("user1@gmail.com");
        entity.setPassword(new BCryptPasswordEncoder().encode("my12345pwd"));
        userEntityRepository.save(entity);
    }

}
