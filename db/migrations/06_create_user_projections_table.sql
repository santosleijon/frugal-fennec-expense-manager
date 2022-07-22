\c frugal_fennec;
CREATE TABLE IF NOT EXISTS user_projections (
  id UUID PRIMARY KEY,
  email VARCHAR(255) NOT NULL,
  data JSONB NOT NULL,
  version INT NOT NULL
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE user_projections TO frugal_fennec;
CREATE UNIQUE INDEX user_projections_email_idx ON user_projections(email);