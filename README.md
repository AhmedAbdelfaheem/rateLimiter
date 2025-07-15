# Rate Limiter System Design

This project is a Java Spring Boot application for designing and implementing a rate limiter from scratch, inspired by
concepts from *System Design Interview Vol. 1*. The goal is to explore and compare multiple rate limiting algorithms,
providing a hands-on understanding of their trade-offs and real-world applications.

## Features

- Manual implementation of various rate limiting algorithms:
    - Fixed Window
    - Sliding Window
    - Token Bucket
    - Leaky Bucket
- Modular and extensible design for easy addition of new algorithms
- RESTful API endpoints for testing and demonstration
- Configurable rate limits and algorithm selection

## Getting Started

### Prerequisites

- Java 21
- Gradle

### Running the Application

```bash
./gradlew bootRun
