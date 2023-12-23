--liquibase formatted sql
--changeset igor:20

create table if not exists sprint_layout
(
    id                  bigserial                not null,
    project_code        character varying(255)   not null,
    sprint_id           int8                     not null,
    sprint_layout       character varying(255)[] not null,
    constraint sprint_layout_pk primary key (id)
);

create index sprint_layout_sprint_id_idx on sprint_layout(sprint_id);

--rollback drop table sprint_layout;