# quackus

Project made in a day, just trying out the Quarkus framework, so didn't have time to write tests with JUnit nor to make proper validation mechanisms, was just for trying and getting the hang of the Quarkus flow. Found it funny to make a simple CRUD app that resembles the frameworks' name. So it's a service composed by a REST-API with an endpoint to get random duck memes and another one to upload an image (jpg, jpeg, png). It's also composed by a simple React app for a frontend so you don't have to use cURL or Postman to mock requests. It uses PostGreSQL as a database.

## Disclaimer

I know that it doesn't make much sense to have the React App getting the contents (in this case images) as blobs and sending them as multipart when it is exactly running on top of Quarkus, I'm just serving the index.html that I manipulate with React, but it's for demonstrating the flow, in a classical microservices architecture I'd have React deployed and this REST service deployed as well separately and they'd communicate via either direct API calls or via a message broker like Apache Kafka or RabbitMQ.

So it's not a Production Grade Product, it's just me figuring my way around the framework and its flow.

## Instructions

Firstly you got to build the image (I'll show the JVM image commands):

```bash
    ./mvnw package -DskipTests
    docker build -f src/main/docker/Dockerfile.jvm -t quarkus/quackus-jvm .
```

Now you got to setup the .env. You have a .env.example in the repository, just rename it to .env and guarantee it is in the same directory as the docker-compose.yml. Run the docker-compose. It will start a PostGreSQL container and this app (JVM container only).

```bash
    docker-compose up -d
```

And access localhost:8080/ (maybe wait a little if Database or App is not available yet)

To stop the container

```bash
     docker-compose down -v
```

I tested with Postman, and tried everything out in Quarkus Dev mode and also ran the containers with docker-compose.
