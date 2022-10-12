--liquibase formatted sql
--changeset drlionardo:create-usr
CREATE TABLE usr
(
    id              BIGSERIAL PRIMARY KEY,
    email           TEXT,
    login           TEXT,
    password        TEXT,
    email_confirmed BOOL
);
--changeset drlionardo:add-otp-usr-reference
ALTER TABLE IF EXISTS otp
    ADD CONSTRAINT otp_usr_fk FOREIGN KEY (user_id) REFERENCES usr;
