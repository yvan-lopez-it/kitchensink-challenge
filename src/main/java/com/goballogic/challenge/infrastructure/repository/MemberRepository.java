package com.goballogic.challenge.infrastructure.repository;

import com.goballogic.challenge.infrastructure.entity.MemberEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MemberRepository extends MongoRepository<MemberEntity, String> {

}
