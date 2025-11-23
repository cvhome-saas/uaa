package com.asrevo.cvhome.uaa.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientRoleService {

    private final JdbcTemplate jdbc;

    public ClientRoleService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Set<String> getClientRoleNames(String clientId) {
        List<String> roles = jdbc.query(
            "select r.name from client_roles cr join roles r on r.id = cr.role_id where cr.client_id = ?",
            (rs, rowNum) -> rs.getString(1), clientId
        );
        return roles.stream().collect(Collectors.toSet());
    }

    public Set<String> getClientPermissionNames(String clientId) {
        List<String> perms = jdbc.query(
            "select distinct p.name from client_roles cr "+
                "join role_permissions rp on rp.role_id = cr.role_id "+
                "join permissions p on p.id = rp.permission_id "+
                "where cr.client_id = ?",
            (rs, rowNum) -> rs.getString(1), clientId
        );
        return perms.stream().collect(Collectors.toSet());
    }
}
