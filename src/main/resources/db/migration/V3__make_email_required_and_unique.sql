ALTER TABLE users
    ALTER COLUMN email SET NOT NULL, ADD CONSTRAINT users_email_key UNIQUE (email);
