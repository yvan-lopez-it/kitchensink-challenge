package com.goballogic.challenge.infrastructure.adapter.in.web.controller.rest;

import static org.hamcrest.Matchers.hasSize;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goballogic.challenge.infrastructure.adapter.in.dto.MemberDTO;
import com.goballogic.challenge.infrastructure.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@Testcontainers
class MemberRestControllerTest {

    static MongoDBContainer mongoDBContainer = new MongoDBContainer(DockerImageName.parse("mongo:latest"));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberRepository memberRepository;

    @DynamicPropertySource
    static void configureMongoDB(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUpEach() {
        memberRepository.deleteAll();
    }

    @Test
    void testRegisterMember() throws Exception {
        // Crear un objeto MemberDTO de prueba
        MemberDTO memberDTO = MemberDTO.builder()
            .id("1").name("Juan Ramirez").email("juan.ramirez@mail.com").phoneNumber("9901234516").build();

        mockMvc.perform(post("/rest/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Juan Ramirez"))
            .andExpect(jsonPath("$.email").value("juan.ramirez@mail.com"));
    }

    @Test
    void testGetMemberById() throws Exception {
        // Primero registrar un miembro
        MemberDTO memberDTO = MemberDTO.builder()
            .id("1").name("Juan Ramirez").email("juan.ramirez@mail.com").phoneNumber("9901234516").build();

        String response = mockMvc.perform(post("/rest/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberDTO)))
            .andExpect(status().isCreated())
            .andReturn().getResponse().getContentAsString();

        // Obtener el ID del miembro registrado
        MemberDTO registeredMember = objectMapper.readValue(response, MemberDTO.class);

        // Ahora buscar ese miembro por su ID
        mockMvc.perform(get("/rest/members/{id}", registeredMember.getId())
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Juan Ramirez"))
            .andExpect(jsonPath("$.email").value("juan.ramirez@mail.com"));
    }

    @Test
    void testGetAllMembers() throws Exception {
        // Registrar varios miembros
        MemberDTO memberDTO1 = MemberDTO.builder()
            .id("1").name("Juan Ramirez").email("juan.ramirez@mail.com").phoneNumber("9901234516").build();
        MemberDTO memberDTO2 = MemberDTO.builder()
            .id("2").name("Carlos Vidal").email("carlos.vidal@mail.com").phoneNumber("9881234516").build();

        mockMvc.perform(post("/rest/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberDTO1)))
            .andExpect(status().isCreated());

        mockMvc.perform(post("/rest/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberDTO2)))
            .andExpect(status().isCreated());

        // Obtener todos los miembros
        mockMvc.perform(get("/rest/members")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].name").value("Juan Ramirez"))
            .andExpect(jsonPath("$[1].name").value("Carlos Vidal"));
    }

}
