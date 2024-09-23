# MemPersistCacheSystem

A caching solution with options for in-memory or persistent storage, ensuring synchronization between both. This project includes functionality to measure and compare latency between storage types.

## Features

- **In-Memory Caching**: Fast data access using an in-memory cache.
- **Persistent Storage**: Data can be stored persistently in a relational database (e.g., MySQL).
- **Synchronization**: Ensures data consistency between in-memory cache and persistent storage.
- **Latency Measurement**: Compare read and write latencies between in-memory and persistent storage.
- **Custom Eviction Policies**: Implemented eviction strategies such as LRU (Least Recently Used) and TTL (Time-to-Live).
- **Metrics Monitoring**: Integration with Micrometer for performance tracking and monitoring.

## Technologies Used

- **Java**: Primary programming language.
- **Spring Boot**: Framework for building the application.
- **MySQL**: Persistent storage solution.
- **Micrometer**: For metrics collection and monitoring.
- **JUnit & Mockito**: For unit testing and mocking dependencies.

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven
- MySQL Database

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/chandrakanthrck/MemPersistCacheSystem.git
   cd MemPersistCacheSystem
   ```

2. Configure your `application.properties` for database connection:
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_db_name
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   ```

3. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## Usage

### API Endpoints

- **PUT** data into the cache:
  ```
  POST /cache/put?key={key}&value={value}
  ```

- **GET** data from the cache:
  ```
  GET /cache/get?key={key}
  ```

- **REMOVE** data from the cache:
  ```
  DELETE /cache/remove?key={key}
  ```

- **Metrics** monitoring:
  ```
  GET /actuator/prometheus
  ```

## Testing

Run unit tests using Maven:
```bash
mvn test
```

## Contributing

Contributions are welcome! Please open an issue or submit a pull request if you'd like to contribute.

## License

This project is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.