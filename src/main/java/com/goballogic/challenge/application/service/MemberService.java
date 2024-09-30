package com.goballogic.challenge.application.service;

import com.goballogic.challenge.application.exceptions.DuplicateEmailException;
import com.goballogic.challenge.application.exceptions.ResourceNotFoundException;
import com.goballogic.challenge.application.port.in.MemberUseCase;
import com.goballogic.challenge.application.port.out.MemberPort;
import com.goballogic.challenge.domain.model.Member;

import com.goballogic.challenge.infrastructure.adapter.in.dto.MemberDTO;
import com.goballogic.challenge.infrastructure.adapter.out.mapper.MemberMapper;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService implements MemberUseCase {

    private static final Logger LOGGER = LoggerFactory.getLogger(MemberService.class);

    private final MemberPort memberPort;
    private final MemberMapper mapper;

    public MemberService(MemberPort memberPort, MemberMapper mapper) {
        this.memberPort = memberPort;
        this.mapper = mapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MemberDTO> getAllMembers() {
        LOGGER.info("Fetching all members");
        List<Member> memberList = memberPort.getAllMembers();
        LOGGER.debug("Retrieved {} members", memberList.size());
        return mapper.toDTOList(memberList);
    }

    @Override
    public List<MemberDTO> findAllOrderedByName() {
        LOGGER.info("Fetching members ordered by name");
        List<Member> memberList = memberPort.findAllOrderedByName();
        LOGGER.debug("Retrieved {} members ordered by name", memberList.size());
        return mapper.toDTOList(memberList);
    }

    @Override
    @Transactional
    public MemberDTO registerMember(MemberDTO memberDTO) throws DuplicateEmailException {
        LOGGER.info("Registering new member with email: {}", memberDTO.getEmail());
        Member member = mapper.toDomain(memberDTO);
        Optional<Member> memberOptional = memberPort.findByEmail(member.getEmail());
        if (memberOptional.isPresent()) {
            LOGGER.error("Email already registered: {}", member.getEmail());
            throw new DuplicateEmailException("Error. The email is already registered.");
        }

        Member savedMember = memberPort.registerMember(member);
        LOGGER.info("Successfully registered member with email: {}", savedMember.getEmail());
        return mapper.toDTO(savedMember);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDTO getMemberById(String id) {
        LOGGER.info("Fetching member by ID: {}", id);
        Member member = memberPort.getMemberById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Member with id: " + id + " not found"));

        getDebug(member.getId());

        return mapper.toDTO(member);
    }

    @Override
    @Transactional(readOnly = true)
    public MemberDTO findByEmail(String email) {
        LOGGER.info("Fetching member by email: {}", email);
        Member member = memberPort.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Member with email: [" + email + "] not found"));

        getDebug(member.getEmail());

        return mapper.toDTO(member);
    }

    private static void getDebug(String member) {
        LOGGER.debug("Member found: {}", member);
    }

}
