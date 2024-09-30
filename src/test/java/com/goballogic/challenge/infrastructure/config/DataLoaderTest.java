package com.goballogic.challenge.infrastructure.config;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.goballogic.challenge.domain.model.Member;
import com.goballogic.challenge.infrastructure.adapter.out.mapper.MemberMapper;
import com.goballogic.challenge.infrastructure.entity.MemberEntity;
import com.goballogic.challenge.infrastructure.repository.MemberRepository;
import java.io.InputStream;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

@ExtendWith(MockitoExtension.class)
class DataLoaderTest {

    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private MemberMapper memberMapper;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private DataLoader dataLoader;

    @DynamicPropertySource
    static void configureMongoDB(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void testRunLoadsData() throws Exception {
        // Arrange
        // Create a list of Member objects from your JSON content
        Member member1 = new Member("1", "Juan Ramirez", "juan.ramirez@mail.com", "9901234516");
        Member member2 = new Member("2", "Carlos Vidal", "carlos.vidal@mail.com", "9881234728");
        List<Member> members = List.of(member1, member2); // Create the list

        // Mock the ObjectMapper to return the list of members when reading the test JSON
        when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class)))
            .thenReturn(members); // Use any for InputStream and TypeReference

        // Mock the memberMapper to return a corresponding list of MemberEntity
        List<MemberEntity> memberEntities = List.of(
            new MemberEntity("1", "Juan Ramirez", "juan.ramirez@mail.com", "9901234516"),
            new MemberEntity("2", "Carlos Vidal", "carlos.vidal@mail.com", "9881234728")
        );
        when(memberMapper.toEntityList(members)).thenReturn(memberEntities);

        // Act
        dataLoader.run();

        // Assert
        verify(memberRepository).deleteAll();  // Verify that all records are deleted
        verify(memberRepository).saveAll(memberEntities);  // Verify that new records are saved
    }

    @Test
    void testRunHandlesException() throws Exception {
        // Arrange
        when(objectMapper.readValue(any(InputStream.class), any(TypeReference.class)))
            .thenThrow(new RuntimeException("Failed to read JSON"));

        // Act & Assert
        assertThatThrownBy(() -> dataLoader.run())
            .isInstanceOf(RuntimeException.class)
            .hasMessage("Failed to read JSON");

        verify(memberRepository).deleteAll();  // Verifica que se eliminan los registros previos
        verify(memberRepository, never()).saveAll(anyList());  // Verifica que no se guardan registros si ocurre un error
    }

}
