# Frugal Fennec Expense Manager

*Frugal Fennec Expense Manager* a single page web application that can be used to track expenses over time. It allows a user to:
* Log in using email verification without needing a password
* Manage an expense accounts structure
* Post expenses to accounts
* View expense reports as line charts

![screen capture of demo](https://github.com/santosleijon/frugal-fennec-expense-manager/blob/main/docs/demo.gif?raw=true)

## Technology stack

* Backend
  * [Kotlin](https://kotlinlang.org/) - Strongly statically typed programming language that runs on the JVM, but with a [number of improvements over Java](https://kotlinlang.org/docs/comparison-to-java.html#some-java-issues-addressed-in-kotlin).
  * [Spring Boot](https://spring.io/projects/spring-boot) - Framework that makes it easy to write production-ready web applications with Spring.
  * [PostgreSQL](https://www.postgresql.org/) - The world's most advanced open source relational database.
  * [SendGrid](https://sendgrid.com/) - Email delivery service.
* Frontend
  * [React](https://github.com/facebook/react/) - Used to build web UI with functional components.
  * [Redux](https://github.com/reduxjs/redux) - Used to handle UI state.
  * [MUI Core (formerly Material-UI)](https://github.com/mui/material-ui) - Used as the frontend design system.
  * [DevExtreme React Grid](https://devexpress.github.io/devextreme-reactive/react/grid/) - Used to generate charts.
* Automated end-to-end testing
  * [JUnit](https://github.com/junit-team/junit4) - Test framework used to execute Cucumber tests.
  * [Cucumber](https://cucumber.io/) - Makes it possible to write human readably Behavior-Driven Development (BDD) test scenarios using the [Gherkin](https://cucumber.io/docs/gherkin/reference/) *Given-When-Then* syntax.
  * [Selenium](https://www.selenium.dev/) - Used to execute automated test steps in a real web browser.
  * [Testcontainers](https://www.testcontainers.org/) - Used to set up a complete PostgreSQL database instance in a Docker container during the execution of test scenarios.


## Launching the application

1. Download the Git repo:

```
git clone https://github.com/santosleijon/frugal-fennec-expense-manager.git
```

2. Generate self-signed TLS certificates for both frontend and backend
```
cd scripts
./generate-localhost-cert.sh
```

3. Build and start the Kotlin backend server:

```
mvn clean install
java -jar ./target/frugal-fennec-expense-manager-0.0.1.jar
```

4. Build and start the React frontend:

```
npm install
npm start
```
## Executing automated end-to-end tests

The automated end-to-end tests are executed as part of the *verify* Maven lifecycle phase.

```
mvn clean verify
```

This command will trigger the following:
* A fresh database instance will be started in a Docker container.
* The Kotlin backend server will start on port `8081`.
* The web UI will be served on port `8080`.
* Selenium will open a Chrome web browser and use it to execute the test steps.

## Backend project structure
The backend uses a layered project structure inspired by [Microsoft's Domain-Driven Design (DDD) practices](https://docs.microsoft.com/en-us/dotnet/architecture/microservices/microservice-ddd-cqrs-patterns/ddd-oriented-microservice) with the aim to separate the domain entities with business logic, the REST web API and the persistence layer from each other.
This makes it possible to change these parts independently.

![backend project structure](https://github.com/santosleijon/frugal-fennec-expense-manager/blob/main/docs/backend_project_structure.png?raw=true)

* **Domain layer** - Contains domain entities and value-objects (i.e. *Account* and *Expense*) with their associated business logic, as well as repository contracts.
* **Application layer** - Contains the REST web API and the commands and queries that can be triggered through it. Uses domain entities from the domain layer and the repository implementations from the infrastructure layer (through Dependency Injection).
* **Infrastructure layer** - Contains repository implementations and the Data Access Objects (DAO) they use to read and write data to the database.

## Command and Query Separation (CQRS) and Event Sourcing

This project uses a simplified [CQRS pattern](https://martinfowler.com/bliki/CQRS.html) where read operation (*queries*) and write operations (*commands*) are modeled separately. 
This separation makes it possible to use constructs that are optimized for the type of operation being performed, for example by using a database structure that is optimized for read performance when executing queries, but not having to use it when executing commands.

CQRS fits well with [event sourcing](https://martinfowler.com/eaaDev/EventSourcing.html) which this project also uses.
With event sourcing all changes in the system are captured as a sequence of *events* and the state of a domain entity (i.e. an *Account*) is derived from these events.
This is unlike in a pure CRUD (Create, Retrieve, Update, Delete) system where only the last state is recorded, and we have no knowledge about the sequence of changes that led to that state.

![cqrs and event sourcing architecture](https://github.com/santosleijon/frugal-fennec-expense-manager/blob/main/docs/cqrs_es_architecture.png?raw=true)
