package com.goballogic.challenge.infrastructure.adapter.out.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.goballogic.challenge.domain.model.Member;
import com.goballogic.challenge.infrastructure.adapter.in.dto.MemberDTO;
import com.goballogic.challenge.infrastructure.entity.MemberEntity;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

class MemberMapperTest {

    private MemberMapper memberMapper;

    @BeforeEach
    void setUp() {
        // Instanciamos el mapper
        memberMapper = Mappers.getMapper(MemberMapper.class);
    }

    @Test
    void testToDTO() {
        Member member = new Member("1", "Juan Ramirez", "juan.ramirez@mail.com", "9901234516");

        MemberDTO memberDTO = memberMapper.toDTO(member);

        assertEquals(member.getId(), memberDTO.getId());
        assertEquals(member.getName(), memberDTO.getName());
        assertEquals(member.getEmail(), memberDTO.getEmail());
        assertEquals(member.getPhoneNumber(), memberDTO.getPhoneNumber());
    }

    @Test
    void testToDomain() {
        MemberDTO memberDTO = new MemberDTO("1", "Juan Ramirez", "juan.ramirez@mail.com", "9901234516");

        Member member = memberMapper.toDomain(memberDTO);

        assertEquals(memberDTO.getId(), member.getId());
        assertEquals(memberDTO.getName(), member.getName());
        assertEquals(memberDTO.getEmail(), member.getEmail());
        assertEquals(memberDTO.getPhoneNumber(), member.getPhoneNumber());
    }

    @Test
    void testToEntity() {
        Member member = new Member("1", "Juan Ramirez", "juan.ramirez@mail.com", "9901234516");

        MemberEntity memberEntity = memberMapper.toEntity(member);

        assertEquals(member.getId(), memberEntity.getId());
        assertEquals(member.getName(), memberEntity.getName());
        assertEquals(member.getEmail(), memberEntity.getEmail());
        assertEquals(member.getPhoneNumber(), memberEntity.getPhoneNumber());
    }

    @Test
    void testToDomainFromEntity() {
        MemberEntity memberEntity = new MemberEntity("1", "Juan Ramirez", "juan.ramirez@mail.com", "9901234516");

        Member member = memberMapper.toDomain(memberEntity);

        assertEquals(memberEntity.getId(), member.getId());
        assertEquals(memberEntity.getName(), member.getName());
        assertEquals(memberEntity.getEmail(), member.getEmail());
        assertEquals(memberEntity.getPhoneNumber(), member.getPhoneNumber());
    }

    @Test
    void testToDTOList() {
        Member member1 = new Member("1", "Juan Ramirez", "juan.ramirez@mail.com", "9901234516");
        Member member2 = new Member("2", "Carlos Vidal", "carlos.vidal@mail.com", "9881234728");
        List<Member> members = Arrays.asList(member1, member2);

        List<MemberDTO> memberDTOs = memberMapper.toDTOList(members);

        assertEquals(2, memberDTOs.size());
        assertEquals(member1.getId(), memberDTOs.get(0).getId());
        assertEquals(member2.getId(), memberDTOs.get(1).getId());
    }

    @Test
    void testToEntityList() {
        Member member1 = new Member("1", "Juan Ramirez", "juan.ramirez@mail.com", "9901234516");
        Member member2 = new Member("2", "Carlos Vidal", "carlos.vidal@mail.com", "9881234728");
        List<Member> members = Arrays.asList(member1, member2);

        List<MemberEntity> memberEntities = memberMapper.toEntityList(members);

        assertEquals(2, memberEntities.size());
        assertEquals(member1.getId(), memberEntities.get(0).getId());
        assertEquals(member2.getId(), memberEntities.get(1).getId());
    }
}
