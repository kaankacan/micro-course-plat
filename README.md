# Micro Course Platform 
Micro Course Platform is a microservices-based e-learning backend built with Spring Boot. It supports user registration, course management, purchase flow, and distributed monitoring/logging.


Clone and spin it up:

```bash
git clone https://github.com/kaankacan/micro-course-plat
cd micro-course-plat
docker compose up --build
```

---

### 📊 Monitoring & Tracing Dashboards

| Tool | URL |
| ---- | --- |
| **Zipkin** | http://localhost:9411 |
| **Prometheus** | http://localhost:9090 |
| **Grafana** | http://localhost:3000  (admin / admin) |
| **Kibana (ELK)** | http://localhost:5601 |

---

#### 🪵 Logging Pipeline

| Layer | Purpose |
| ----- | ------- |
| **SLF4J** | Logging API (what your code calls) |
| **Logback** | Log producer inside the application |
| **Logstash** | Takes and transforms logs, ships them to Elasticsearch |
| **Elasticsearch** | Log data store |
| **Kibana** | UI for visualising and querying logs |

#### 📈 Monitoring & Tracing

| Layer | Purpose |
| ----- | ------- |
| **Micrometer** | Emits application metrics (routes data to Prometheus & Zipkin) |
| **Prometheus** | Scrapes / aggregates metrics (HTTP counts, latencies…) |
| **Grafana** | Visualises Prometheus metrics via dashboards |
| **Zipkin** | Distributed tracing (tracks traceId / spanId across services) |

#### 📬 Service-to-Service Communication
- **Feign Client** → Declarative HTTP client between microservices  
  - 'purchase-service` fetches course info from `course-service`

#### 📣 Event-Driven Architecture
- **Apache Kafka** → Asynchronous messaging backbone  
  - `user-service` emits “user-registered” events  
  - `course-service` emits “course-created” events  
  - `purchase-service` emits “purchase-created” events  
  - `notification-service` consumes them all

#### ⚙️ Config & Discovery
- **Spring Cloud Config** → Centralised configuration in `config-repo`
- **Eureka Discovery Server** → Service registration & lookup

#### 🛡️ Security
- **JWT** → Token-based authentication across all services  
  - Roles: `ADMIN`, `USER`

---

### 🔁 REST Endpoints

All requests flow through the **API Gateway** (`http://localhost:8080`).  
Protected routes require the header **`Authorization: Bearer <JWT>`**.

#### 🧑‍💻 user-service
| Method | Path |
| ------ | ---- |
| `POST` | `/api/users/register` |
| `POST` | `/api/users/login` |
| `GET`  | `/api/users/me` |

#### 🎓 course-service
| Method | Path |
| ------ | ---- |
| `POST`   | `/api/courses` |
| `GET`    | `/api/courses/all` |
| `PUT`    | `/api/courses/{id}` |
| `DELETE` | `/api/courses/{id}` |

#### 💳 purchase-service
| Method | Path |
| ------ | ---- |
| `POST` | `/api/purchases` |
| `GET`  | `/api/purchases/user` |

#### 📣 notification-service
- No external REST API — listens exclusively to **Kafka events**.

---

### 🔐 Sign-Up / Login / JWT Workflow

#### 👤 Register a USER (receives JWT)

```http
POST /api/users/register
Content-Type: application/json

{
  "email": "user@kaan.com",
  "password": "123456",
  "fullName": "kaanuser"
}
```

#### 👑 Register an ADMIN

```http
POST /api/users/register
Content-Type: application/json

{
  "email": "admin@kaan.com",
  "password": "123456",
  "fullName": "kaanadmin"
}
```

Grant `ADMIN` role manually:

```bash
docker exec -e PGPASSWORD=123456 postgres   psql -U postgres -d micro_course_userdb   -c "UPDATE users SET role = 'ADMIN' WHERE id = 2;"
```

#### 🔑 Login (retrieve JWT)

```http
POST /api/users/login
Content-Type: application/json

{
  "email": "admin@kaan.com",
  "password": "123456"
}
```

---

### 📚 Course Operations

#### ✅ Create a Course (requires **ADMIN** JWT)

```http
POST /api/courses
Authorization: Bearer <ADMIN-JWT>
Content-Type: application/json

{
  "title": "Spring Boot Microservices",
  "description": "Learn how to build scalable microservices with Spring Boot.",
  "price": 49.99,
  "instructor": "Kaan Kaçan"
}
```

#### 📖 List All Courses

```http
GET /api/courses/all
```

---

### 🛒 Purchase Operations

#### 💰 Purchase a Course (requires **USER** JWT)

```http
POST /api/purchases
Authorization: Bearer <USER-JWT>
Content-Type: application/json

{
  "courseId": 1
}
```

#### 🧾 My Purchases (requires **USER** JWT)

```http
GET /api/purchases/user
Authorization: Bearer <USER-JWT>
```
