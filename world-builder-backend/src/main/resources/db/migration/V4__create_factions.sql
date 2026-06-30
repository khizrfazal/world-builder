CREATE TABLE worldbuilder.factions (
    id UUID PRIMARY KEY,
    world_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    alignment VARCHAR(50),
    type VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_faction_world
        FOREIGN KEY (world_id)
        REFERENCES worldbuilder.worlds(id)
        ON DELETE CASCADE
);
