package com.asrevo.cvhome.uaa.web.admin;

import com.asrevo.cvhome.uaa.domain.Role;
import com.asrevo.cvhome.uaa.repo.RoleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/roles")
public class AdminRoleController {
    private final RoleRepository repo;

    public AdminRoleController(RoleRepository repo) {
        this.repo = repo;
    }

    @PreAuthorize("hasAuthority('SCOPE_super_admin') or hasRole('SUPER_ADMIN')")
    @PostMapping
    public Role create(@RequestParam String name){
        return repo.save(new Role(name));
    }
}
