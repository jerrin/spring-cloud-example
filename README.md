# Spring Cloud Example

A comprehensive microservices architecture demonstration using Spring Cloud and Spring Boot. This project showcases service discovery, configuration management, API gateway, and reactive programming with multiple service patterns.

## Project Structure

```
spring-cloud-example/
├── eureka-server/          # Service Registry
├── config-server/          # Centralized Configuration Server
├── gateway/                # API Gateway (Cloud Gateway)
├── mvc-service/            # Traditional Spring MVC Service
├── flux-service/           # Reactive WebFlux Service
├── socket-stream/          # GraphQL Real-time Streaming Service with Kafka
└── pom.xml                 # Parent POM (Multi-module Maven project)
```

## Architecture Overview

```
                    ┌──────────────────┐
                    │  Config Server   │
                    │   (Port 8001)    │
                    └──────────────────┘
                             ▲
                             │ (pulls config)
                             │
    ┌────────────────────────┼────────────────────────────────┐
    │                        │                                │
┌─────────────────┐  ┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│   Gateway       │  │  MVC Service     │  │  Flux Service    │  │ Socket Stream    │
│  (Port 9000)    │  │   (Port 9001)    │  │   (Port 9002)    │  │ GraphQL (7000)   │
└─────────────────┘  └──────────────────┘  └──────────────────┘  └──────────────────┘
    │                        │                        │                     │
    └────────────────────────┼────────────────────────┼─────────────────────┘
                             │ (registers/discovers) │
                             │                       │ (Kafka messages)
                             ▼                       ▼
                    ┌──────────────────┐    ┌──────────────────┐
                    │  Eureka Server   │    │  Kafka Broker    │
                    │   (Port 8000)    │    │   (Port 7007)    │
                    └──────────────────┘    └──────────────────┘
```

## Services

### 1. **Eureka Server** (Service Registry & Discovery)
**Port:** 8000

The central service registry where all microservices register themselves and discover other services.

**Features:**
- Service registration and discovery
- Health check monitoring
- Load balancing support

**Endpoints:**
| Endpoint | Description |
|----------|-------------|
| `http://localhost:8000/` | Eureka Dashboard |
| `http://localhost:8000/eureka/apps` | List all registered services |

---

### 2. **Config Server** (Centralized Configuration)
**Port:** 8001

Centralized configuration management for all microservices. Configurations are stored locally in `classpath:/config`.

**Features:**
- Centralized configuration management
- Environment-specific configurations
- Dynamic configuration refresh
- Native profile for local file-based configuration

**Configuration Files:**
- `config/application.yaml` - Common properties for all services (Eureka, health checks)
- `config/gateway.yaml` - Gateway route definitions and settings
- `config/mvc-service.yaml` - MVC Service port and context configuration
- `config/flux-service.yaml` - Flux Service port and base path configuration
- `config/socket-stream.yaml` - Socket Stream Kafka and GraphQL configuration

**Endpoints:**
| Endpoint | Description |
|----------|-------------|
| `http://localhost:8001/gateway/default` | Get gateway configuration |
| `http://localhost:8001/mvc-service/default` | Get MVC service configuration |
| `http://localhost:8001/flux-service/default` | Get Flux service configuration |
| `http://localhost:8001/socket-stream/default` | Get Socket Stream configuration |

**How Config Server Works:**
1. Each microservice declares its application name and imports configuration from Config Server during startup
2. The Config Server looks for files matching the pattern: `{application-name}.yaml`
3. Configurations are loaded and applied to the service before the application context fully initializes
4. Common properties from `application.yaml` are inherited by all services

---

### 3. **API Gateway** (Spring Cloud Gateway)
**Port:** 9000

Central entry point for all client requests. Routes traffic to backend services based on configured predicates.

**Features:**
- Request routing based on path predicates
- Load balancing via Eureka
- Filter support (authentication, logging, etc.)

**Routing Configuration:**
The gateway routes requests to backend services using service names (discovered via Eureka):
- `/mvc-service/**` routes to MVC Service
- `/flux-service/**` routes to Flux Service
- `/socket-stream/**` routes to Socket Stream Service

**Endpoints (Gateway Routes):**
| Endpoint | Target Service | Description |
|----------|----------------|-------------|
| `http://localhost:9000/mvc-service/get` | MVC Service | Get MVC service message |
| `http://localhost:9000/flux-service/users` | Flux Service | Get user stream (Server-Sent Events) |
| `http://localhost:9000/socket-stream/graphiql` | Socket Stream | GraphQL IDE |

---

### 4. **MVC Service** (Traditional Spring MVC)
**Port:** 9001

A traditional Spring MVC microservice demonstrating conventional request-response patterns.

**Local Access Endpoints:**
| Endpoint | Method | Description | Response |
|----------|--------|-------------|----------|
| `http://localhost:9001/mvc-service/get` | GET | Get a message from MVC service | `"Hello from MVC Service!"` |

**Via Gateway:**
| Endpoint | Method | Description |
|----------|--------|-------------|
| `http://localhost:9000/mvc-service/get` | GET | Get message (routed through gateway) |

---

### 5. **Flux Service** (Reactive WebFlux)
**Port:** 9002

A reactive microservice using Spring WebFlux for handling asynchronous, non-blocking requests with Reactor Flux.

**Local Access Endpoints:**
| Endpoint | Method | Description | Response |
|----------|--------|-------------|----------|
| `http://localhost:9002/flux-service/users` | GET | Stream users (Server-Sent Events) | Stream of User objects |

**Via Gateway:**
| Endpoint | Method | Description |
|----------|--------|-------------|
| `http://localhost:9000/flux-service/users` | GET | Stream users (routed through gateway) |

**Response Format (Server-Sent Events):**
One user per second in JSON format until 10 users are streamed.

---

### 6. **Socket Stream Service** (GraphQL Real-time Streaming)
**Port:** 7000

A real-time message streaming service that combines GraphQL subscriptions with Kafka messaging. This service demonstrates event-driven communication patterns using WebSocket connections for live data updates.

**Features:**
- GraphQL API for queries, mutations, and subscriptions
- Real-time message streaming via WebSocket
- Kafka integration for message persistence and distribution
- Interactive GraphiQL UI for testing
- Automatic topic creation and consumer group management

**Key Endpoints:**
| Endpoint | Type | Purpose |
|----------|------|---------|
| `http://localhost:7000/graphiql` | HTTP GET | Interactive GraphQL IDE |
| `http://localhost:7000/graphql` | HTTP POST | GraphQL mutations and queries |
| `ws://localhost:7000/graphql` | WebSocket | GraphQL subscriptions (real-time) |

**Configuration:** `config/socket-stream.yaml`
- Server port: 7000
- Kafka broker: localhost:7007
- Kafka topic: stream-topic
- Consumer group: stream-group
- GraphQL WebSocket enabled

**How to Run Kafka (Required for Socket Stream):**

Socket Stream requires Kafka and Kafka UI containers running. Use docker-compose to start them from the socket-stream directory:

| Step | Command | Purpose |
|------|---------|---------|
| Navigate to socket-stream | `cd socket-stream` | Move to service directory |
| Start Kafka and UI | `docker-compose up -d` | Launch Kafka broker and management UI |
| View Kafka UI | Open `http://localhost:8080` | Monitor topics and messages |
| Stop services | `docker-compose down` | Stop containers |
| Stop and clean data | `docker-compose down -v` | Stop containers and remove volumes |

**Testing GraphQL Mutations and Subscriptions:**

The Socket Stream service exposes two main GraphQL operations:

**1. Mutation - sendMessage**

Purpose: Send a message to the stream, which will be published to Kafka and distributed to all subscribers.

How to test:
- Open GraphiQL at http://localhost:7000/graphiql in a browser window
- Paste the mutation query in the editor
- Click the Play button to execute

Query structure:
```
mutation {
  sendMessage(input: { content: "Hello from GraphQL" }) {
    id
    content
  }
}
```

Expected response: Returns the message ID and content that was published.

**2. Subscription - messageStream**

Purpose: Subscribe to receive messages in real-time as they are published to Kafka.

How to test:
- Open GraphiQL at http://localhost:7000/graphiql in a **different browser window or tab**
- Paste the subscription query in the editor
- Click the Play button to activate the subscription
- The subscription will wait for messages and display them as they arrive

Query structure:
```
subscription {
  messageStream {
    id
    content
  }
}
```

Expected behavior: Messages will stream in real-time, arriving one per second. Keep this window open while sending mutations from another window.

**Complete Testing Workflow:**

1. Start Kafka and Kafka UI from socket-stream directory:
   ```
   cd socket-stream
   docker-compose up -d
   ```

2. Start Socket Stream service (mvn spring-boot:run from socket-stream directory)

3. Open two browser windows/tabs:
   - Window A: http://localhost:7000/graphiql
   - Window B: http://localhost:7000/graphiql

4. In Window B (subscription listener):
   - Paste the messageStream subscription query
   - Click Play to activate listening
   - Keep this window open

5. In Window A (mutation sender):
   - Paste the sendMessage mutation query
   - Click Play to send the message
   - Watch Window B receive the message in real-time

6. You can send multiple mutations from Window A while subscription is active in Window B

**Architecture Details:**

The service uses:
- **GraphQL Controller**: Handles mutation and subscription requests
- **Kafka Producer**: Publishes messages to Kafka topic
- **Kafka Consumer**: Consumes messages from Kafka topic
- **Reactive Stream**: Uses Project Reactor to stream messages to subscribers
- **WebSocket Bridge**: Spring GraphQL WebSocket support for subscriptions

For detailed configuration options and advanced usage, see the `socket-stream` directory documentation.

---

## Configuration Management Details

### How Config Server is Used

1. **Service Bootstrap:**
   - Each service (except Config Server and Eureka) imports configuration from Config Server
   - Configuration is fetched before the application context is fully loaded

2. **Configuration Import:**
   - Each service declares its application name and imports config from Config Server
   - Configuration location: `spring.config.import: configserver:http://localhost:8001`

3. **Config File Naming Convention:**
   - Config Server looks for files: `{application-name}.yaml`
   - Example: For `mvc-service`, it looks for `mvc-service.yaml`

4. **Configuration Properties:**
   - **Server Port:** Each service has a dedicated port
   - **Eureka Registration:** All services register with the Eureka Server
   - **Health Check Intervals:** Configurable lease renewal and expiration

### Configuration Files Breakdown

| Service | Config File | Port | Features |
|---------|-------------|------|----------|
| Gateway | `gateway.yaml` | 9000 | Route definitions, Eureka registration |
| MVC Service | `mvc-service.yaml` | 9001 | Context path, Eureka registration |
| Flux Service | `flux-service.yaml` | 9002 | WebFlux base path, Eureka registration |
| Socket Stream | `socket-stream.yaml` | 7000 | Kafka integration, GraphQL, WebSocket, Eureka registration |

### Common Properties (Inherited by All Services)

All services inherit common properties from `config/application.yaml`:
- **Eureka Server URL:** http://localhost:8000/eureka/
- **Service Hostname:** localhost
- **Health Check Intervals:** Heartbeat every 5 seconds, cleanup after 10 seconds
- **Actuator Endpoints:** Health, info, and metrics endpoints enabled

---

## Getting Started

### Prerequisites

- Java 25+
- Maven 3.6+
- Git
- Docker and Docker Compose (for Socket Stream and Kafka)

### Starting the Services

**Step 0: Start Kafka (for Socket Stream service)**

Before starting services, start Kafka and Kafka UI using Docker Compose from the socket-stream directory:

```bash
cd socket-stream
docker-compose up -d
cd ..
```

Wait for containers to start (check with `docker-compose ps` in socket-stream directory).

**Step 1-6: Start Spring Cloud Services**

Start services in this order:

```bash
# 1. Start Eureka Server (Service Registry)
cd eureka-server
mvn spring-boot:run

# 2. Start Config Server (Configuration Management)
cd config-server
mvn spring-boot:run

# 3. Start MVC Service
cd mvc-service
mvn spring-boot:run

# 4. Start Flux Service
cd flux-service
mvn spring-boot:run

# 5. Start Socket Stream Service (requires Kafka running)
cd socket-stream
mvn spring-boot:run

# 6. Start API Gateway
cd gateway
mvn spring-boot:run
```

### Alternative: Run All Services in IntelliJ

1. Create multiple run configurations:
   - Run → Edit Configurations
   - Click "+" → Spring Boot
   - Set main class and module for each service
   - Run each configuration

2. Add VM options for all configurations:
   - VM options: `--enable-native-access=ALL-UNNAMED`

---

## Testing the Services

### 1. Check Eureka Dashboard
```
http://localhost:8000/eureka/
```
You should see all 3 services registered (gateway, mvc-service, flux-service).

### 2. Test MVC Service Directly
```bash
curl http://localhost:9001/mvc-service/get
# Response: "Hello from MVC Service!"
```

### 3. Test MVC Service via Gateway
```bash
curl http://localhost:9000/mvc-service/get
# Response: "Hello from MVC Service!"
```

### 4. Test Flux Service (Server-Sent Events)
```bash
# Direct access
curl -N http://localhost:9002/flux-service/users

# Via Gateway
curl -N http://localhost:9000/flux-service/users

# Will stream 10 users, one every second
```

### 5. Test Socket Stream Service (GraphQL)

**Access GraphiQL UI:**
```
http://localhost:7000/graphiql
```

**Testing Mutations and Subscriptions:**

Open two browser windows with GraphiQL:

**Window A - Send Mutation:**
- Navigate to http://localhost:7000/graphiql
- Paste the sendMessage mutation
- Click Play to execute

**Window B - Subscribe to Messages:**
- Navigate to http://localhost:7000/graphiql in a different window
- Paste the messageStream subscription
- Click Play to start listening for messages
- Keep this window open

Then send mutations from Window A and watch messages appear in real-time in Window B.

### 6. Verify Config Server
```bash
curl http://localhost:8001/gateway/default
curl http://localhost:8001/mvc-service/default
curl http://localhost:8001/flux-service/default
curl http://localhost:8001/socket-stream/default
```

---

## Dependencies

### Parent POM (Spring Boot 4.0.2)
```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.0.2</version>
</parent>

<spring-cloud.version>2025.1.1</spring-cloud.version>
```

### Key Dependencies by Service

**Eureka Server:**
- `spring-cloud-starter-netflix-eureka-server`

**Config Server:**
- `spring-cloud-config-server`
- `spring-cloud-starter-netflix-eureka-client`

**Gateway:**
- `spring-cloud-starter-gateway-server-webflux`
- `spring-cloud-starter-netflix-eureka-client`
- `spring-cloud-starter-config`

**MVC Service:**
- `spring-boot-starter-web`
- `spring-cloud-starter-netflix-eureka-client`
- `spring-cloud-starter-config`

**Flux Service:**
- `spring-boot-starter-webflux`
- `spring-cloud-starter-netflix-eureka-client`
- `org.projectlombok:lombok:1.18.42`

**Socket Stream Service:**
- `spring-boot-starter-graphql`
- `spring-kafka`
- `io.projectreactor.kafka:reactor-kafka`
- `spring-cloud-starter-config`
- `spring-cloud-starter-netflix-eureka-client`
- `org.projectlombok:lombok:1.18.42`

---

## Troubleshooting

### Services Not Showing in Eureka
- Ensure Eureka Server is running first
- Check that `eureka.client.service-url.defaultZone` points to correct Eureka address
- Check service logs for registration errors

### Gateway Routes Not Working
- Verify Config Server is running and accessible
- Check gateway.yaml configuration is loaded correctly
- Ensure backend services are registered in Eureka
- Test services directly on their ports to isolate issues

### Config Server Not Loading Configuration
- Verify `classpath:/config` files exist
- Check that application names match config file names
- Ensure `spring.config.import: configserver:...` is set in each service

### SSE Stream Not Working in Flux Service
- Ensure Flux Service is running on port 9002
- Check that `curl -N` flag is used (disables buffering)
- Verify reactor dependencies are correct

### Socket Stream Service Issues

**Kafka Connection Refused:**
- Ensure Kafka is running: `cd socket-stream && docker-compose ps`
- Check Kafka port 7007 is accessible: `nc -zv localhost 7007`
- Restart Kafka: `docker-compose down && docker-compose up -d`

**GraphiQL Not Accessible:**
- Verify Socket Stream service is running on port 7000
- Check `spring.graphql.graphiql.enabled: true` in socket-stream.yaml
- Clear browser cache and refresh page

**Subscription Not Receiving Messages:**
- Ensure mutation was sent before subscribing
- Verify Kafka broker is running and healthy
- Check WebSocket connection in browser DevTools (Network tab)
- Make sure subscription is executed before sending mutations

**Messages Not Appearing in Real-time:**
- Confirm both windows have active connections (check browser console)
- Verify Kafka topic exists: Access Kafka UI at http://localhost:8080
- Check consumer group offset: `docker-compose exec -it kafka kafka-consumer-groups --bootstrap-server localhost:7007 --list`

---

## Project Technologies

- **Spring Boot:** 4.0.2
- **Spring Cloud:** 2025.1.1
- **Java:** 25
- **Maven:** Multi-module build
- **GraphQL:** Spring GraphQL with WebSocket subscriptions
- **Message Broker:** Apache Kafka with Docker Compose
- **Microservices Patterns:**
  - Service Discovery (Eureka)
  - Configuration Management (Config Server)
  - API Gateway (Spring Cloud Gateway)
  - Reactive Programming (Project Reactor)
  - Real-time Streaming (GraphQL Subscriptions)
  - Event-driven Architecture (Kafka)

---

## Key Learnings

1. **Service Discovery:** Eureka enables services to find each other dynamically
2. **Centralized Configuration:** Config Server eliminates the need for environment-specific deployments
3. **API Gateway Pattern:** Single entry point for all client requests with intelligent routing
4. **Reactive vs Traditional:** Compare MVC and WebFlux approaches side-by-side
5. **Spring Cloud Integration:** Seamless integration between different Spring Cloud components
6. **Real-time Streaming:** GraphQL subscriptions with WebSocket for live data updates
7. **Event-driven Architecture:** Kafka enables asynchronous, decoupled communication between services
8. **Container Orchestration:** Docker Compose simplifies running dependent services like Kafka

---

## License

This project is provided as-is for educational purposes.


