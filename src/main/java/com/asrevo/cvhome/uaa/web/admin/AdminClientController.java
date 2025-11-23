package com.asrevo.cvhome.uaa.web.admin;

import com.asrevo.cvhome.uaa.service.AdminClientService;
import com.asrevo.cvhome.uaa.service.OAuthGrantType;
import com.asrevo.cvhome.uaa.service.ClientAuthMethod;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

record CreateClientRequest(
    String clientId,
    @NotBlank String clientName,
    Set<String> redirectUris,
    Set<String> scopes,
    Set<OAuthGrantType> grantTypes,         // standardized enum grant types
    Set<ClientAuthMethod> authMethods,        // standardized enum client auth methods
    Boolean requirePkce,
    Boolean requireConsent
) {}

record CreatedClientResponse(String clientId, String clientSecret) {}

@RestController
@RequestMapping("/admin/clients")
public class AdminClientController {
    private final AdminClientService service;

    public AdminClientController(AdminClientService service) {
        this.service = service;
    }

    // REST: create a client and return the plaintext secret once
    @PreAuthorize("hasAuthority('SCOPE_super_admin') or hasRole('SUPER_ADMIN')")
    @PostMapping
    public CreatedClientResponse create(@RequestBody CreateClientRequest req) {
        var created = service.createClient(new AdminClientService.CreateClientCommand(
            req.clientId(), req.clientName(), req.redirectUris(), req.scopes(),
            req.grantTypes(), req.authMethods(), req.requirePkce(), req.requireConsent()
        ));
        return new CreatedClientResponse(created.clientId(), created.clientSecret());
    }
}

@Controller
@RequestMapping("/admin/clients")
class AdminClientPageController {
    private final AdminClientService service;

    public AdminClientPageController(AdminClientService service) { this.service = service; }

    @PreAuthorize("hasAuthority('SCOPE_super_admin') or hasRole('SUPER_ADMIN')")
    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("result", null);
        return "admin/clients";
    }

    @PreAuthorize("hasAuthority('SCOPE_super_admin') or hasRole('SUPER_ADMIN')")
    @PostMapping("/new")
    public String submit(@RequestParam String clientName,
                         @RequestParam(required = false) Set<String> redirectUris,
                         @RequestParam(required = false) Set<String> scopes,
                         Model model) {
        // Use sensible defaults for form-based creation: code + refresh, client_secret_basic, PKCE+consent true
        var created = service.createClient(new AdminClientService.CreateClientCommand(
            null, clientName, redirectUris, scopes,
            Set.of(OAuthGrantType.AUTHORIZATION_CODE, OAuthGrantType.REFRESH_TOKEN),
            Set.of(ClientAuthMethod.CLIENT_SECRET_BASIC),
            true,
            true
        ));
        model.addAttribute("result", created);
        return "admin/clients";
    }

    // REST: list clients with pagination
    @PreAuthorize("hasAuthority('SCOPE_super_admin') or hasRole('SUPER_ADMIN')")
    @GetMapping
    public AdminClientService.PagedClients list(@RequestParam(defaultValue = "0") int page,
                                                @RequestParam(defaultValue = "20") int size) {
        return service.listClients(page, size);
    }
}
