FROM node:16-slim

# Copy build from dev machine
COPY ./build ./
RUN npm install -g http-server

# Run stage
EXPOSE 8080
ENTRYPOINT ["npx", "http-server", "--port", "8080", "./"]
