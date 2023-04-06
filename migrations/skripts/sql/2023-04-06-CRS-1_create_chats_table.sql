--liquibase formatted sql

--changeset g.liseychikov:CRS-1_add_chats_sequence
CREATE SEQUENCE IF NOT EXISTS chat_id_seq
    START WITH 1
    INCREMENT BY 1
    CACHE 1
;


--changeset g.liseychikov:CRS-1_create_chats_table
CREATE TABLE IF NOT EXISTS chats
(
    id              BIGINT      primary key         default nextval('chat_id_seq'),
    name            TEXT                                                                    NOT NULL,
    created_date    TIMESTAMP WITHOUT TIME ZONE     default now()                           NOT NULL,
    last_call_date  DATE                            default now()                           NOT NULL
);

--changeset g.liseychikov:CRS-1_add_index_chats_id
CREATE INDEX idx_chat_id ON chats(id);
