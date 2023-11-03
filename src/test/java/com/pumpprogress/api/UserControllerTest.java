package com.pumpprogress.api;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pumpprogress.api.Model.User;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;

@TestInstance(Lifecycle.PER_CLASS)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    private static final String BASE_URL = "/api/users";
    private Integer user_id = 0;
    private static final String USER_EMAIL = "johndoe@gmail.com";
    private static final String USER = "{\"name\":\"John Doe\",\"email\":\"johndoe@gmail.com\",\"password\":\"password\"}";
    private static final String USER_TEST = "{\"name\":\"Test test\",\"email\":\"test@gmail.com\",\"password\":\"password\"}";

    @BeforeAll
    public void setup() throws Exception {
        MvcResult result = mockMvc.perform(post(BASE_URL + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER))
                .andExpect(status().isCreated())
                .andReturn();

        ObjectMapper objectMapper = new ObjectMapper();
        User createdUser = objectMapper.readValue(result.getResponse().getContentAsString(), User.class);

        user_id = createdUser.getId();
    }

    @Test
    public void testAddUser() throws Exception {

        mockMvc.perform(post(BASE_URL + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER_TEST))
                .andExpect(status().isCreated())
                .andReturn();
    }

    @Test
    public void testGetUserById() throws Exception {

        mockMvc.perform(get(BASE_URL + "/id/" + user_id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(user_id)));
    }

    @Test
    public void testGetUserByEmail() throws Exception {

        mockMvc.perform(get(BASE_URL + "/email/" + USER_EMAIL))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.email", is(USER_EMAIL)));
    }

    @Test
    public void testGetUserByIdBadRequest() throws Exception {

        mockMvc.perform(get(BASE_URL + "/id/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetUserByIdNotFound() throws Exception {

        mockMvc.perform(get(BASE_URL + "/id/0"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testGetUserByEmailNotFound() throws Exception {

        mockMvc.perform(get(BASE_URL + "/email/abc"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testAddUserBadRequest() throws Exception {
        String json = "{\"name\":\"John Doe\",\"email\":\"johndoe@gmail.com\", \"password\":\"\"}";

        mockMvc.perform(post(BASE_URL + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testAddUserConflict() throws Exception {

        mockMvc.perform(post(BASE_URL + "/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content(USER))
                .andExpect(status().isConflict());
    }

    @Test
    public void testDeleteUserBadRequest() throws Exception {

        mockMvc.perform(delete(BASE_URL + "/delete/abc"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteUserNotFound() throws Exception {

        mockMvc.perform(delete(BASE_URL + "/delete/0"))
                .andExpect(status().isNotFound());
    }

    @AfterAll
    public void testDeleteUser() throws Exception {
        System.err.println("user_id delete method: " + user_id);
        mockMvc.perform(delete(BASE_URL + "/delete/" + user_id))
                .andExpect(status().isOk());
    }
}
