\c frugal_fennec;
CREATE TABLE IF NOT EXISTS user_session_projections (
  id UUID PRIMARY KEY,
  user_id UUID NOT NULL,
  token VARCHAR(512) NOT NULL,
  valid_to TIMESTAMP WITH TIME ZONE NOT NULL,
  data JSONB NOT NULL,
  version INT NOT NULL
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE user_session_projections TO frugal_fennec;
CREATE INDEX user_session_projections_id_idx ON user_session_projections(id);
CREATE INDEX user_session_projections_token_idx ON user_session_projections(token);
