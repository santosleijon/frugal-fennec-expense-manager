docker run -p 8080:8080 \
  -d \
  --name frugal-fennec-ui \
  --env UI_PROXY_URL=http://localhost:8080? \
  --rm \
   frugal-fennec-ui:latest
