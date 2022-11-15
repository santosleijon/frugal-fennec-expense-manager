FROM node:16-slim

# Build stage
COPY ./build ./
COPY ./certs/production ./certs/production
RUN npm install -g http-server

# Run stage
EXPOSE 8080
ENTRYPOINT ["npx", "http-server", "--port", "8080", "./"]
