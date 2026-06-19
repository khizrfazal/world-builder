CREATE TABLE worldbuilder.lore_entries (
    id UUID PRIMARY KEY,
    world_id UUID NOT NULL,
    title VARCHAR(255) NOT NULL,
    body TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP
);