package com.asrevo.cvhome.uaa.web.admin;

import com.asrevo.cvhome.uaa.dto.ClientSummary;
import com.asrevo.cvhome.uaa.dto.CreateClientCommand;
import com.asrevo.cvhome.uaa.dto.CreateClientRequest;
import com.asrevo.cvhome.uaa.dto.CreatedClientResponse;
import com.asrevo.cvhome.uaa.service.AdminClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin/clients")
public class AdminClientController {
    private final AdminClientService service;

    public AdminClientController(AdminClientService service) {
        this.service = service;
    }

    // REST: create a client and return the plaintext secret once
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
    public RegisteredClient findOne(@PathVariable("clientId") String clientId) {
        return service.findByClientId(clientId);
    }

    @PreAuthorize("hasAuthority('SCOPE_super_admin') or hasRole('SUPER_ADMIN')")
    @DeleteMapping("{clientId}")
    public void delete(@PathVariable("clientId") String clientId) {
         service.delete(clientId);
    }
}
