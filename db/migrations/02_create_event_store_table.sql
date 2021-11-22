\c frugal_fennec;
CREATE TABLE IF NOT EXISTS event_store (
  event_id UUID PRIMARY KEY,
  aggregate_name VARCHAR(255) NOT NULL,
  aggregate_id UUID NOT NULL,
  event_date TIMESTAMP WITH TIME ZONE NOT NULL,
  version INT NOT NULL,
  data JSONB NOT NULL
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE event_store TO frugal_fennec;
CREATE INDEX event_store_aggregate_id_idx ON event_store(aggregate_id);