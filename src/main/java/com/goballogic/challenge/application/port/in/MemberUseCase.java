package com.goballogic.challenge.application.port.in;


import com.goballogic.challenge.application.exceptions.DuplicateEmailException;
import com.goballogic.challenge.infrastructure.adapter.in.dto.MemberDTO;
import java.util.List;

public interface MemberUseCase {

    List<MemberDTO> getAllMembers();

    List<MemberDTO> findAllOrderedByName() throws DuplicateEmailException;

    MemberDTO registerMember(MemberDTO memberDTO);

    MemberDTO getMemberById(String id);

    MemberDTO findByEmail(String email);

}
