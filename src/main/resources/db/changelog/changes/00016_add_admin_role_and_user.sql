--liquibase formatted sql
--changeset igor:16

alter table user_role add column if not exists code character varying(255) not null default '';

insert into user_data (name, login, password, created_at) values
('Администратор', 'admin', '$2a$10$qEW.HL9sOY0rr6sAhWkbx.p3WzVlMJIZ/hWLaVeWOaeE90f/1TvhG', NOW());

insert into user_role(id, title, code) values
(1, 'Администратор', 'ADMINISTRATOR');

insert into user_position(user_id, role_id, rmv) values
((select id from user_data ud where ud.login = 'admin'), 1, false);
