package com.goballogic.challenge.infrastructure.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goballogic.challenge.domain.model.Member;
import com.goballogic.challenge.infrastructure.adapter.out.mapper.MemberMapper;
import com.goballogic.challenge.infrastructure.repository.MemberRepository;
import java.io.InputStream;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

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
        memberRepository.deleteAll();

        InputStream inputStream = new ClassPathResource("members.json").getInputStream();
        List<Member> members = objectMapper.readValue(inputStream, new TypeReference<>() {});
        memberRepository.saveAll(memberMapper.toEntityList(members));

        logger.info("Test data loaded successfully.");
    }
}
