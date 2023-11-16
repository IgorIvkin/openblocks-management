--liquibase formatted sql
--changeset igor:19

alter table project_access add column if not exists project_admin boolean not null default false;