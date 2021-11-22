\c frugal_fennec;
CREATE TABLE IF NOT EXISTS account_projections (
  account_id UUID PRIMARY KEY,
  account_name VARCHAR(255) NOT NULL,
  data JSONB NOT NULL,
  version INT NOT NULL
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE account_projections TO frugal_fennec;
CREATE INDEX account_projections_account_name_idx ON account_projections(account_name);