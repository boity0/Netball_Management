# Netball Manager Backend

REST API for managing an 11-player netball squad. Built with Java 26, Spring Boot
4.1.2, Spring MVC, Spring Data JPA/Hibernate, Jakarta Validation, Spring Security,
JWT, BCrypt, and MySQL. The default profile uses an in-memory H2 database so the API
can be started and tried without first installing MySQL.

## Run locally

Install JDK 26 and Maven 3.6.3 or later. In PowerShell, set a coach account and a
private signing key before starting the API:

```powershell
$env:BOOTSTRAP_COACH_EMAIL = "coach@example.com"
$env:BOOTSTRAP_COACH_PASSWORD = "Choose-a-unique-password"
$env:JWT_SECRET = "replace-with-a-private-secret-at-least-32-characters"
mvn spring-boot:run
```

The configured coach is created on first startup only. Public registration creates
PLAYER accounts; it cannot grant coach privileges. To persist local data beyond a
restart, use MySQL or change the default H2 JDBC URL to a file-backed H2 URL.

To use MySQL, create a database/user (or use the configured create-database option),
set `DATABASE_USERNAME`, `DATABASE_PASSWORD`, `DATABASE_URL`, and `JWT_SECRET`, then
start with:

```powershell
$env:SPRING_PROFILES_ACTIVE = "mysql"
mvn spring-boot:run
```

Use a unique, private `JWT_SECRET` and a production-safe `DDL_AUTO` setting outside
local development. Configure database credentials through environment variables;
do not commit secrets.

## Authentication and first requests

API: `http://localhost:8080`. `GET /api/health` is public. Sign in as the configured
coach:

```http
POST /api/auth/login
Content-Type: application/json

{"email":"coach@example.com","password":"Choose-a-unique-password"}
```

Copy `accessToken` from the response and set `Authorization: Bearer <token>` on
protected requests. Create a team:

```http
POST /api/teams
Authorization: Bearer <token>
Content-Type: application/json

{"name":"Harriers","description":"Senior squad"}
```

Then add players using `POST /api/teams/{teamId}/players`. For example:

```json
{
  "firstName": "Ava",
  "lastName": "Player",
  "email": "ava@example.com",
  "phoneNumber": "555-0100",
  "jerseyNumber": 1,
  "preferredPositions": ["GS", "GA"]
}
```

The system links a registered player account to a roster profile when their email
addresses match. Register players using `POST /api/auth/register`; passwords must
be at least 10 characters and are stored as BCrypt hashes.

## API routes

All endpoints other than health and authentication require a JWT. Mutations that
manage the squad or match are coach-only; players can view their own profile,
attendance, and statistics, and update their own availability.

| Area | Routes |
|---|---|
| Teams | `GET/POST /api/teams`, `GET/PUT /api/teams/{teamId}` |
| Players | `GET/POST /api/teams/{teamId}/players`, `GET/PUT/DELETE /api/players/{playerId}`, `GET /api/players/me` |
| Matches | `POST /api/teams/{teamId}/matches`, `GET /api/matches?teamId={teamId}`, `GET /api/matches/upcoming?teamId={teamId}`, `GET /api/matches/completed?teamId={teamId}`, `GET/PUT/DELETE /api/matches/{matchId}`, `POST /api/matches/{matchId}/result` |
| Availability | `POST/GET /api/matches/{matchId}/availability` |
| Lineups | `POST/GET /api/matches/{matchId}/lineup`, `POST /api/matches/{matchId}/generate-lineup` |
| Substitutions | `POST/GET /api/matches/{matchId}/substitutions` |
| Training | `POST/GET /api/teams/{teamId}/training-sessions` |
| Attendance | `POST /api/matches/{matchId}/attendance`, `POST /api/training-sessions/{sessionId}/attendance`, `GET /api/players/{playerId}/attendance`, `GET /api/players/{playerId}/attendance/summary` |
| Statistics | `POST /api/matches/{matchId}/statistics`, `GET /api/players/{playerId}/statistics` |

Availability uses `AVAILABLE`, `UNAVAILABLE`, or `MAYBE`. The response summarizes
the number available, unavailable players, and players who have not replied. It
includes a warning message if fewer than seven are available.

A lineup request has seven starting selections, one for each of `GS`, `GA`, `WA`,
`C`, `WD`, `GD`, and `GK`, plus up to four standby selections:

```json
{
  "starting": [
    {"playerId": 1, "position": "GS"},
    {"playerId": 2, "position": "GA"},
    {"playerId": 3, "position": "WA"},
    {"playerId": 4, "position": "C"},
    {"playerId": 5, "position": "WD"},
    {"playerId": 6, "position": "GD"},
    {"playerId": 7, "position": "GK"}
  ],
  "standby": []
}
```

`generate-lineup` returns a suggestion and does not save it. The rule-based ranking
uses preferred positions, availability, attendance, recent statistics, and prior
starting selections. Coaches review/edit the suggestion and submit it to `/lineup`.
Match, lineup, player, and team validation errors return JSON with a message and an
appropriate HTTP status.

## Data model and code layout

Java sources are under `com.netballmanager.netballmanager`: `controller` exposes
HTTP endpoints, `service` contains business rules, `repository` contains Spring
Data JPA interfaces, `entity` models relational data, `dto` defines API payloads,
`exception` handles API errors, `config` configures Spring, and `security` handles
JWT token creation. Hibernate creates/updates the local schema from the JPA entities.

## Tests

Run `mvn test`. The MockMvc integration tests start the app against H2 and cover
health, registration/authentication, coach-only access, duplicate jersey rejection,
starting-seven validation, and lineup recommendations.
