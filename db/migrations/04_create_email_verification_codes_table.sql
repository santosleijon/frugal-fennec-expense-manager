\c frugal_fennec;
CREATE TABLE IF NOT EXISTS email_verification_codes (
  email VARCHAR(255) NOT NULL,
  verification_code VARCHAR(4) NOT NULL,
  issued TIMESTAMP WITH TIME ZONE NOT NULL,
  valid_to TIMESTAMP WITH TIME ZONE NOT NULL
);
GRANT SELECT, INSERT, UPDATE, DELETE ON TABLE email_verification_codes TO frugal_fennec;
CREATE INDEX email_verification_codes_email_idx ON email_verification_codes(email);