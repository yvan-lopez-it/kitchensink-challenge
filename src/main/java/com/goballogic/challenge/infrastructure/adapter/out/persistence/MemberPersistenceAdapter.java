package com.goballogic.challenge.infrastructure.adapter.out.persistence;

import com.goballogic.challenge.application.port.out.MemberPort;
import com.goballogic.challenge.domain.model.Member;
import com.goballogic.challenge.infrastructure.adapter.out.mapper.MemberMapper;
import com.goballogic.challenge.infrastructure.entity.MemberEntity;
import com.goballogic.challenge.infrastructure.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class MemberPersistenceAdapter implements MemberPort {

    private final MemberRepository repository;
    private final MemberMapper mapper;

    public MemberPersistenceAdapter(MemberRepository repository, MemberMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Member> getAllMembers() {
        List<MemberEntity> memberEntityList = repository.findAll();
        return mapper.toDomainList(memberEntityList);
    }

    @Override
    public Member registerMember(Member member) {
        MemberEntity memberEntitySaved = repository.save(mapper.toEntity(member));
        return mapper.toDomain(memberEntitySaved);
    }

    @Override
    public Optional<Member> getMemberById(String id) {
        Optional<MemberEntity> memberEntityOptional = repository.findById(id);
        return memberEntityOptional.map(mapper::toDomain);
    }
}
