--liquibase formatted sql
--changeset drlionardo:create-otp
CREATE TABLE otp
(
    user_id    BIGSERIAL PRIMARY KEY,
    code       TEXT,
    expires_at TIMESTAMP
);