package com.goballogic.challenge.infrastructure.adapter.in.web.controller.view;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.goballogic.challenge.application.port.in.MemberUseCase;
import com.goballogic.challenge.infrastructure.adapter.in.dto.MemberDTO;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class) // We load only the controller context
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean // We mock MemberUseCase so it is injected into the controller
    private MemberUseCase memberUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Start mocks if necessary
    }

    @Test
    void testShowMembers() throws Exception {

        MemberDTO memberDTO1 = MemberDTO.builder()
            .id("1").name("Juan Ramirez").email("juan.ramirez@mail.com").phoneNumber("9901234516").build();
        MemberDTO memberDTO2 = MemberDTO.builder()
            .id("2").name("Carlos Vidal").email("carlos.vidal@mail.com").phoneNumber("9881234516").build();

        when(memberUseCase.findAllOrderedByName())
            .thenReturn(Arrays.asList(memberDTO1, memberDTO2));

        mockMvc.perform(get("/"))
            .andExpect(status().isOk())
            .andExpect(view().name("index"))
            .andExpect(model().attribute("members", hasSize(2)))
            .andExpect(model().attribute("members", Arrays.asList(memberDTO1, memberDTO2)));
    }
}
