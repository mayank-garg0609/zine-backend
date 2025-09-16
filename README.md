# Zine Backend

## Overview
Zine Backend is a Spring Boot-based REST API server for managing Zine Robotics' platform. It provides endpoints for user management, blog posts, events, rooms, tasks, and more. The backend is designed for scalability, security, and ease of integration with frontend clients.

## Features
- User authentication & authorization
- Blog and event management
- Room and member management
- Task assignment and tracking
- Media uploads
- Email verification and password reset
- Real-time notifications (WebSocket)

## Technology Stack
- Java 18
- Spring Boot
- Maven
- Docker
- Cloudinary (media uploads)
- Firebase (admin integration)
- MySQL (or compatible RDBMS)

## Database Design

## Database Design
[View ERD Diagram](https://dbdiagram.io/d/Zine-Backend-68387f8fbd74709cb71ee871)
*Refer to the ERD diagram for entity relationships and database structure.*

## API Reference
[View API Reference](https://docs.google.com/document/d/1-0nLUxVE3ZETlcbpRUza0RWILYV4oVlg7wahennnLjY/edit?tab=t.0)
Detailed documentation for all API endpoints, including HTTP methods, request/response formats, and models.


## Setup & Installation

## Setup & Installation
1. **Clone the repository:**
	```sh
	git clone https://github.com/zine-robotics/zine-backend.git
	cd zine-backend
	```
 
2. **Configure environment:**
	```sh
	- Update `src/main/resources/application.properties` with your DB and service credentials.
	- Place your Firebase admin JSON in `src/main/resources/zine-firebase-admin.json`.
	```

3. **Build the project:**
	```sh
	./mvnw clean package
	```

4. **Run locally:**
	```sh
	Open `ZineApplication.java` and run the `main` method directly from your IDE
	```

## Docker
Build and run the backend using Docker:
```sh
docker build -t zine-backend .
docker run -p 8080:8080 --env ENVIRONMENT=production zine-backend
```
