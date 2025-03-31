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

1. Copy the example environment files for each service:

```bash
cp AuthService/.env.example AuthService/.env
cp AppointmentService/.env.example AppointmentService/.env
cp NotificationService/.env.example NotificationService/.env
```

2. Fill in your environment variables in each `.env` file before running the services.

### Auth Service
```bash
JWT_SECRET_KEY=your_jwt_secret_key
DB_HOST=mysql
DB_PORT=3306
DB_USER=your_db_user
DB_PASSWORD=your_db_password
DB_NAME=auth_service
```

### Appointment Service
```bash
SPRING_PROFILES_ACTIVE=docker  # Use 'local' for local development
```

### Notification Service
```bash
RMQP_URL=amqp://guest:guest@rabbitmq:5672/
QUEUE_NAME=appointment_notifications
SERVER_MAIL=your_gmail_address
APP_PASSWORD=your_gmail_app_password  # Generate from Google Account settings
```

### MySQL
```bash
MYSQL_ROOT_PASSWORD=your_mysql_root_password
```

> **Note**: For the Gmail APP_PASSWORD, you need to:
> 1. Enable 2-Step Verification in your Google Account
> 2. Go to Security → App passwords
> 3. Generate a new app password for mail

## License

ISC