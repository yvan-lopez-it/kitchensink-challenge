package com.goballogic.challenge.infrastructure.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goballogic.challenge.domain.model.Member;
import com.goballogic.challenge.infrastructure.adapter.out.mapper.MemberMapper;
import com.goballogic.challenge.infrastructure.repository.MemberRepository;
import java.io.InputStream;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final MemberMapper memberMapper;
    private final ObjectMapper objectMapper;

    public DataLoader(MemberRepository memberRepository, MemberMapper memberMapper, ObjectMapper objectMapper) {
        this.memberRepository = memberRepository;
        this.memberMapper = memberMapper;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        // Limpiar la colección
        memberRepository.deleteAll();

        // Leer archivo JSON desde src/main/resources
        InputStream inputStream = new ClassPathResource("members.json").getInputStream();

        // Convertir el JSON en una lista de objetos Member
        List<Member> members = objectMapper.readValue(inputStream, new TypeReference<>() {});

        // Guardar en la base de datos MongoDB
        memberRepository.saveAll(memberMapper.toEntityList(members));

        System.out.println("Datos de prueba cargados correctamente.");
    }
}
