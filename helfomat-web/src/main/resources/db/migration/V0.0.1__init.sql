create table approval
(
    approval_id            uuid not null
        constraint approval_pkey primary key,
    approved_domain_event  jsonb,
    created_by             varchar(255),
    created_date           timestamp with time zone,
    requested_domain_event jsonb,
    updated_by             varchar(255),
    updated_date           timestamp with time zone
);

create table event
(
    event_id        uuid not null
        constraint event_pkey primary key,
    created_by      varchar(255),
    created_date    timestamp with time zone,
    domain_event    jsonb,
    organization_id varchar(255)
);

create table picture
(
    picture_id   uuid    not null
        constraint picture_pkey primary key,
    created_by   varchar(255),
    created_date timestamp with time zone,
    public       boolean not null,
    updated_by   varchar(255),
    updated_date timestamp with time zone
);
