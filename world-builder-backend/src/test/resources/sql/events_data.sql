TRUNCATE TABLE worldbuilder.events CASCADE;

-- Events owned by testuser
INSERT INTO worldbuilder.events
    (id, world_id, title, description, date, type, created_at, updated_at)
VALUES
    (
        'aaaaaaaa-1111-1111-1111-111111111111',
        '11111111-1111-1111-1111-111111111111',
        'Battle of Murim',
        'The great battle for the Northern Heavenly Sect',
        '2026-01-10',
        'BATTLE',
        NOW(),
        NOW()
    ),
    (
        'bbbbbbbb-2222-2222-2222-222222222222',
        '11111111-1111-1111-1111-111111111111',
        'Council of Sects',
        'The martial arts sects gather to discuss their future',
        '2026-02-15',
        'MEETING',
        NOW(),
        NOW()
    ),
    (
        'cccccccc-3333-3333-3333-333333333333',
        '22222222-2222-2222-2222-222222222222',
        'Fall of Eldoria',
        'A dark force threatens the kingdom of Eldoria',
        '2026-03-20',
        'BATTLE',
        NOW(),
        NOW()
    );

-- Event owned by otheruser (for 403 tests)
INSERT INTO worldbuilder.events
    (id, world_id, title, description, date, type, created_at, updated_at)
VALUES
    (
        'dddddddd-4444-4444-4444-444444444444',
        '33333333-3333-3333-3333-333333333333',
        'Forbidden Event',
        'This event belongs to another user',
        '2026-04-01',
        'BATTLE',
        NOW(),
        NOW()
    );