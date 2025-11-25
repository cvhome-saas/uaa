package com.asrevo.cvhome.uaa.web.admin;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class AdminController {
    @GetMapping("me")
    public Object me() {
        return SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
