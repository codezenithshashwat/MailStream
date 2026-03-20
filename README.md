# MailStream

A high-throughput, fault-tolerant email delivery platform built on an event-driven architecture. MailStream decouples email submission from delivery — requests are accepted instantly, queued durably, and processed asynchronously at scale.

---

## What it does

Most applications bolt email sending onto the request lifecycle — your API waits for the SMTP call to finish before responding. This works until it doesn't: one slow provider, one burst of signups, one retry loop, and your entire service is blocked.

MailStream takes a different approach. Your application publishes an email event and moves on. A dedicated worker pool consumes from the queue, enforces per-tenant rate limits, checks suppression lists, and delivers — all without ever touching your critical path.

---

## Architecture

```
 Client / API
     │
     ▼
 Rate Limiter ──── Redis ────── Suppression Cache
     │                          Template Cache
     ▼
 Kafka Topic
 (email.send)
     │
     ├── Worker 1 ──┐
     ├── Worker 2 ──┼──► SMTP (Mailpit in dev / provider in prod)
     └── Worker N ──┘
```

| Layer | Technology | Role |
|---|---|---|
| Message broker | Apache Kafka 3.7 | Durable queue, fan-out, replay |
| Cache / rate limiter | Redis 7 | Sub-millisecond counters, suppression lists |
| Application | Spring Boot 3 | REST API + Kafka producer |
| Mail catcher (dev) | Mailpit | Safe SMTP sink with web UI |
| Build tool | Maven (wrapper) | No global install required |
| Infrastructure | Docker Compose | One-command local environment |

---

## Getting started

### Prerequisites

- [Docker Desktop](https://www.docker.com/products/docker-desktop/) — nothing else needs to be installed globally

### 1. Clone and start the infrastructure

```bash
git clone https://github.com/codezenithshashwat/mailstream.git
cd mailstream

docker-compose up -d
```

This pulls and starts three services:

| Service | What it is | Local address |
|---|---|---|
| Redis | Rate limiter + cache | `localhost:6379` |
| Kafka | Message broker | `localhost:9092` |
| Mailpit | Fake SMTP + web UI | `http://localhost:8025` |

### 2. Start the application

```bash
cd backend
./mvnw spring-boot:run          # Linux / Mac
mvnw.cmd spring-boot:run        # Windows
```

The API starts on `http://localhost:8080`.

### 3. Open Mailpit

Navigate to [http://localhost:8025](http://localhost:8025) to see all outbound emails caught during development. No emails ever reach real inboxes.

---

## Project structure

```
mailstream/
├── docker-compose.yml          # Full local dev environment
└── backend/                    # Spring Boot application
    ├── pom.xml                 # Dependencies and build config
    ├── mvnw / mvnw.cmd         # Maven wrapper (no global install)
    └── src/
        ├── main/
        │   ├── java/com/example/mailstream/
        │   │   └── MailstreamApplication.java
        │   └── resources/
        │       └── application.properties
        └── test/
            └── java/com/example/mailstream/
                └── MailstreamApplicationTests.java
```

---

## Infrastructure detail

### Kafka — the backbone

Emails are published as events to the `email.send` topic. Kafka retains every message durably — if a worker crashes mid-delivery, it picks up exactly where it left off on restart. The consumer group model means you can scale workers horizontally by simply running more instances; Kafka distributes partitions between them automatically.

Running in **KRaft mode** — no Zookeeper dependency.

### Redis — rate limiting and cache

Two jobs, one service.

**Rate limiting** — before a job enters Kafka, Redis checks the tenant's rolling counter. The check-and-increment is a single atomic Lua script, so concurrent workers can never race past the limit. Counters expire automatically with the window.

**Caching** — suppression lists, email templates, and tenant config are cached with TTLs between 5 minutes and 24 hours. Workers read from cache first; a miss falls through to the database and repopulates the key.

### Mailpit — safe development

Every email your workers send during development lands in Mailpit's inbox rather than a real address. The web UI at `:8025` shows full headers, HTML rendering, and raw source. No SendGrid account, no test email allowlist, no accidental spam.

---

## Configuration

`backend/src/main/resources/application.properties`

```properties
# Server
server.port=8080

# Kafka
spring.kafka.bootstrap-servers=localhost:9092

# Redis
spring.data.redis.host=localhost
spring.data.redis.port=6379
