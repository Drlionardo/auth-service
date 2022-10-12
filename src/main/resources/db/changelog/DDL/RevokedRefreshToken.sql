--liquibase formatted sql
--changeset drlionardo:create-revoked-refresh-token
CREATE TABLE revoked_token
(
    user_id    BIGSERIAL PRIMARY KEY,
    token      TEXT,
    expires_at TIMESTAMP
);