KEYSTORE_OUTPUT_FILE="../src/main/resources/localhost/localhost.p12"
KEYSTORE_PASSWORD="password"
PRIVATE_KEY_FILE="../ui/certs/localhost/localhost-privkey.pem"
CSR_FILE="csr.pem"
CERT_FILE="../ui/certs/localhost/localhost-cert.pem"

# Cleanup previous files
rm -f "$KEYSTORE_OUTPUT_FILE"
rm -f "$PRIVATE_KEY_FILE"
rm -f "$CERT_FILE"

# Generate private key and Certificate Signing Request
openssl req -newkey rsa:2048 \
  -keyout "$PRIVATE_KEY_FILE" \
  -out "$CSR_FILE" \
  -subj "/C=SE/O=Frugal Fennec Expense Manager/CN=localhost" \
  -nodes

# Generate certificate
openssl x509 -req -days 3650 \
  -in "$CSR_FILE" \
  -signkey "$PRIVATE_KEY_FILE" \
  -out "$CERT_FILE"

# Generate P12 file for backend
openssl pkcs12 -export -out "$KEYSTORE_OUTPUT_FILE" \
  -inkey "$PRIVATE_KEY_FILE" \
  -in "$CERT_FILE" \
  -passout pass:"$KEYSTORE_PASSWORD"

# Show details of generated files
printf "\n'%s' subject:\n" $CERT_FILE
openssl x509 -in "$CERT_FILE" -noout -subject

printf "\n'%s' subject:\n" $KEYSTORE_OUTPUT_FILE
openssl pkcs12 -in "$KEYSTORE_OUTPUT_FILE" -nodes -passin pass:"$KEYSTORE_PASSWORD" | openssl x509 -noout -subject

# Clean up temp files
rm -f "$CSR_FILE"
