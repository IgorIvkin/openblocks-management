--liquibase formatted sql
--changeset igor:13

create table if not exists task_link
(
    id                  bigserial                not null,
    created_at          timestamp with time zone not null default NOW(),
    link_type           int8                     not null default 1,
    task_code           character varying(255)   not null,
    connected_task_code character varying(255)   not null,
    constraint task_link_pk primary key (id)
);

create index task_link_task_code_idx on task_link(task_code);
create index task_link_connected_task_code_idx on task_link(connected_task_code);

--rollback drop table task_link;