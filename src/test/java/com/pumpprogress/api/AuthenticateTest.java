package com.pumpprogress.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pumpprogress.api.Model.User;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticateTest {
    @Autowired
    private MockMvc mockMvc;
    private static final String BASE_URL = "/api/authenticate";
    private static final int USER_ID = 33;
    private static final String CREDENTIAL = "{\"email\":\"admin@gmail.com\", \"password\":\"admin\"}";

    @Test
    public void testAuthenticate() throws Exception {
        MvcResult result = mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(CREDENTIAL))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();
        User userGranted = mapper.readValue(result.getResponse().getContentAsString(), User.class);
        assertEquals(USER_ID, userGranted.getId());
    }

    @Test
    public void testAuthenticateFail() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"bidon@bidon.fr\", \"password\":\"bidon\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAuthenticateMissingEmailField() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"\", \"password\":\"bidon\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAuthenticateMissingPasswordField() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"bidon@bidon.fr\", \"password\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAuthenticateWrongCredentialObj() throws Exception {
        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
}
