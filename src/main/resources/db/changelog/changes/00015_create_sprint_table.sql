--liquibase formatted sql
--changeset igor:15

create table if not exists sprint
(
    id                  bigserial                not null,
    created_at          timestamp with time zone not null default NOW(),
    project_code        character varying(255)   not null,
    title               character varying(255)   not null,
    start_date          date                     not null default NOW(),
    end_date            date                     not null default NOW(),
    finished            boolean                  not null default false,
    constraint sprint_pk primary key (id)
);

create index if not exists sprint_project_code_idx on sprint(project_code);

alter table task add column if not exists sprint int8 null;

create index if not exists task_sprint_idx on task(sprint) where sprint is not null;

--rollback drop table sprint;
--rollback alter table task drop column if exists sprint;