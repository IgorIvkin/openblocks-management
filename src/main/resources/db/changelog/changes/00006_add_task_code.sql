--liquibase formatted sql
--changeset igor:6

alter table task add column if not exists code character varying(255) not null default '';
alter table project add column if not exists task_counter int8 not null default 0;

create unique index if not exists task_code_idx on task(code);