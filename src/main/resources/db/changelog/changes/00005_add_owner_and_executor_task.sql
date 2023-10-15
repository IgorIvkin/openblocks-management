--liquibase formatted sql
--changeset igor:5

alter table task add column if not exists owner_id int8 null;
alter table task add column if not exists executor_id int8 null;

create index if not exists task_owner_id_project_idx on task(executor_id, project_code) where executor_id is not null;