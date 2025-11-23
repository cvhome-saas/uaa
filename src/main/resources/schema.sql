-- Spring Authorization Server core tables for PostgreSQL
-- oauth2_registered_client
create table if not exists oauth2_registered_client (
  id varchar(100) primary key,
  client_id varchar(100) not null unique,
  client_id_issued_at timestamp not null,
  client_secret varchar(200),
  client_secret_expires_at timestamp,
  client_name varchar(200) not null,
  client_authentication_methods varchar(1000) not null,
  authorization_grant_types varchar(1000) not null,
  redirect_uris varchar(1000),
  post_logout_redirect_uris varchar(1000),
  scopes varchar(1000) not null,
  client_settings varchar(2000) not null,
  token_settings varchar(2000) not null
);

-- oauth2_authorization
create table if not exists oauth2_authorization (
  id varchar(100) primary key,
  registered_client_id varchar(100) not null,
  principal_name varchar(200) not null,
  authorization_grant_type varchar(100) not null,
  authorized_scopes varchar(1000),
  attributes bytea,

  state varchar(500),
  authorization_code_value bytea,
  authorization_code_issued_at timestamp,
  authorization_code_expires_at timestamp,
  authorization_code_metadata bytea,

  access_token_value bytea,
  access_token_issued_at timestamp,
  access_token_expires_at timestamp,
  access_token_metadata bytea,
  access_token_type varchar(100),
  access_token_scopes varchar(1000),

  oidc_id_token_value bytea,
  oidc_id_token_issued_at timestamp,
  oidc_id_token_expires_at timestamp,
  oidc_id_token_metadata bytea,

  refresh_token_value bytea,
  refresh_token_issued_at timestamp,
  refresh_token_expires_at timestamp,
  refresh_token_metadata bytea,

  foreign key (registered_client_id) references oauth2_registered_client (id)
);

-- oauth2_authorization_consent
create table if not exists oauth2_authorization_consent (
  registered_client_id varchar(100) not null,
  principal_name varchar(200) not null,
  authorities varchar(1000) not null,
  primary key (registered_client_id, principal_name),
  foreign key (registered_client_id) references oauth2_registered_client (id)
);

-- Client to Role mapping for machine (client_credentials) principals
-- Note: We reference oauth2_registered_client.client_id (unique) and store role_id (UUID)
create table if not exists client_roles (
  client_id varchar(100) not null,
  role_id uuid not null,
  primary key (client_id, role_id),
  foreign key (client_id) references oauth2_registered_client (client_id)
);
