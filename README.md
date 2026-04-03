# Shopwave Enterprise Application 🚀



## 👤 Author Information
- **Name:** Antnhe Debebe
- **Student ID:** ATE/3036/14
- **Course:** SE 4801 – Enterprise Application Development (Assignment 1)

---

## 🛠️ Technology Stack
- **Java 21**
- **Spring Boot 3.5+** (Web, Data JPA, Validation)
- **H2 in-memory Database** (Runtime execution)
- **PostgreSQL via Testcontainers** (Advanced Isolation Testing)
- **Lombok** (Boilerplate Reduction)
- **Mockito & JUnit 5** (Testing Frameworks)

---

## 🚀 How to Build

Navigate cleanly to the root of the application framework (`shopwave-starter` directory). You can compile your application natively using the integrated Maven Wrapper. 

```bash
cd shopwave-starter

# To clean past artifacts and compile Java source files:
./mvnw clean compile
```

---

## ⚙️ How to Run

Because this is a Spring Boot application featuring an embedded server, you can instantiate the development server gracefully using standard Spring Boot run configurations.

```bash
cd shopwave-starter

# Spin up the application locally
./mvnw spring-boot:run
```
Once the banner terminates, your application will inherently bind to `http://localhost:8080/`. You can begin testing your Endpoints (like `GET /api/products`) using Postman, Thunder Client, or CURL.

---

## 🧪 How to Run Tests

This application possesses a sophisticated test suite executing Mockito (Unit Layer mocks) and Testcontainers (Dockerized PostgreSQL Integrations).

To automatically trigger the suite leveraging Maven, execute:
```bash
cd shopwave-starter

./mvnw test
```

> [!NOTE] 
> **Handling Testcontainers without Docker**
> The Database Integration tests (`ProductRepositoryTest`) strictly require a Docker Daemon (e.g. Docker Desktop or Colima) executing live on your machine. 
> To prevent your environment from catastrophically failing if Docker isn't globally installed, the `@Testcontainers(disabledWithoutDocker = true)` annotation has been engineered. If Docker is not found, the Database test safely skips gracefully, presenting a clean `BUILD SUCCESS` checkmark!

---

## 📚 API Endpoints Implemented

| HTTP Method | Endpoint | Description |
| :--- | :--- | :--- |
| `POST` | `/api/products` | Persists a validated Product entity |
| `GET` | `/api/products` | Fetches a paginated compilation of all Products |
| `GET` | `/api/products/{id}` | Fetches a definitive Product or throws `404 Not Found` |
| `GET` | `/api/products/search` | Dynamic keyword and max price filtering |
| `PATCH`| `/api/products/{id}/stock` | Modifies raw stock values synchronously via defined Delta ranges |
