CREATE TABLE worldbuilder.event_characters (
    id UUID PRIMARY KEY,
    world_id UUID NOT NULL,
    event_id UUID NOT NULL,
    character_id UUID NOT NULL,
    role VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_event_character_world
        FOREIGN KEY (world_id)
        REFERENCES worldbuilder.worlds(id)
        ON DELETE CASCADE
);
