FROM node:16-slim

# Copy build from dev machine
COPY ./build ./
RUN npm install -g http-server

# Run stage
EXPOSE 8080
ENTRYPOINT http-server --port 8080 --proxy "$UI_PROXY_URL" ./
