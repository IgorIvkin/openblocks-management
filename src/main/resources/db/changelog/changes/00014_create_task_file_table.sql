--liquibase formatted sql
--changeset igor:14

create table if not exists task_file
(
    id                  bigserial                not null,
    created_at          timestamp with time zone not null default NOW(),
    file_storage_type   int8                     not null default 1,
    task_code           character varying(255)   not null,
    owner_id            int8                     not null,
    file_path           character varying(512)   not null,
    mime_type           character varying(255)   null,
    file_name           character varying(255)   null,
    constraint task_file_pk primary key (id)
);

create index task_file_task_code_idx on task_file(task_code);
create index task_file_owner_id_idx on task_file(owner_id);

--rollback drop table task_file;