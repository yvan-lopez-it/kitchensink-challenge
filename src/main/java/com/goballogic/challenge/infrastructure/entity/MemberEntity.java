package com.goballogic.challenge.infrastructure.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "members")
@TypeAlias("MemberEntity")
public class MemberEntity {

    @Id
    private String id;
    private String name;
    private String email;
    private String phoneNumber;

}
