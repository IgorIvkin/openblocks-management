--liquibase formatted sql
--changeset igor:9

create table if not exists task_comment
(
    id              bigserial               not null,
    content         character varying(255)  not null,
    author_id       int8                    not null,
    task_code       character varying(255)  not null,
    created_at      timestamp with time zone not null default NOW(),
    constraint task_comment_pk primary key (id)
);

create index if not exists task_comment_author_id_idx on task_comment(author_id);
create index if not exists task_comment_task_code_idx on task_comment(task_code);

--rollback drop table task_comment;