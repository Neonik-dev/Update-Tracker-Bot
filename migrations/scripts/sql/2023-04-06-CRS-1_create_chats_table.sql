--liquibase formatted sql

--changeset g.liseychikov:CRS-1_create_chats_table
CREATE TABLE IF NOT EXISTS chats
(
    id              BIGINT                                                               PRIMARY KEY,
    created_date    TIMESTAMP WITHOUT TIME ZONE     default now()                           NOT NULL,
    last_call_date  DATE                            default now()                           NOT NULL
);