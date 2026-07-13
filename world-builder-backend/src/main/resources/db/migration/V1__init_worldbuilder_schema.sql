CREATE SCHEMA IF NOT EXISTS worldbuilder;

CREATE TABLE worldbuilder.worlds (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);

ALTER TABLE worldbuilder.worlds
    ALTER COLUMN id SET DEFAULT gen_random_uuid();
