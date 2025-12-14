-- Spring Authorization Server core tables for PostgreSQL
-- oauth2_registered_client
drop schema if exists public CASCADE;
create schema public;
create table if not exists public.oauth2_registered_client
(
    id                            varchar(100) primary key,
    client_id                     varchar(100)  not null unique,
    client_id_issued_at           timestamp     not null,
    client_secret                 varchar(200),
    client_secret_expires_at      timestamp,
    client_name                   varchar(200)  not null,
    client_authentication_methods varchar(1000) not null,
    authorization_grant_types     varchar(1000) not null,
    redirect_uris                 varchar(1000),
    post_logout_redirect_uris     varchar(1000),
    scopes                        varchar(1000) not null,
    client_settings               varchar(2000) not null,
    token_settings                varchar(2000) not null
);

-- oauth2_authorization
create table if not exists public.oauth2_authorization
(
    id                            varchar(100) primary key,
    registered_client_id          varchar(100) not null,
    principal_name                varchar(200) not null,
    authorization_grant_type      varchar(100) not null,
    authorized_scopes             varchar(1000),
    attributes                    bytea,

    state                         varchar(500),
    authorization_code_value      bytea,
    authorization_code_issued_at  timestamp,
    authorization_code_expires_at timestamp,
    authorization_code_metadata   bytea,

    access_token_value            bytea,
    access_token_issued_at        timestamp,
    access_token_expires_at       timestamp,
    access_token_metadata         bytea,
    access_token_type             varchar(100),
    access_token_scopes           varchar(1000),

    oidc_id_token_value           bytea,
    oidc_id_token_issued_at       timestamp,
    oidc_id_token_expires_at      timestamp,
    oidc_id_token_metadata        bytea,

    refresh_token_value           bytea,
    refresh_token_issued_at       timestamp,
    refresh_token_expires_at      timestamp,
    refresh_token_metadata        bytea,

    foreign key (registered_client_id) references public.oauth2_registered_client (id)
);

-- oauth2_authorization_consent
create table if not exists public.oauth2_authorization_consent
(
    registered_client_id varchar(100)  not null,
    principal_name       varchar(200)  not null,
    authorities          varchar(1000) not null,
    primary key (registered_client_id, principal_name),
    foreign key (registered_client_id) references public.oauth2_registered_client (id)
);


-- Users table
CREATE TABLE public.users
(
    id            uuid PRIMARY KEY,
    email         VARCHAR(254) NOT NULL UNIQUE,
    username      VARCHAR(190) NOT NULL UNIQUE,
    password_hash VARCHAR(100) NOT NULL,
    status        VARCHAR(20),
    enabled       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    constraint UK6dotkott2kjsp8vw4d0m25fb7 unique (email),
    constraint UKr43af9ap4edm43mmtq01oddj6 unique (username)
);

-- Roles table
CREATE TABLE public.roles
(
    id   uuid PRIMARY KEY,
    name VARCHAR(80) NOT NULL,
    constraint UKofx66keruapi6vyqpv6f2or37 unique (name)
);

-- User-Roles join table
CREATE TABLE public.user_roles
(
    user_id uuid NOT NULL,
    role_id uuid NOT NULL,
    PRIMARY KEY (user_id, role_id),
    constraint FKh8ciramu9cc9q3qcqiv4ue8a6 foreign key (role_id) references public.roles,
    constraint FKhfh9dx7w3ubf1co1vdev94g3f foreign key (user_id) references public.users
);

-- Client to Role mapping for machine (client_credentials) principals
-- Note: We reference oauth2_registered_client.client_id (unique) and store role_id (UUID)
create table if not exists public.client_roles
(
    client_id varchar(100) not null,
    role_id   uuid         not null,
    primary key (client_id, role_id),
    foreign key (client_id) references public.oauth2_registered_client (client_id),
    foreign key (role_id) references public.roles (id)
);


create table public.signing_keys
(
    id         uuid                        not null,
    active     boolean                     not null,
    created_at timestamp(6) with time zone not null,
    jwk_json   oid                         not null,
    kid        varchar(190)                not null,
    primary key (id),
    constraint UKtobs6m52hleh04iy0qgpb2yfv unique (kid)
)
