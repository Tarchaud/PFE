FROM node:20.9.0-alpine3.14

WORKDIR /app

COPY . .

RUN npm install

RUN ["npm", "run", "start_docker"]
