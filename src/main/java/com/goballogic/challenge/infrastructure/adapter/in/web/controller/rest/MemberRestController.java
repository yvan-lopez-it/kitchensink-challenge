package com.goballogic.challenge.infrastructure.adapter.in.web.controller.rest;

import com.goballogic.challenge.application.port.in.MemberUseCase;
import com.goballogic.challenge.infrastructure.adapter.in.dto.MemberDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kitchensink")
public class MemberRestController {

    private final MemberUseCase memberUseCase;

    public MemberRestController(MemberUseCase memberUseCase) {
        this.memberUseCase = memberUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<MemberDTO> registerMember(@RequestBody MemberDTO memberDTO) {
        MemberDTO savedMember = memberUseCase.registerMember(memberDTO);
        return ResponseEntity.ok(savedMember);
    }
}
