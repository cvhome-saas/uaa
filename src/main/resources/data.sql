-- Seed roles
insert into roles (id, name) values
  ('11111111-1111-1111-1111-111111111111','SUPER_ADMIN')
on conflict (id) do nothing;

insert into roles (id, name) values
  ('22222222-2222-2222-2222-222222222222','USER')
on conflict (id) do nothing;

-- Seed permissions
insert into permissions (id, name) values
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1','user:create'),
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2','user:update'),
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3','client:create'),
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa4','client:read'),
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa5','client:write')
on conflict (id) do nothing;

-- Map SUPER_ADMIN to all permissions
insert into role_permissions (role_id, permission_id) values
  ('11111111-1111-1111-1111-111111111111','aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa1'),
  ('11111111-1111-1111-1111-111111111111','aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa2'),
  ('11111111-1111-1111-1111-111111111111','aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa3'),
  ('11111111-1111-1111-1111-111111111111','aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa4'),
  ('11111111-1111-1111-1111-111111111111','aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaa5')
on conflict do nothing;

-- Seed users (passwords are {bcrypt} encoded using DelegatingPasswordEncoder default)
-- superadmin / change-me
insert into users (id, username, email, password_hash, status)
values ('33333333-3333-3333-3333-333333333333','admin','superadmin@example.com',
        '{bcrypt}$2a$10$pse9zsAXkH/zOjZpfiP7X.weD6CNtVY/NR5A4mYUwbYqcYThHchRa','ACTIVE')
on conflict (id) do nothing;

-- regular user / user-pass
insert into users (id, username, email, password_hash, status)
values ('44444444-4444-4444-4444-444444444444','revo','user1@example.com',
        '{bcrypt}$2a$10$pse9zsAXkH/zOjZpfiP7X.weD6CNtVY/NR5A4mYUwbYqcYThHchRa','ACTIVE')
on conflict (id) do nothing;

-- Map user roles
insert into user_roles (user_id, role_id) values
  ('33333333-3333-3333-3333-333333333333','11111111-1111-1111-1111-111111111111'),
  ('44444444-4444-4444-4444-444444444444','22222222-2222-2222-2222-222222222222')
on conflict do nothing;

-- (moved below after clients are inserted to satisfy FK)

-- Seed OAuth clients (note: settings are simplified and may be adjusted per SAS version)
-- admin-sdk: client_credentials with scope super_admin
insert into oauth2_registered_client (
    id, client_id, client_id_issued_at, client_secret, client_name,
    client_authentication_methods, authorization_grant_types, redirect_uris,
    post_logout_redirect_uris, scopes, client_settings, token_settings
) values (
    '55555555-5555-5555-5555-555555555555',
    'admin-sdk', now(), '{bcrypt}$2a$10$k1r8o8rJ8Jx0R6i3F7m8OeJ5iG.8m0vV5x3yKqXQ7m4b6m8o1tH6C', 'Admin SDK',
    'client_secret_basic', 'client_credentials',
    NULL, NULL,
    'super_admin',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":false,"settings.token.x509-certificate-bound-access-tokens":false,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",86400.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",86400.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",86400.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}')on conflict (id) do nothing;

-- webapp: authorization_code + refresh_token with PKCE and consent
insert into oauth2_registered_client (
    id, client_id, client_id_issued_at, client_secret, client_name,
    client_authentication_methods, authorization_grant_types, redirect_uris,
    post_logout_redirect_uris, scopes, client_settings, token_settings
) values (
    '66666666-6666-6666-6666-666666666666',
    'webapp', now(), '{bcrypt}$2a$10$YpG1q7tR2b6u8w9x0yZ3Uu8sV7iQ5w3eD6tH8jK1lP2nR4sT6v8eW', 'Web App',
    'client_secret_basic', 'authorization_code,refresh_token',
    'https://app.example.com/callback',
    NULL,
    'openid,profile,api.read',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.client.require-proof-key":false,"settings.client.require-authorization-consent":true}',
    '{"@class":"java.util.Collections$UnmodifiableMap","settings.token.reuse-refresh-tokens":false,"settings.token.x509-certificate-bound-access-tokens":false,"settings.token.id-token-signature-algorithm":["org.springframework.security.oauth2.jose.jws.SignatureAlgorithm","RS256"],"settings.token.access-token-time-to-live":["java.time.Duration",86400.000000000],"settings.token.access-token-format":{"@class":"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat","value":"self-contained"},"settings.token.refresh-token-time-to-live":["java.time.Duration",86400.000000000],"settings.token.authorization-code-time-to-live":["java.time.Duration",86400.000000000],"settings.token.device-code-time-to-live":["java.time.Duration",300.000000000]}')
on conflict (id) do nothing;

-- Map machine client (admin-sdk) to SUPER_ADMIN role so its tokens include roles/perms
insert into client_roles (client_id, role_id) values
  ('admin-sdk','11111111-1111-1111-1111-111111111111')
on conflict do nothing;
