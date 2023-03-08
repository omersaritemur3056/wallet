#!/bin/bash
set -e
export PGPASSWORD=$POSTGRES_PASSWORD;
psql -X -U "postgres" <<-EOSQL
SELECT 'CREATE DATABASE "wallet-postgre-db"' WHERE NOT EXISTS (SELECT * FROM pg_database WHERE datname = '"wallet-postgre-db"') \gexec
SELECT 'CREATE USER "wallet-postgre-user"' WHERE NOT EXISTS (SELECT * FROM pg_roles WHERE rolname = 'wallet-postgre-user') \gexec
ALTER USER "wallet-postgre-user" WITH PASSWORD 'H3gnuylLZLWhDw4w' \gexec
ALTER DATABASE "wallet-postgre-db" OWNER TO "wallet-postgre-user" \gexec
grant all privileges on database "wallet-postgre-db" to "wallet-postgre-user" \gexec
EOSQL
