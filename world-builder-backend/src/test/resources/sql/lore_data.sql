TRUNCATE TABLE worldbuilder.lore_entries CASCADE;

INSERT INTO worldbuilder.lore_entries (
    id,
    world_id,
    title,
    body,
    created_at,
    updated_at
)
VALUES
(
    'aaaaaaaa-1111-1111-1111-111111111111',
    '11111111-1111-1111-1111-111111111111',
    'The Northern Heavenly Sect',
    'A powerful sect in the northern region of Murim.',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
),
(
    'bbbbbbbb-2222-2222-2222-222222222222',
    '11111111-1111-1111-1111-111111111111',
    'The Five Great Clans',
    'Five influential clans that hold considerable power throughout Murim.',
    CURRENT_TIMESTAMP,
    CURRENT_TIMESTAMP
);