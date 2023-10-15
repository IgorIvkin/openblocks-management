--liquibase formatted sql
--changeset igor:7

alter table task add column if not exists task_type int8 not null default 1;