# User Service For Chat Application

This microservice is part of the _[real-time chat application](https://github.com/vsayfb/real-time-chat-application)._

## Description

This microservice handles user management in the application and uses MongoDB to store them in a database.

## Running the application

#### Development

`docker compose up -d && docker compose logs -f`

#### Testing

`BUILD_TARGET=test docker compose up -d && docker compose logs -f`

#### Production

`docker build -t user-ms . && kubectl apply -f deployment.yml`
