CREATE TABLE worldbuilder.relationships (
    id UUID PRIMARY KEY,
    world_id UUID NOT NULL,
    source_character_id UUID NOT NULL,
    target_character_id UUID NOT NULL,
    relationship_type VARCHAR(50) NOT NULL,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,

    CONSTRAINT fk_relationship_world
        FOREIGN KEY (world_id)
        REFERENCES worldbuilder.worlds(id),

    CONSTRAINT fk_relationship_source_character
        FOREIGN KEY (source_character_id)
        REFERENCES worldbuilder.characters(id),

    CONSTRAINT fk_relationship_target_character
        FOREIGN KEY (target_character_id)
        REFERENCES worldbuilder.characters(id)
);