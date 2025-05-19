CREATE EXTENSION IF NOT EXISTS "pgcrypto";

CREATE TABLE IF NOT EXISTS users (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100),
    role VARCHAR(50) NOT NULL,

    CONSTRAINT chk_email_length CHECK (char_length(email) <= 150),
    CONSTRAINT chk_pswd_length CHECK (char_length(password) >= 8 AND char_length(password) <= 255),
    CONSTRAINT chk_role CHECK (role IN ('ADMIN', 'USER'))
);


