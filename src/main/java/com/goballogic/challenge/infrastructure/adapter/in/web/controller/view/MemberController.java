package com.goballogic.challenge.infrastructure.adapter.in.web.controller.view;

import com.goballogic.challenge.application.port.in.MemberUseCase;
import com.goballogic.challenge.infrastructure.adapter.in.dto.MemberDTO;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/kitchensink")
public class MemberController {

    private final MemberUseCase memberUseCase;

    public MemberController(MemberUseCase memberUseCase) {
        this.memberUseCase = memberUseCase;
    }

    @GetMapping({"", "/"})
    public String showMembers(Model model) {
        List<MemberDTO> members = memberUseCase.getAllMembers();
        model.addAttribute("members", members);
        return "index";
    }

}
