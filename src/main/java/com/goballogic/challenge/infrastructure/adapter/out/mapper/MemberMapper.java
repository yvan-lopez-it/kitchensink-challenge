package com.goballogic.challenge.infrastructure.adapter.out.mapper;

import com.goballogic.challenge.domain.model.Member;
import com.goballogic.challenge.infrastructure.adapter.in.dto.MemberDTO;
import com.goballogic.challenge.infrastructure.entity.MemberEntity;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    MemberDTO toDTO(Member member);
    Member toDomain(MemberDTO memberDTO);

    MemberEntity toEntity(Member member);
    Member toDomain(MemberEntity entity);

    List<MemberDTO> toDTOList(List<Member> members);
    List<Member> toDomainList(List<MemberEntity> entities);
    List<MemberEntity> toEntityList(List<Member> members);

}
