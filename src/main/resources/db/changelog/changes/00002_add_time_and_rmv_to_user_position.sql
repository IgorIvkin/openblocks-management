--liquibase formatted sql
--changeset igor:2

alter table user_position add column if not exists updated_at timestamp with time zone not null default NOW();
alter table user_position add column if not exists rmv boolean not null default true;

--rollback alter table user_position drop column if exists updated_at;
--rollback alter table user_position drop column if exists rmv;