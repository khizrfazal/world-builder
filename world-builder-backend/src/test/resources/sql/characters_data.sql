TRUNCATE TABLE worldbuilder.characters CASCADE;

-- Characters owned by testuser
INSERT INTO worldbuilder.characters (id, world_id, name, summary, created_at, updated_at)
VALUES
  ('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', '11111111-1111-1111-1111-111111111111', 'Jin Mu-Won', 'Young master of the Northern Heavenly Sect', NOW(), NOW()),
  ('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '11111111-1111-1111-1111-111111111111', 'Seo Hae-Rang', 'Sword prodigy of Murim', NOW(), NOW()),
  ('cccccccc-cccc-cccc-cccc-cccccccccccc', '22222222-2222-2222-2222-222222222222', 'Aeloria Windleaf', 'Elven mage of Eldoria', NOW(), NOW());

-- Character owned by otheruser (for 403 tests)
INSERT INTO worldbuilder.characters (id, world_id, name, summary, created_at, updated_at)
VALUES
  ('dddddddd-dddd-dddd-dddd-dddddddddddd',
   '33333333-3333-3333-3333-333333333333',
   'Forbidden Hero',
   'Should not be accessible',
   NOW(),
   NOW());