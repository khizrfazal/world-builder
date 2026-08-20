TRUNCATE TABLE worldbuilder.users CASCADE;

INSERT INTO worldbuilder.users (id, username, password)
VALUES (
    '00000000-0000-0000-0000-000000000001',
    'testuser',
    'password'
);
