--liquibase formatted sql

--changeset g.liseychikov:CRS-4_create_humans_interests_table
CREATE TABLE IF NOT EXISTS humans_interests
(
    chat_id         BIGINT      REFERENCES chats(id)        NOT NULL,
    link_id         BIGINT      REFERENCES links(id)        NOT NULL,
    PRIMARY KEY (chat_id, link_id)
);

--changeset g.liseychikov:CRS-4_add_index_chat_link_id
CREATE INDEX idx_chat_link_id ON humans_interests(chat_id, link_id);
