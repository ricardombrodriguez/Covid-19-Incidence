FROM node:18-alpine AS build

WORKDIR /usr/src/app

COPY package.json package-lock.json ./

RUN npm install

COPY . .

RUN npm run build 

FROM nginx:latest

COPY --from=build /usr/src/app/dist/ /usr/share/nginx/html