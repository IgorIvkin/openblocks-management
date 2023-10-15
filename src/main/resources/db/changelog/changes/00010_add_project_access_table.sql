--liquibase formatted sql
--changeset igor:10

create table if not exists project_access
(
    id              bigserial               not null,
    user_id         int8                    not null,
    project_code    character varying(255)  not null,
    created_at      timestamp with time zone not null default NOW(),
    constraint project_access_pk primary key (id)
);

create index if not exists project_access_user_id_project_code_idx on project_access(user_id, project_code);

--rollback drop table project_access;