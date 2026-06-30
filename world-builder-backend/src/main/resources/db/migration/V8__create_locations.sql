CREATE TABLE worldbuilder.locations (
    id UUID PRIMARY KEY,
    world_id UUID NOT NULL,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_location_world
        FOREIGN KEY (world_id)
        REFERENCES worldbuilder.worlds(id)
        ON DELETE CASCADE
);
