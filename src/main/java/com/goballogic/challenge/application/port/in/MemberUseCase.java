package com.goballogic.challenge.application.port.in;


import com.goballogic.challenge.application.exceptions.DuplicateEmailException;
import com.goballogic.challenge.infrastructure.adapter.in.dto.MemberDTO;
import java.util.List;
import java.util.Optional;

public interface MemberUseCase {

    List<MemberDTO> getAllMembers();

    List<MemberDTO> findAllOrderedByName() throws DuplicateEmailException;

    MemberDTO registerMember(MemberDTO memberDTO);

    Optional<MemberDTO> getMemberById(String id);

    Optional<MemberDTO> findByEmail(String email);

}
