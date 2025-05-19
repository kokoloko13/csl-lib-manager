INSERT INTO users(email, password, name, role)
SELECT
    'admin@csl.pl',
    '$2b$12$8Xuunn0OyLmONy6JJtIm6eaMhSd8U3GrLu3Elad9rcFdvEqiy7xqy',
    'CSL Admin',
    'ADMIN'
WHERE NOT EXISTS (
    SELECT 1 FROM users WHERE role = 'ADMIN'
)