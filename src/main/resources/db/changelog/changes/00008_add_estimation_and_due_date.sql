--liquibase formatted sql
--changeset igor:8

alter table task add column if not exists due_date date null;
alter table task add column if not exists estimation integer null;