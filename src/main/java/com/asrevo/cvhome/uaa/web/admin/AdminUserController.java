package com.asrevo.cvhome.uaa.web.admin;

import com.asrevo.cvhome.uaa.domain.Role;
import com.asrevo.cvhome.uaa.domain.User;
import com.asrevo.cvhome.uaa.dto.CreateUserRequest;
import com.asrevo.cvhome.uaa.dto.UpdateUserRequest;
import com.asrevo.cvhome.uaa.dto.UserDto;
import com.asrevo.cvhome.uaa.service.AdminService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static java.util.stream.Collectors.toSet;

@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminUserController {
    private final AdminService adminService;

    public AdminUserController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PreAuthorize("hasAuthority('SCOPE_super_admin') or hasRole('SUPER_ADMIN')")
    @PostMapping
    public UserDto create(@RequestBody CreateUserRequest req) {
        User u = adminService.createUser(
                req.username(),
                req.email(),
                req.password(),
                Optional.ofNullable(req.roles()).orElse(Set.of())
        );
        return new UserDto(u.getId(), u.getUsername(), u.getEmail(), u.getRoles().stream().map(Role::getName).collect(toSet()));
    }

    @PreAuthorize("hasAuthority('SCOPE_super_admin') or hasRole('SUPER_ADMIN')")
    @PostMapping("/{id}/roles")
    public void assign(@PathVariable UUID id, @RequestBody Set<String> roles) {
        adminService.assignRoles(id, roles);
    }

    @PreAuthorize("hasAuthority('SCOPE_super_admin') or hasRole('SUPER_ADMIN')")
    @PutMapping("/{id}")
    public UserDto update(@PathVariable UUID id, @RequestBody UpdateUserRequest req) {
        User u = adminService.updateUser(id, req.email(), req.password(), req.status());
        return new UserDto(u.getId(), u.getUsername(), u.getEmail(), u.getRoles().stream().map(Role::getName).collect(toSet()));
    }

    @PreAuthorize("hasAuthority('SCOPE_super_admin') or hasRole('SUPER_ADMIN')")
    @PostMapping("/{id}/roles/remove")
    public void removeRoles(@PathVariable UUID id, @RequestBody Set<String> roles) {
        adminService.removeRoles(id, roles);
    }
}
