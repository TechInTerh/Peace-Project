FROM node:18.3.0-alpine

WORKDIR /usr/app
COPY package.json .

RUN npm install --quiet
COPY . .

CMD node index.js
