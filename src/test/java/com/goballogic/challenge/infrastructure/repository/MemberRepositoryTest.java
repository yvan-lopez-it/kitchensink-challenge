package com.goballogic.challenge.infrastructure.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.goballogic.challenge.infrastructure.entity.MemberEntity;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

@DataMongoTest
class MemberRepositoryTest {

    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    private MemberRepository memberRepository;

    // Configura Testcontainers para que use el contenedor de MongoDB
    @DynamicPropertySource
    static void configureMongoDB(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @BeforeEach
    void setUp() {
        memberRepository.deleteAll();
    }

    @Test
    void testFindAllByOrderByNameAsc() {
        // Arrange
        MemberEntity member1 = new MemberEntity("51", "Alice", "alice@example.com", "1234567890");
        MemberEntity member2 = new MemberEntity("52", "Bob", "bob@example.com", "0987654321");
        memberRepository.save(member1);
        memberRepository.save(member2);

        // Act
        List<MemberEntity> members = memberRepository.findAllByOrderByNameAsc();

        // Assert
        assertThat(members).hasSize(2);
        assertThat(members.get(0).getName()).isEqualTo("Alice");
        assertThat(members.get(1).getName()).isEqualTo("Bob");
    }

    @Test
    void testFindMemberEntityByEmail() {
        // Arrange
        MemberEntity member = new MemberEntity("51", "Alice", "alice@example.com", "1234567890");
        memberRepository.save(member);

        // Act
        Optional<MemberEntity> foundMember = memberRepository.findMemberEntityByEmail("alice@example.com");

        // Assert
        assertThat(foundMember).isPresent();
        assertThat(foundMember.get().getEmail()).isEqualTo("alice@example.com");
    }

    @Test
    void testFindMemberEntityByEmail_NotFound() {
        // Act
        Optional<MemberEntity> foundMember = memberRepository.findMemberEntityByEmail("nonexistent@example.com");

        // Assert
        assertThat(foundMember).isNotPresent();
    }

}
