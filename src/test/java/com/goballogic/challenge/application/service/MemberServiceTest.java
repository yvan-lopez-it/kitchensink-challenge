package com.goballogic.challenge.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.goballogic.challenge.application.exceptions.DuplicateEmailException;
import com.goballogic.challenge.application.exceptions.ResourceNotFoundException;
import com.goballogic.challenge.application.port.out.MemberPort;
import com.goballogic.challenge.domain.model.Member;
import com.goballogic.challenge.infrastructure.adapter.in.dto.MemberDTO;
import com.goballogic.challenge.infrastructure.adapter.out.mapper.MemberMapper;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @Mock
    private MemberPort memberPort;

    @Mock
    private MemberMapper mapper;

    @InjectMocks
    private MemberService memberService;

    private Member member;
    private MemberDTO memberDTO;

    @BeforeEach
    void setUp() {
        member = new Member("1", "Miguel Grau", "miguel.grau@example.com", "1234567890");
        memberDTO = MemberDTO.builder()
            .id("1")
            .name("Miguel Grau")
            .email("miguel.grau@example.com")
            .phoneNumber("1234567890")
            .build();
    }

    @Test
    void testGetAllMembers() {
        when(memberPort.getAllMembers()).thenReturn(Collections.singletonList(member));
        when(mapper.toDTOList(any())).thenReturn(Collections.singletonList(memberDTO));

        List<MemberDTO> result = memberService.getAllMembers();

        assertEquals(1, result.size());
        assertEquals("Miguel Grau", result.getFirst().getName());
        verify(memberPort).getAllMembers();
        verify(mapper).toDTOList(any());
    }

    @Test
    void testRegisterMember() throws DuplicateEmailException {
        when(mapper.toDomain(memberDTO)).thenReturn(member);
        when(memberPort.findByEmail(member.getEmail())).thenReturn(Optional.empty());
        when(memberPort.registerMember(member)).thenReturn(member);
        when(mapper.toDTO(member)).thenReturn(memberDTO);

        MemberDTO result = memberService.registerMember(memberDTO);

        assertNotNull(result);
        assertEquals("miguel.grau@example.com", result.getEmail());
        verify(memberPort).findByEmail(member.getEmail());
        verify(memberPort).registerMember(member);
    }

    @Test
    void testRegisterMemberDuplicateEmail() {
        when(mapper.toDomain(memberDTO)).thenReturn(member);
        when(memberPort.findByEmail(member.getEmail())).thenReturn(Optional.of(member));

        Exception exception = assertThrows(DuplicateEmailException.class, () -> {
            memberService.registerMember(memberDTO);
        });

        assertEquals("Error. The email is already registered.", exception.getMessage());
        verify(memberPort).findByEmail(member.getEmail());
        verify(memberPort, never()).registerMember(any());
    }

    @Test
    void testGetMemberById() {
        when(memberPort.getMemberById("1")).thenReturn(Optional.of(member));
        when(mapper.toDTO(member)).thenReturn(memberDTO);

        MemberDTO result = memberService.getMemberById("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(memberPort).getMemberById("1");
    }

    @Test
    void testGetMemberByIdNotFound() {
        when(memberPort.getMemberById("1")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            memberService.getMemberById("1");
        });

        assertEquals("Member with id: 1 not found", exception.getMessage());
    }

    @Test
    void testFindByEmail() {
        when(memberPort.findByEmail("miguel.grau@example.com")).thenReturn(Optional.of(member));
        when(mapper.toDTO(member)).thenReturn(memberDTO);

        MemberDTO result = memberService.findByEmail("miguel.grau@example.com");

        assertNotNull(result);
        assertEquals("miguel.grau@example.com", result.getEmail());
        verify(memberPort).findByEmail("miguel.grau@example.com");
    }

    @Test
    void testFindByEmailNotFound() {
        when(memberPort.findByEmail("miguel.grau@example.com")).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            memberService.findByEmail("miguel.grau@example.com");
        });

        assertEquals("Member with email: [miguel.grau@example.com] not found", exception.getMessage());
    }

}
