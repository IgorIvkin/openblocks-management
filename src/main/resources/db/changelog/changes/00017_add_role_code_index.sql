--liquibase formatted sql
--changeset igor:17

create unique index user_role_code on user_role(code);