# Covid-19 Web Application

## Introduction

First individual project made for the *Testes e Qualidade de Software* course. The goal is to develop a web application that presents covid-19 statistics to the user.

## Functionalities

The user is allowed to search statistics from any country of the world and filter them by time interval (today, last week, last month, last year). It has a cache attached to the system, in order to respond faster to repeated request from the user.
The application functionalities are pretty simple, since the main focus of the project is to develop clean code with well-defined tests (unit tests, integration tests, funcionality tests, etc..).

## Architecture

- **Frontend** - Angular
- **Backend** - Spring
- **External API** - [COVID-19 API](https://rapidapi.com/api-sports/api/covid-193/)

## Web Application Demo

[![demo](https://img.youtube.com/vi/7s2TqLgZciw/0.jpg)](https://www.youtube.com/watch?v=7s2TqLgZciw)

## Report

The report, written in English, is inside the *report* folder.

## How to run

```
============================================================

# Terminal n1 - inside the covid_service/ (backend) folder:

cd covid_service/
./mvnw spring-boot:run

Runs on localhost:8080/

============================================================

# Terminal n2 - Inside the frontend/ (frontend) folder:

cd frontend/
ng serve

Runs on localhost:4200/

============================================================

```

## Author

- [Ricardo Rodriguez](https://github.com/ricardombrodriguez)
