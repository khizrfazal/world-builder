TRUNCATE TABLE worldbuilder.factions CASCADE;

INSERT INTO worldbuilder.factions (
    id,
    world_id,
    name,
    description,
    alignment,
    type,
    created_at,
    updated_at
)
VALUES
    (
        'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa',
        '11111111-1111-1111-1111-111111111111',
        'Northern Heavenly Sect',
        'A powerful martial arts sect in the Northern region',
        'GOOD',
        'ORDER',
        NOW(),
        NOW()
    ),
    (
        'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb',
        '11111111-1111-1111-1111-111111111111',
        'Black Moon Clan',
        'A mysterious clan with a dark reputation',
        'EVIL',
        'CLAN',
        NOW(),
        NOW()
    ),
    (
        'cccccccc-cccc-cccc-cccc-cccccccccccc',
        '22222222-2222-2222-2222-222222222222',
        'Silvermoon Guild',
        'A trading guild based in Eldoria',
        'NEUTRAL',
        'GUILD',
        NOW(),
        NOW()
    ),
    (
        'dddddddd-dddd-dddd-dddd-dddddddddddd',
        '33333333-3333-3333-3333-333333333333',
        'Forbidden Faction',
        'Owned by another user',
        'EVIL',
        'CULT',
        NOW(),
        NOW()
    );