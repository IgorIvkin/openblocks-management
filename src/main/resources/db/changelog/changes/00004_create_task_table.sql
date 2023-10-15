--liquibase formatted sql
--changeset igor:4

create table if not exists task_status
(
    id              bigserial               not null,
    code            character varying(255)  not null,
    title           character varying(255)  not null,
    constraint task_status_pk primary key (id)
);

insert into task_status(id, code, title) values
(1, 'CREATED', 'Создано'),
(2, 'IN_WORK', 'В работе'),
(3, 'CLOSED', 'Закрыто');

create table if not exists task
(
    id              bigserial               not null,
    status          int8                    not null,
    project_code    character varying(255)  not null,
    subject         character varying(255)  not null,
    explanation     text                    null,
    created_at      timestamp with time zone not null default NOW(),
    updated_at      timestamp with time zone not null default NOW(),
    constraint task_pk primary key (id)
);

create index if not exists task_project_code_status_idx on task(project_code, status);

--rollback drop table task;
--rollback drop table task_status;