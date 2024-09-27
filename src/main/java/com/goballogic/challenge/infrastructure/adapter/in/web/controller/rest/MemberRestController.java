package com.goballogic.challenge.infrastructure.adapter.in.web.controller.rest;

import com.goballogic.challenge.application.port.in.MemberUseCase;
import com.goballogic.challenge.infrastructure.adapter.in.dto.MemberDTO;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/kitchensink/rest")
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

    @GetMapping("/members/{id}")
    public ResponseEntity<MemberDTO> getMemberById(@PathVariable String id) {
        MemberDTO member = memberUseCase.getMemberById(id).orElse(null);
        if (member == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(member);
    }

    @GetMapping("/members")
    public ResponseEntity<List<MemberDTO>> getAllMembers() {
        List<MemberDTO> members = memberUseCase.getAllMembers();
        return ResponseEntity.ok(members);
    }
}
