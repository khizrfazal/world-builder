CREATE TABLE worldbuilder.character_locations (
    id UUID PRIMARY KEY,
    world_id UUID NOT NULL,
    character_id UUID NOT NULL,
    location_id UUID NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_character_location_world
        FOREIGN KEY (world_id)
        REFERENCES worldbuilder.worlds(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_character_location_character
        FOREIGN KEY (character_id)
        REFERENCES worldbuilder.characters(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_character_location_location
        FOREIGN KEY (location_id)
        REFERENCES worldbuilder.locations(id)
        ON DELETE CASCADE
);
