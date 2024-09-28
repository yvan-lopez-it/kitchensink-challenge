package com.goballogic.challenge.infrastructure.repository;

import com.goballogic.challenge.infrastructure.entity.MemberEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<MemberEntity, String> {

    List<MemberEntity> findAllByOrderByNameAsc();

    Optional<MemberEntity> findMemberEntityByEmail(String email);

}
