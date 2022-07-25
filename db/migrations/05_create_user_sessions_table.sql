\c frugal_fennec;
CREATE TABLE IF NOT EXISTS user_sessions (
  user_id UUID NOT NULL,
  token VARCHAR(512) NOT NULL,
  issued TIMESTAMP WITH TIME ZONE NOT NULL,
  valid_to TIMESTAMP WITH TIME ZONE NOT NULL
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE user_sessions TO frugal_fennec;
CREATE INDEX user_sessions_user_id_idx ON user_sessions(user_id);
CREATE UNIQUE INDEX user_sessions_token_idx ON user_sessions(token);