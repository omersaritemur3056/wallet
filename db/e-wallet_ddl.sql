DROP SCHEMA IF EXISTS public CASCADE;

CREATE SCHEMA public AUTHORIZATION postgres;

GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;

CREATE TABLE users (
       id bigint NOT NULL,
       deleted bool NOT NULL,
       email varchar(255) NOT NULL,
       name varchar(255) NOT NULL,
       surname varchar(255) NOT NULL,
       password varchar(255) NULL,
       CONSTRAINT uk_6dotkott2kjsp8vw4d0m25fb7 UNIQUE (email),
       CONSTRAINT users_pkey PRIMARY KEY (id)
);

ALTER TABLE users OWNER TO postgres;
GRANT ALL ON TABLE users TO postgres;

CREATE SEQUENCE user_seq
    INCREMENT BY 50
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
    CACHE 1
    NO CYCLE;

ALTER SEQUENCE user_seq OWNER TO postgres;
GRANT ALL ON SEQUENCE user_seq TO postgres;