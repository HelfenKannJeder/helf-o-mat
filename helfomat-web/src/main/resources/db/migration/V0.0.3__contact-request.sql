create table contact_request
(
    contact_request_id            uuid    not null
        constraint contact_request_pkey
            primary key,
    name                          varchar(255),
    age                           integer,
    confirmation_code             varchar(255),
    contact_person_email          varchar(255),
    contact_person_firstname      varchar(255),
    contact_person_lastname       varchar(255),
    contact_person_telephone      varchar(255),
    created_by                    varchar(255),
    created_date                  timestamp with time zone,
    email                         varchar(255),
    last_confirmation_email_sent  timestamp with time zone,
    location                      varchar(255),
    message                       varchar(255),
    number_of_confirmation_emails integer not null,
    organization_id               varchar(255),
    status                        integer,
    subject                       varchar(255),
    updated_by                    varchar(255),
    updated_date                  timestamp with time zone
);
