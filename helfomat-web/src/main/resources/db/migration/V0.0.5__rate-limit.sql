-- "if not exists" because helfomat-import shares this database and runs with ddl-auto: update, so
-- it may have created the table already if it started before this migration ran.
create table if not exists rate_limit
(
    bucket       varchar(255)             not null,
    window_start timestamp with time zone not null,
    counter      integer                  not null,
    constraint rate_limit_pkey
        primary key (bucket, window_start)
);
