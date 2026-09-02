# Smart Complaint & Service Management Portal

A full-stack complaint management system built with Java Servlets, JSP, and JDBC
against an Oracle database — developed as a hands-on learning project
(Wipro TalentNext training) with every layer written and tested by hand,
without a framework, specifically to understand what frameworks like Spring
and Hibernate later automate.

## Features

- **Customer** — registration, login, file complaints (with file attachments),
  track status and view full history
- **Agent** — view assigned complaints, update status with remarks
- **Admin** — manage categories, assign agents to complaints, manage user roles
  (with last-admin protection)
- Role-based access control enforced via servlet filters and per-resource
  ownership checks
- Transactional status updates with a full audit trail (`status_history`)
- File attachments stored as BLOBs, with size limits and ownership-checked downloads

## Tech Stack

- Java 17 · Jakarta Servlets/JSP (Tomcat 10.1)
- JDBC · Oracle Database 21c (Docker)
- JUnit 5 + Mockito (unit tests) · Maven Failsafe (integration tests)
- BCrypt · JSTL

## Architecture

Layered: Servlet (controller) → Service (business logic) → DAO (persistence) → Oracle.
DAOs and Services are built against interfaces throughout, with Oracle-specific implementations — deliberately mirroring the shape Spring's dependency injection and repository pattern will later automate.

## Running Locally

1. `docker compose up -d` (starts Oracle XE — see `.env.example` for required vars)
2. Run schema scripts in `src/main/resources/schema/` in order (001–006), or use
   `scripts/reset-schema-and-data.sql` for a full rebuild
3. `mvn clean package`, deploy the resulting WAR to Tomcat 10.1
4. Optionally run `SeedTestData` (`mvn compile exec:java -Dexec.mainClass=...`)
   for realistic demo data — creates 1 admin, 2 agents, 3 customers, 3 categories,
   and 6 sample complaints, all with password `Password123`

## Testing & Quality

To run tests locally:

- `mvn test` — runs unit tests (Mockito-mocked).
- `mvn verify` — runs unit + integration tests against the live Oracle instance (Docker container must be running).

**Quality Metrics:**

- **Unit tests** (JUnit 5 + Mockito) — Service layer and `AuthenticationFilter` fully covered (100%); Servlet layer covered for three representative flows (authentication, ownership-based access control, multipart file upload).
- **Integration tests** (Maven Failsafe) — Full DAO layer verified against a live Oracle instance, including a dedicated test proving transactional rollback behavior.
- **Test coverage** (JaCoCo, merged unit + integration): 59% instruction / 53% branch coverage overall.
  - Core domain breakdown: Filter 100%, Exceptions 100%, Service 85%, DAO 85%, Model 81%.
  - Servlet coverage: 19% (see note below).
- **Load testing** (JMeter) — Login flow tested at 100 concurrent users: 100% pass rate, APDEX 1.000 (500ms toleration threshold).

### Known Coverage Gaps

- **Servlet layer (19%)**: Coverage reflects a deliberately scoped subset (3 of 12+ servlets) demonstrating the controller-tier testing pattern, rather than an exhaustive sweep. This is a deliberate engineering trade-off favoring depth in core business logic and security filters over breadth in boilerplate endpoints.
- **`tools` package (0%)**: The database seed data script is intentionally untested as it is local development tooling, not core application logic.

## CI/CD

Every push and pull request runs the full test suite (unit + integration, against a
live Oracle service container) via GitHub Actions. Dependency updates are managed
automatically via Dependabot, gated by the same CI checks before merging.

## Roadmap

This is v1 of a planned four-version progression, each exploring a different
combination of legacy and modern tooling:

- **v1** (this version) — Servlets/JSP/JDBC, no framework
- **v2** — Spring Boot + Hibernate backend, JSP/AJAX frontend
- **v3** — plain Servlet backend (hand-rolled JSON, no Spring) + React frontend
- **v4** — Spring Boot + Hibernate + REST + React
