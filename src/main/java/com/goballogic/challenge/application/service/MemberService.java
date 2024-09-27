package com.goballogic.challenge.application.service;

import com.goballogic.challenge.application.port.in.MemberUseCase;
import com.goballogic.challenge.application.port.out.MemberPort;
import com.goballogic.challenge.domain.model.Member;

import com.goballogic.challenge.infrastructure.adapter.in.dto.MemberDTO;
import com.goballogic.challenge.infrastructure.adapter.out.mapper.MemberMapper;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService implements MemberUseCase {

    private final MemberPort memberPort;
    private final MemberMapper mapper;

    public MemberService(MemberPort memberPort, MemberMapper mapper) {
        this.memberPort = memberPort;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberDTO> getAllMembers() {
        List<Member> memberList = memberPort.getAllMembers();
        return mapper.toDTOList(memberList);
    }

    @Override
    @Transactional
    public MemberDTO registerMember(MemberDTO memberDTO) {
        Member member = mapper.toDomain(memberDTO);
        Member savedMember = memberPort.registerMember(member);
        return mapper.toDTO(savedMember);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<MemberDTO> getMemberById(String id) {
        Optional<Member> memberOptional = memberPort.getMemberById(id);
        return memberOptional.map(mapper::toDTO);
    }
}
