--liquibase formatted sql
--changeset igor:1

create table if not exists user_data
(
    id              bigserial               not null,
    name            character varying(255)  not null,
    login           character varying(255)  not null,
    password        character varying(255)  not null,
    created_at      timestamp with time zone not null default NOW(),
    constraint user_data_pk primary key (id)
);

create unique index user_data_login_idx on user_data(login);
create index user_data_name_idx on user_data(name);


create table if not exists user_role
(
    id      bigserial               not null,
    title   character varying(255)  not null,
    constraint user_role_pk primary key (id)
);

create unique index user_role_title_index on user_role(title);


create table if not exists user_position
(
    user_id int8 not null,
    role_id int8 not null
);

create index user_position_user_id_index on user_position(user_id);

--rollback drop table user_data;
--rollback drop table user_role;
--rollback drop table user_position;