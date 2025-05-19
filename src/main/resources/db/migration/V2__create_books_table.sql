CREATE TABLE IF NOT EXISTS books (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    title VARCHAR(255) NOT NULL UNIQUE,
    author VARCHAR(255),
    pub_year VARCHAR(4),

    CONSTRAINT chk_title_length CHECK (char_length(title) <= 255)
);


