#!/bin/bash
set -e
export PGPASSWORD=$POSTGRES_PASSWORD;
psql -X -U "postgres" <<-EOSQL
SELECT 'CREATE DATABASE "ewallet-postgre-db"' WHERE NOT EXISTS (SELECT * FROM pg_database WHERE datname = '"ewallet-postgre-db"') \gexec
SELECT 'CREATE USER "ewallet-postgre-user"' WHERE NOT EXISTS (SELECT * FROM pg_roles WHERE rolname = 'ewallet-postgre-user') \gexec
ALTER USER "ewallet-postgre-user" WITH PASSWORD 'H3gnuylLZLWhDw4w' \gexec
ALTER DATABASE "ewallet-postgre-db" OWNER TO "ewallet-postgre-user" \gexec
grant all privileges on database "ewallet-postgre-db" to "ewallet-postgre-user" \gexec
EOSQL
