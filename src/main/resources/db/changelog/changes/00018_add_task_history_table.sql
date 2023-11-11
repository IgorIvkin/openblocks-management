--liquibase formatted sql
--changeset igor:18

create table if not exists task_history
(
    id              bigserial                not null,
    task_code       character varying(255)   not null,
    created_at      timestamp with time zone not null default NOW(),
    author_id       int8                     not null,
    change_object   int8                     not null,
    previous_value  text                     null,
    new_value       text                     null,
    constraint task_history_pk primary key (id)
);

create index if not exists task_history_task_code_idx on task_history(task_code);

--rollback drop table task_history;