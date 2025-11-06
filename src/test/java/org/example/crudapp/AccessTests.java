package org.example.crudapp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class AccessTests {

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }


    @Test
    @WithAnonymousUser
    void whenAnonymousAccessAdminHello_then401() throws Exception {
        mockMvc.perform(get("/api/v1/items/hello/admin"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void whenAdminAccessAdminHello_then200() throws Exception {
        mockMvc.perform(get("/api/v1/items/hello/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void whenUserAccessAdminHello_then403() throws Exception {
        mockMvc.perform(get("/api/v1/items/hello/admin"))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithAnonymousUser
    void whenAnonymousGetItems_then401() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void whenUserGetItems_then200() throws Exception {
        mockMvc.perform(get("/items"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void whenUserPostItem_then200() throws Exception {
        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content("{\"name\":\"Test Item\"}"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void whenAnonymousPostItem_then401() throws Exception {
        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content("{\"name\":\"Test Item\"}"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"USER"})
    void whenUserDeleteItem_then403() throws Exception {
        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void whenAdminDeleteItem_then200() throws Exception {
        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isOk());
    }

    @Test
    @WithAnonymousUser
    void whenAnonymousDeleteItem_then401() throws Exception {
        mockMvc.perform(delete("/items/1"))
                .andExpect(status().isUnauthorized());
    }
}