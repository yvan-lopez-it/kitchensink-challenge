package com.goballogic.challenge.infrastructure.adapter.out.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.goballogic.challenge.domain.model.Member;
import com.goballogic.challenge.infrastructure.adapter.out.mapper.MemberMapper;
import com.goballogic.challenge.infrastructure.entity.MemberEntity;
import com.goballogic.challenge.infrastructure.repository.MemberRepository;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;

@ExtendWith(MockitoExtension.class)
class MemberPersistenceAdapterTest {

    static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Mock
    private MemberRepository repository;

    @Mock
    private MemberMapper mapper;

    @InjectMocks
    private MemberPersistenceAdapter memberPersistenceAdapter;

    @DynamicPropertySource
    static void configureMongoDB(DynamicPropertyRegistry registry) {
        mongoDBContainer.start();
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }

    @Test
    void testGetAllMembers() {
        // Test data
        MemberEntity memberEntity1 = new MemberEntity("1", "Juan Ramirez", "juan.ramirez@mail.com", "9901234516");
        MemberEntity memberEntity2 = new MemberEntity("2", "Carlos Vidal", "carlos.vidal@mail.com", "9881234728");
        List<MemberEntity> memberEntityList = Arrays.asList(memberEntity1, memberEntity2);

        Member member1 = new Member("1", "Juan Ramirez", "juan.ramirez@mail.com", "9901234516");
        Member member2 = new Member("2", "Carlos Vidal", "carlos.vidal@mail.com", "9881234728");
        List<Member> members = Arrays.asList(member1, member2);

        // Setting up mocks
        when(repository.findAll()).thenReturn(memberEntityList);
        when(mapper.toDomainList(memberEntityList)).thenReturn(members);

        // Execute
        List<Member> result = memberPersistenceAdapter.getAllMembers();

        // Verification
        assertEquals(2, result.size());
        assertEquals("Juan Ramirez", result.get(0).getName());
        assertEquals("Carlos Vidal", result.get(1).getName());
        verify(repository).findAll();
        verify(mapper).toDomainList(memberEntityList);
    }

    @Test
    void testFindAllOrderedByName() {

        MemberEntity memberEntity1 = new MemberEntity("1", "Carlos Vidal", "carlos.vidal@mail.com", "9881234728");
        MemberEntity memberEntity2 = new MemberEntity("2", "Juan Ramirez", "juan.ramirez@mail.com", "9901234516");
        List<MemberEntity> memberEntityList = Arrays.asList(memberEntity1, memberEntity2);

        Member member1 = new Member("1", "Carlos Vidal", "carlos.vidal@mail.com", "9881234728");
        Member member2 = new Member("2", "Juan Ramirez", "juan.ramirez@mail.com", "9901234516");
        List<Member> members = Arrays.asList(member1, member2);

        when(repository.findAllByOrderByNameAsc()).thenReturn(memberEntityList);
        when(mapper.toDomainList(memberEntityList)).thenReturn(members);

        List<Member> result = memberPersistenceAdapter.findAllOrderedByName();

        assertEquals(2, result.size());
        assertEquals("Carlos Vidal", result.get(0).getName());
        assertEquals("Juan Ramirez", result.get(1).getName());
        verify(repository).findAllByOrderByNameAsc();
        verify(mapper).toDomainList(memberEntityList);
    }

    @Test
    void testRegisterMember() {
        Member member = new Member("1", "Juan Ramirez", "juan.ramirez@mail.com", "9901234516");
        MemberEntity memberEntity = new MemberEntity("1", "Juan Ramirez", "juan.ramirez@mail.com", "9901234516");
        MemberEntity savedEntity = new MemberEntity("1", "Juan Ramirez", "juan.ramirez@mail.com", "9901234516");

        when(mapper.toEntity(member)).thenReturn(memberEntity);
        when(repository.save(memberEntity)).thenReturn(savedEntity);
        when(mapper.toDomain(savedEntity)).thenReturn(member);

        Member result = memberPersistenceAdapter.registerMember(member);

        assertEquals("Juan Ramirez", result.getName());
        verify(mapper).toEntity(member);
        verify(repository).save(memberEntity);
        verify(mapper).toDomain(savedEntity);
    }

    @Test
    void testGetMemberById() {

        String id = "1";
        MemberEntity memberEntity = new MemberEntity(id, "Juan Ramirez", "juan.ramirez@mail.com", "9901234516");
        Member member = new Member(id, "Juan Ramirez", "juan.ramirez@mail.com", "9901234516");

        when(repository.findById(id)).thenReturn(Optional.of(memberEntity));
        when(mapper.toDomain(memberEntity)).thenReturn(member);

        Optional<Member> result = memberPersistenceAdapter.getMemberById(id);

        assertTrue(result.isPresent());
        assertEquals("Juan Ramirez", result.get().getName());
        verify(repository).findById(id);
        verify(mapper).toDomain(memberEntity);
    }

    @Test
    void testFindByEmail() {

        String email = "juan.ramirez@mail.com";
        MemberEntity memberEntity = new MemberEntity("1", "Juan Ramirez", email, "9901234516");
        Member member = new Member("1", "Juan Ramirez", email, "9901234516");

        when(repository.findMemberEntityByEmail(email)).thenReturn(Optional.of(memberEntity));
        when(mapper.toDomain(memberEntity)).thenReturn(member);

        Optional<Member> result = memberPersistenceAdapter.findByEmail(email);

        assertTrue(result.isPresent());
        assertEquals("Juan Ramirez", result.get().getName());
        verify(repository).findMemberEntityByEmail(email);
        verify(mapper).toDomain(memberEntity);
    }
}
