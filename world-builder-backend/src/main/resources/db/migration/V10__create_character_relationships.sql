CREATE TABLE worldbuilder.character_relationships (
    id UUID PRIMARY KEY,
    world_id UUID NOT NULL,
    from_character_id UUID NOT NULL,
    to_character_id UUID NOT NULL,
    type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_character_relationship_world
        FOREIGN KEY (world_id)
        REFERENCES worldbuilder.worlds(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_character_relationship_from
        FOREIGN KEY (from_character_id)
        REFERENCES worldbuilder.characters(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_character_relationship_to
        FOREIGN KEY (to_character_id)
        REFERENCES worldbuilder.characters(id)
        ON DELETE CASCADE
);
