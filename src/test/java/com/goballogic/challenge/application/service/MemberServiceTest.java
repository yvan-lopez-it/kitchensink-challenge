package com.goballogic.challenge.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.goballogic.challenge.application.port.out.MemberPort;
import com.goballogic.challenge.domain.model.Member;
import com.goballogic.challenge.infrastructure.adapter.in.dto.MemberDTO;
import com.goballogic.challenge.infrastructure.adapter.out.mapper.MemberMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService memberService;

    @Mock
    private MemberPort memberPort;

    @Mock
    private MemberMapper mapper;

    @Test
    void shouldGetAllMembers() {
        Member memberA = Member.builder()
            .id("12345")
            .name("Member A")
            .email("membera@mail.com")
            .phoneNumber("999000999")
            .build();

        Member memberB = Member.builder()
            .id("67890")
            .name("Member B")
            .email("memberb@mail.com")
            .phoneNumber("888888888")
            .build();

        List<Member> memberList = List.of(memberA, memberB);
        List<MemberDTO> memberDTOList = List.of(
            MemberDTO.builder()
                .id("12345")
                .name("Member A")
                .email("membera@mail.com")
                .phoneNumber("999000999")
                .build(),
            MemberDTO.builder()
                .id("67890")
                .name("Member B")
                .email("memberb@mail.com")
                .phoneNumber("888888888")
                .build()
        );

        when(memberPort.getAllMembers()).thenReturn(memberList);
        when(mapper.toDTOList(memberList)).thenReturn(memberDTOList);

        List<MemberDTO> result = memberService.getAllMembers();

        verify(memberPort).getAllMembers();
        verify(mapper).toDTOList(memberList);
        assertThat(result).isEqualTo(memberDTOList);
    }

    @Test
    void shouldRegisterMember() {
        MemberDTO memberDTO = MemberDTO.builder()
            .id("12345")
            .name("Member A")
            .email("membera@mail.com")
            .phoneNumber("999000999")
            .build();

        Member member = Member.builder()
            .id("12345")
            .name("Member A")
            .email("membera@mail.com")
            .phoneNumber("999000999")
            .build();

        Member savedMember = Member.builder()
            .id("67890")
            .name("Member A")
            .email("membera@mail.com")
            .phoneNumber("999000999")
            .build();

        MemberDTO savedMemberDTO = MemberDTO.builder()
            .id("67890")
            .name("Member A")
            .email("membera@mail.com")
            .phoneNumber("999000999")
            .build();

        when(mapper.toDomain(memberDTO)).thenReturn(member);
        when(memberPort.registerMember(member)).thenReturn(savedMember);
        when(mapper.toDTO(savedMember)).thenReturn(savedMemberDTO);

        MemberDTO result = memberService.registerMember(memberDTO);

        verify(mapper).toDomain(memberDTO);
        verify(memberPort).registerMember(member);
        verify(mapper).toDTO(savedMember);

        assertThat(result).isEqualTo(savedMemberDTO);
    }

    @Test
    void shouldGetMemberById() {
        String memberId = "12345";
        Member member = Member.builder()
            .id(memberId)
            .name("Member A")
            .email("membera@mail.com")
            .phoneNumber("999000999")
            .build();

        MemberDTO memberDTO = MemberDTO.builder()
            .id(memberId)
            .name("Member A")
            .email("membera@mail.com")
            .phoneNumber("999000999")
            .build();

        when(memberPort.getMemberById(memberId)).thenReturn(Optional.of(member));
        when(mapper.toDTO(member)).thenReturn(memberDTO);

        Optional<MemberDTO> result = memberService.getMemberById(memberId);

        verify(memberPort).getMemberById(memberId);
        verify(mapper).toDTO(member);
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(memberDTO);
    }

    @Test
    void shouldReturnEmptyWhenMemberNotFoundById() {
        String memberId = "9999";
        when(memberPort.getMemberById(memberId)).thenReturn(Optional.empty());

        Optional<MemberDTO> result = memberService.getMemberById(memberId);

        verify(memberPort).getMemberById(memberId);
        verify(mapper, never()).toDTO(any(Member.class));
        assertThat(result).isEmpty();
    }

}
