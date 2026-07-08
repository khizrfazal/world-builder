TRUNCATE TABLE worldbuilder.worlds CASCADE;

INSERT INTO worldbuilder.worlds (title, description, created_at, updated_at)
VALUES
    ('Murim', 'Cultivation world', NOW(), NOW()),
    ('Eldoria', 'High fantasy realm', NOW(), NOW());
