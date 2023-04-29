--liquibase formatted sql

--changeset g.liseychikov:CRS-6_alter_default_id_in_links_and_domains_tables
ALTER TABLE domains ALTER COLUMN id SET default nextval('domain_id_seq');
ALTER TABLE links ALTER COLUMN id SET default nextval('link_id_seq');