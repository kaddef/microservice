# Appointment System

A microservices-based appointment management system built with multiple technologies and services.

## Architecture

The system consists of three main microservices:
- **Auth Service** (Node.js/Express) - Handles user authentication and authorization
- **Appointment Service** (Spring Boot) - Manages appointment scheduling and business logic
- **Notification Service** (Go) - Handles email notifications

## Tech Stack

### Backend Services
- **Auth Service**: Node.js, Express, Sequelize, JWT
- **Appointment Service**: Java 17, Spring Boot 3.4, JPA
- **Notification Service**: Go 1.24

### Infrastructure
- **Database**: MySQL 8.0
- **Message Broker**: RabbitMQ
- **Containerization**: Docker
- **API Authentication**: JWT

## Getting Started

1. Clone the repository
2. Make sure you have Docker and Docker Compose installed
3. Run the following command:
```bash
docker-compose up
```

## Service Ports
- Auth Service: 8000
- Appointment Service: 8080
- Notification Service: 9090
- MySQL: 3307
- RabbitMQ: 5672 (Management: 15672)

## Environment Variables

Required environment variables are configured in the docker-compose.yaml file. For local development, refer to the individual service documentation.

## License

ISC