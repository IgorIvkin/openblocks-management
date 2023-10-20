--liquibase formatted sql
--changeset igor:12

drop index if exists user_data_name_idx;
create index if not exists user_data_name_idx on user_data(upper(name));

--rollback drop index if exists user_data_name_idx;