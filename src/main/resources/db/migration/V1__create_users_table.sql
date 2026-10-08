CREATE TABLE users (
                       id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
                       name VARCHAR(100) NOT NULL,
                       age INTEGER NOT NULL CHECK (age >= 0 AND age <= 150),
                       is_male BOOLEAN NOT NULL
);