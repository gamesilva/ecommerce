# Microservices Backend with Spring Boot

This project is a microservices-based backend system developed using **Spring Boot** and various modern technologies. The primary goal of this project is to explore and learn how to architect and implement a microservice ecosystem from scratch.

## 🔧 Tech Stack

- **Java 17+**
- **Spring Boot**
- **Spring Cloud** – Configuration, Discovery, Gateway
- **Apache Kafka** – Asynchronous communication between services
- **MongoDB** – NoSQL database (used in some services)
- **PostgreSQL** – Relational database (used in other services)
- **Keycloak** – Identity and access management
- **Zipkin** – Distributed tracing
- **Docker & Docker Compose** – Containerization and orchestration
- **Lombok** – Boilerplate reduction

## 📐 Architecture Overview

This project follows a **microservices architecture**, where each service is responsible for a single business capability. Some of the services included:

- `api-gateway`: Routes and secures traffic to internal services.
- `customer-service`: Manages customer data
- `product-service`: Manages product catalog and information.
- `order-service`: Handles order processing and communication via Kafka.
- `payment-service`: Manages payments service.
- `notification-service`: Sends notifications triggered by other services.

Each service is autonomous, loosely coupled, and communicates via **REST** and **Kafka**.

## 🔐 Security

- Integrated with **Keycloak** for OAuth2 authentication and role-based access control.
- Secured endpoints using **Spring Security**.
- Token validation through Gateway.

## 📦 Service Communication

- **REST**: Used for synchronous communication (e.g., querying data).
- **Kafka**: Used for asynchronous messaging and event-driven behavior.

## 📊 Observability

- **Zipkin** is integrated into all services to provide tracing and performance insights.
- Logs and traces help identify performance bottlenecks and error flows across services.
