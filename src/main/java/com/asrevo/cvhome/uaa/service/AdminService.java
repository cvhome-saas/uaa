package com.asrevo.cvhome.uaa.service;

import com.asrevo.cvhome.uaa.domain.Role;
import com.asrevo.cvhome.uaa.domain.User;
import com.asrevo.cvhome.uaa.repo.RoleRepository;
import com.asrevo.cvhome.uaa.repo.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class AdminService {
    private final UserRepository users;
    private final RoleRepository roles;
    private final PasswordEncoder encoder;

    public AdminService(UserRepository users, RoleRepository roles, PasswordEncoder encoder) {
        this.users = users;
        this.roles = roles;
        this.encoder = encoder;
    }

    public User createUser(String username, String email, String rawPassword, Set<String> roleNames) {
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(rawPassword));
        Set<Role> rs = new HashSet<>();
        for (String rn : Optional.ofNullable(roleNames).orElse(Set.of())) {
            Role r = roles.findByName(rn).orElseGet(() -> roles.save(new Role(rn)));
            rs.add(r);
        }
        u.getRoles().addAll(rs);
        return users.save(u);
    }

    public void assignRoles(UUID userId, Set<String> roleNames) {
        User u = users.findById(userId).orElseThrow();
        Set<Role> rs = new HashSet<>();
        for (String rn : roleNames) {
            rs.add(roles.findByName(rn).orElseGet(() -> roles.save(new Role(rn))));
        }
        u.getRoles().clear();
        u.getRoles().addAll(rs);
    }

    public User updateUser(UUID userId, String email, String rawPassword, String status) {
        User u = users.findById(userId).orElseThrow();
        if (email != null) u.setEmail(email);
        if (rawPassword != null && !rawPassword.isBlank()) {
            u.setPasswordHash(encoder.encode(rawPassword));
        }
        if (status != null && !status.isBlank()) u.setStatus(status);
        return u; // JPA dirty checking will persist
    }

    public void removeRoles(UUID userId, Set<String> roleNames) {
        if (roleNames == null || roleNames.isEmpty()) return;
        User u = users.findById(userId).orElseThrow();
        u.getRoles().removeIf(r -> roleNames.contains(r.getName()));
    }
}
