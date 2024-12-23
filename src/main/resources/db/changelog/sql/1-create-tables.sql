--liquibase formatted sql

--changeset dmitrii:1

CREATE TABLE users (
    uid UUID PRIMARY KEY NOT NULL,
    user_id VARCHAR(255) NOT NULL UNIQUE,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(255) NOT NULL,
    phone VARCHAR(20) NOT NULL UNIQUE,
    full_name VARCHAR(255) NOT NULL
);

CREATE TABLE guitars (
    uid UUID PRIMARY KEY NOT NULL,
    guitar_id VARCHAR(255) NOT NULL UNIQUE,
    model VARCHAR(255) NOT NULL UNIQUE,
    price INT NOT NULL,
    description TEXT NOT NULL
);

CREATE TABLE guitar_images (
     uid UUID PRIMARY KEY NOT NULL,
     guitar_id VARCHAR(255) REFERENCES guitars(guitar_id) NOT NULL,
     image BYTEA NOT NULL
);

CREATE TABLE orders (
     uid UUID PRIMARY KEY NOT NULL,
     user_id VARCHAR(255) REFERENCES users(user_id) NOT NULL,
     guitar_id VARCHAR(255) REFERENCES guitars(guitar_id) NOT NULL,
     status VARCHAR(255) NOT NULL,
     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

--rollback drop table order;
--rollback drop table guitar_image;
--rollback drop table guitar;
--rollback drop table user;

--changeset dmitrii:2

ALTER TABLE orders
    ADD COLUMN order_id VARCHAR(255) NOT NULL UNIQUE;


