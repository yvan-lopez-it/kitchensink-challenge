package com.goballogic.challenge.infrastructure.adapter.in.web.controller.view;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class ThymeleafTemplateTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testMemberTemplate() throws Exception {
        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(content().string(containsString("Register Member")))
            .andExpect(content().string(containsString("Name")))
            .andExpect(content().string(containsString("Email")))
            .andExpect(content().string(containsString("Phone #")));
    }

    @Test
    void testInvalidMemberForm() throws Exception {
        mockMvc.perform(post("/rest/register")
                .param("name", "Juan123")
                .param("email", "invalid-email")
                .param("phoneNumber", "12345"))
            .andExpect(status().isBadRequest());
    }

}
