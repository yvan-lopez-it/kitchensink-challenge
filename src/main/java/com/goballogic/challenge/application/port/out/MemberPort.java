package com.goballogic.challenge.application.port.out;

import com.goballogic.challenge.domain.model.Member;
import java.util.List;
import java.util.Optional;

public interface MemberPort {

    List<Member> getAllMembers();

    List<Member> findAllOrderedByName();

    Member registerMember(Member member);

    Optional<Member> getMemberById(String id);

    Optional<Member> findByEmail(String email);

}
