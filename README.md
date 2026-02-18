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
    ┌────────────────────────┼────────────────────────┐
    │                        │                        │
┌─────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│   Gateway       │  │  MVC Service     │  │  Flux Service    │
│  (Port 9000)    │  │   (Port 9001)    │  │   (Port 9002)    │
└─────────────────┘  └──────────────────┘  └──────────────────┘
    │                        │                        │
    └────────────────────────┼────────────────────────┘
                             │ (registers/discovers)
                             ▼
                    ┌──────────────────┐
                    │  Eureka Server   │
                    │   (Port 8000)    │
                    └──────────────────┘
```

## Services

### 1. **Eureka Server** (Service Registry & Discovery)
**Port:** 8000

The central service registry where all microservices register themselves and discover other services.

**Features:**
- Service registration and discovery
- Health check monitoring
- Load balancing support

**Configuration:** `eureka-server/src/main/resources/application.yaml`
```yaml
server.port: 8000
eureka.client.register-with-eureka: false
eureka.client.fetch-registry: false
```

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

**Configuration:** `config-server/src/main/resources/application.yaml`
```yaml
server.port: 8001
spring.profiles.active: native
spring.cloud.config.server.native.search-locations: classpath:/config
```

**Configuration Files:**
- `config/gateway.yaml` - Gateway configuration
- `config/mvc-service.yaml` - MVC Service configuration
- `config/flux-service.yaml` - Flux Service configuration

**Endpoints:**
| Endpoint | Description |
|----------|-------------|
| `http://localhost:8001/gateway/default` | Get gateway configuration |
| `http://localhost:8001/mvc-service/default` | Get MVC service configuration |
| `http://localhost:8001/flux-service/default` | Get Flux service configuration |

**How Config Server Works:**
1. Each microservice declares its application name in `application.yaml`
2. On startup, services import config from the Config Server:
   ```yaml
   spring.config.import: configserver:http://localhost:8001
   ```
3. The Config Server looks for files matching the pattern: `{application-name}.yaml`
4. Configurations are loaded and applied to the service before it fully starts

---

### 3. **API Gateway** (Spring Cloud Gateway)
**Port:** 9000

Central entry point for all client requests. Routes traffic to backend services based on configured predicates.

**Features:**
- Request routing based on path predicates
- Load balancing via Eureka
- Filter support (authentication, logging, etc.)

**Configuration:** `config/gateway.yaml`
```yaml
server.port: 9000
spring.cloud.gateway.routes:
  - id: mvc-service
    uri: lb://mvc-service
    predicates:
      - Path=/mvc-service/**
  - id: flux-service
    uri: lb://flux-service
    predicates:
      - Path=/flux-service/**
```

**Endpoints (Gateway Routes):**
| Endpoint | Target Service | Description |
|----------|----------------|-------------|
| `http://localhost:9000/mvc-service/get` | MVC Service | Get MVC service message |
| `http://localhost:9000/flux-service/users` | Flux Service | Get user stream (Server-Sent Events) |

---

### 4. **MVC Service** (Traditional Spring MVC)
**Port:** 9001

A traditional Spring MVC microservice demonstrating conventional request-response patterns.

**Configuration:** `config/mvc-service.yaml`
```yaml
server.port: 9001
server.servlet.context-path: /mvc-service
```

**Local Access Endpoints:**
| Endpoint | Method | Description | Response |
|----------|--------|-------------|----------|
| `http://localhost:9001/mvc-service/get` | GET | Get a message from MVC service | `"Hello from MVC Service!"` |

**Via Gateway:**
| Endpoint | Method | Description |
|----------|--------|-------------|
| `http://localhost:9000/mvc-service/get` | GET | Get message (routed through gateway) |

**Key Code:**
```java
@RestController
public class MvcService {
    @GetMapping("/get")
    public String getMessage() {
        return "Hello from MVC Service!";
    }
}
```

---

### 5. **Flux Service** (Reactive WebFlux)
**Port:** 9002

A reactive microservice using Spring WebFlux for handling asynchronous, non-blocking requests with Reactor Flux.

**Configuration:** `config/flux-service.yaml`
```yaml
server.port: 9002
spring.web-flux.base-path: /flux-service
```

**Local Access Endpoints:**
| Endpoint | Method | Description | Response |
|----------|--------|-------------|----------|
| `http://localhost:9002/flux-service/users` | GET | Stream users (Server-Sent Events) | Stream of User objects |

**Via Gateway:**
| Endpoint | Method | Description |
|----------|--------|-------------|
| `http://localhost:9000/flux-service/users` | GET | Stream users (routed through gateway) |

**Response Format (Server-Sent Events):**
```
event: 
data: {"id":"1","name":"User 1"}

event: 
data: {"id":"2","name":"User 2"}

... (continues every 1 second until 10 users)
```

---

## Configuration Management Details

### How Config Server is Used

1. **Service Bootstrap:**
   - Each service (except Config Server and Eureka) imports configuration from Config Server
   - Configuration is fetched before the application context is fully loaded

2. **Configuration Import:**
   ```yaml
   # application.yaml in each service
   spring:
     application:
       name: <service-name>
     config:
       import: configserver:http://localhost:8001
   ```

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

### Eureka Configuration in Each Service

All services register with Eureka for service discovery:

```yaml
eureka:
  client:
    service-url:
      defaultZone: http://localhost:8000/eureka/
  instance:
    hostname: localhost
    lease-renewal-interval-in-seconds: 5          # Heartbeat interval
    lease-expiration-duration-in-seconds: 10      # Cleanup timeout
```

---

## Getting Started

### Prerequisites

- Java 25+
- Maven 3.6+
- Git

### Starting the Services

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

# 5. Start API Gateway
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

### 5. Verify Config Server
```bash
curl http://localhost:8001/gateway/default
curl http://localhost:8001/mvc-service/default
curl http://localhost:8001/flux-service/default
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

---

## Project Technologies

- **Spring Boot:** 4.0.2
- **Spring Cloud:** 2025.1.1
- **Java:** 25
- **Maven:** Multi-module build
- **Microservices Patterns:**
  - Service Discovery (Eureka)
  - Configuration Management (Config Server)
  - API Gateway (Spring Cloud Gateway)
  - Reactive Programming (Project Reactor)

---

## Key Learnings

1. **Service Discovery:** Eureka enables services to find each other dynamically
2. **Centralized Configuration:** Config Server eliminates the need for environment-specific deployments
3. **API Gateway Pattern:** Single entry point for all client requests with intelligent routing
4. **Reactive vs Traditional:** Compare MVC and WebFlux approaches side-by-side
5. **Spring Cloud Integration:** Seamless integration between different Spring Cloud components

---

## License

This project is provided as-is for educational purposes.


