# Fabrick Interview Assignment

This repository contains the implementation for the Fabrick coding interview tasks.
The project is structured as a **Maven Multi-Module** application containing two microservices.

## 🏗 Project Structure

* **`asteroids-service` (Task 1):** REST API to analyze asteroid paths across the Solar System using NASA NeoWs API.
* **`weather-service` (Task 2):** REST API to find weather stations and airports using the Aviation Weather Center API.

## 🛠 Tech Stack & Features

* **Java 21**
* **Spring Boot 3** (WebFlux / Reactive Stack) [Bonus Point]
* **Maven** (Multi-module build)
* **Caffeine Cache** (Local caching layer) [Bonus Point]
* **JUnit 5 & Mockito** (Unit Testing)

---

## 🚀 How to Run

### Prerequisites
* Java JDK 21
* Maven

### Build the Project
From the root directory, run:
```bash
mvn clean install
```
## ▶️ Run Task 1: Asteroids Service
### This service runs on port 8080.
```bash
cd asteroids-service
mvn spring-boot:run
```
### Test the API:
```bash
GET http://localhost:8080/api/fabrick/v1.0/asteroids/3542519/paths?fromDate=2010-01-01&toDate=2014-01-01
```

## ▶️ Run Task 2: Weather Service
### This service runs on port 8081 to avoid conflicts.
```bash
cd weather-service
mvn spring-boot:run
```
### Test the API:
```bash
# Find stations near an airport (e.g., KDEN - Denver)
GET http://localhost:8081/api/fabrick/v1.0/airports/KDEN/stations?closestBy=0.5

# Find airports near a station
GET http://localhost:8081/api/fabrick/v1.0/stations/KDEN/airports?closestBy=0.5
```