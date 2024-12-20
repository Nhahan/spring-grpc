# Spring gRPC

This project's main goal is to provide a boilerplate for integrating **Spring Boot version 3.x**, **gRPC version 1.68.x**, and **Protocol Buffers version 4.x**. Additionally, it includes a performance comparison between gRPC and REST.  

## Prerequisites

![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white): Required to build and run the project modules using Docker Compose.

## Modules

- **client**: A module that sends requests to both `rest-server` and `grpc-server` to compare performance. It exposes three endpoints:
    - **`/grpc`**: Sends a request to the gRPC server.
    - **`/rest`**: Sends a request to the REST server.
    - **`/test`**: Runs a performance test by sending parallel requests to both the REST and gRPC servers, and returns the time taken for each.

- **rest-server**: A Spring Boot application providing REST endpoints. It processes requests from the `client` module and operates on an internal network without exposing ports to the host.

- **grpc-server**: A Spring Boot gRPC server that handles requests from the `client` using protocol buffers defined in the `proto` module. It operates on an internal network without exposing ports to the host.

- **proto**: Contains protocol buffer definitions shared between modules for consistent data structures across gRPC services.

## Usage

Once the `client` service is running, you can access the following endpoints:

| Endpoint              | URL                                          | Description                                                      |
|-----------------------|----------------------------------------------|------------------------------------------------------------------|
| **`/grpc`**           | `http://localhost:8080/grpc`                | Sends a request to `grpc-server`.                                |
| **`/rest`**           | `http://localhost:8080/rest`                | Sends a request to `rest-server`.                                |
| **`/test`**           | `http://localhost:8080/test?requestCount=1000` | Sends `requestCount` requests to compare REST and gRPC performance. |

## Setup and Execution

1. Clone the repository:

   ```bash
   git clone https://github.com/Nhahan/spring-grpc.git
   cd spring-grpc
    ```

2. Run the project using Docker Compose:

   ```bash
   docker-compose up -d
    ```

## Performance Comparison

The table below compares gRPC and REST speeds for different `requestCount` values.

| requestCount | gRPC Duration (s) | REST Duration (s) |
|--------------|-------------------|-------------------|
| 1,000        | 0.737             | 1.121             |
| 10,000       | 5.524             | 8.225             |
| 100,000      | 51.341            | 79.357            |
| 1,000,000    | 460.791           | 740.651           |
| 10,000,000   | 4,599.891         | 8,955.372         |
| 100,000,000  | 41,359.793        | 82,250.110        |

> **Note**: Measured on an M1 Pro base model.

## License

This project is licensed under the CC0 1.0 Universal (CC0 1.0) Public Domain Dedication. See the [LICENSE](./LICENSE) file for details.
