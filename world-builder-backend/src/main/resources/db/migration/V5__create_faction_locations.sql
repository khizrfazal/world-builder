CREATE TABLE worldbuilder.faction_locations (
    id UUID PRIMARY KEY,
    world_id UUID NOT NULL,
    faction_id UUID NOT NULL,
    location_id UUID NOT NULL,
    role VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_faction_location_world
        FOREIGN KEY (world_id)
        REFERENCES worldbuilder.worlds(id)
        ON DELETE CASCADE
);
