package com.asrevo.cvhome.uaa.web.admin;

import com.asrevo.cvhome.uaa.dto.*;
import com.asrevo.cvhome.uaa.service.AdminClientService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/clients")
@AllArgsConstructor
public class AdminClientController {
    private final AdminClientService service;

    @PreAuthorize("hasAuthority('SCOPE_super_admin') or hasRole('SUPER_ADMIN')")
    @PostMapping
    public CreatedClientResponse create(@RequestBody CreateClientRequest req) {
        var created = service.createClient(new CreateClientCommand(
                req.clientId(), req.clientName(), req.redirectUris(), req.scopes(),
                req.grantTypes(), req.authMethods(), req.requirePkce(), req.requireConsent()
        ));
        return new CreatedClientResponse(created.clientId(), created.clientSecret());
    }

    @PreAuthorize("hasAuthority('SCOPE_super_admin') or hasRole('SUPER_ADMIN')")
    @GetMapping
    public Page<ClientSummary> list(@PageableDefault Pageable pageable) {
        return service.listClients(pageable);
    }

    @PreAuthorize("hasAuthority('SCOPE_super_admin') or hasRole('SUPER_ADMIN')")
    @GetMapping("{clientId}")
    public ClientDetails findOne(@PathVariable("clientId") String clientId) {
        return service.findByClientId(clientId);
    }

    @PreAuthorize("hasAuthority('SCOPE_super_admin') or hasRole('SUPER_ADMIN')")
    @DeleteMapping("{clientId}")
    public void delete(@PathVariable("clientId") String clientId) {
        service.delete(clientId);
    }
}
