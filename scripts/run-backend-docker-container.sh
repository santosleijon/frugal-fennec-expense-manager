docker run -p 8081:8081 \
  --detach \
  --env ALLOWED_CORS_ORIGIN=http://localhost:8080 \
  --name frugal-fennec-backend \
  --rm \
  frugal-fennec-backend
