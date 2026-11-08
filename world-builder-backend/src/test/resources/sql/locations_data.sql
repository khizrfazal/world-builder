TRUNCATE TABLE worldbuilder.locations CASCADE;

INSERT INTO worldbuilder.locations (
    id,
    world_id,
    name,
    description,
    created_at,
    updated_at
)
VALUES
    (
        'aaaaaaaa-1111-1111-1111-111111111111',
        '11111111-1111-1111-1111-111111111111',
        'Northern Heavenly Sect',
        'The headquarters of the Northern Heavenly Sect, hidden among the mountains.',
        NOW(),
        NOW()
    ),
    (
        'bbbbbbbb-2222-2222-2222-222222222222',
        '11111111-1111-1111-1111-111111111111',
        'Black Moon Valley',
        'A remote valley used as a secret meeting place by the Black Moon Clan.',
        NOW(),
        NOW()
    ),
    (
        'cccccccc-3333-3333-3333-333333333333',
        '22222222-2222-2222-2222-222222222222',
        'Silvermoon City',
        'A prosperous city known for its markets and magical academies.',
        NOW(),
        NOW()
    ),
    (
        'dddddddd-4444-4444-4444-444444444444',
        '33333333-3333-3333-3333-333333333333',
        'Forbidden Ruins',
        'Ancient ruins concealed from the rest of the world.',
        NOW(),
        NOW()
    );