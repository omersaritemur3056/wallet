DROP SCHEMA IF EXISTS public CASCADE;

CREATE SCHEMA public AUTHORIZATION postgres;

GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;

create sequence user_seq
    increment by 50;

alter sequence user_seq owner to postgres;

create sequence wallet_seq
    increment by 50;

alter sequence wallet_seq owner to postgres;

create sequence wallet_transaction_seq
    increment by 50;

alter sequence wallet_transaction_seq owner to postgres;

create table public.users
(
    id         bigint       not null
        primary key,
    created_at timestamp    not null,
    email      varchar(255) not null
        constraint uk_6dotkott2kjsp8vw4d0m25fb7
            unique,
    name       varchar(255) not null,
    password   varchar(255) not null,
    surname    varchar(255) not null,
    updated_at timestamp
);

alter table public.users
    owner to postgres;

create table public.wallet
(
    id         bigint         not null
        primary key,
    balance    numeric(19, 2) not null,
    created_at timestamp      not null,
    name       varchar(255)   not null,
    updated_at timestamp,
    user_id    bigint
        constraint fkgbusavqq0bdaodex4ee6v0811
            references public.users,
    deleted    boolean
);

alter table public.wallet
    owner to postgres;

create table public.wallet_transaction
(
    id                 bigint                                               not null
        primary key,
    amount             numeric(19, 2)                                       not null,
    created_at         timestamp                                            not null,
    description        varchar(255),
    transaction_id     uuid                                                 not null
        constraint uk_t2bo7umq66dt8cqw7xxnblwoy
            unique,
    transaction_type   varchar(255)                                         not null,
    transaction_status varchar(255) default 'PROCESSING'::character varying not null,
    receiver_wallet_id bigint
        constraint fkjuuknji4hqtrpnvelvykw5p81
            references public.wallet,
    sender_wallet_id   bigint
        constraint fk7cm6foxo3fukbja3tbph5pxqa
            references public.wallet
);

alter table public.wallet_transaction
    owner to postgres;



alter table public.wallet_transaction
    owner to postgres;