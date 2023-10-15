--liquibase formatted sql
--changeset igor:3

create table if not exists project
(
    id              bigserial               not null,
    title           character varying(255)  not null,
    code            character varying(255)  not null,
    created_at      timestamp with time zone not null default NOW(),
    constraint project_pk primary key (id)
);

create unique index project_code_idx on project(code);


--rollback drop table project;