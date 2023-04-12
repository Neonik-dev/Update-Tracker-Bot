--liquibase formatted sql

--changeset g.liseychikov:CRS-2_add_domains_sequence
CREATE SEQUENCE IF NOT EXISTS domain_id_seq
    START WITH 1
    INCREMENT BY 1
    CACHE 1
;

--changeset g.liseychikov:CRS-2_create_domains_table
CREATE TABLE IF NOT EXISTS domains
(
    id              BIGINT      primary key         default nextval('domain_id_seq'),
    name            TEXT        UNIQUE                                      NOT NULL,
    created_date    Date        default now()                               NOT NULL
);
