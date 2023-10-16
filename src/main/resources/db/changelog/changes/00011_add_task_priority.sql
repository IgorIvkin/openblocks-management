--liquibase formatted sql
--changeset igor:8

alter table task add column if not exists priority int8 not null default 2;