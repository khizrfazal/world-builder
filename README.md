# World Builder

A full-stack application for writers to organise fictional worlds, characters, locations, factions, events, and lore.

Built with **Java (Spring Boot)**, **PostgreSQL**, **Next.js**, and **TypeScript**.

---

## Features

- Create and manage worlds
- Organise characters, locations, factions, events, and lore
- Manage character relationships and locations
- Assign factions to locations
- Track event participants
- JWT authentication with Spring Security
- RESTful API
- Database migrations using Flyway
- Dockerised local development
- Deployed frontend and backend

---

## Architecture

```
                Next.js (TypeScript)
                        │
                 REST API (JSON)
                        │
            Spring Boot (Java 21)
                        │
                 PostgreSQL Database
```

---

## Domain Model

```
World
│
├── Characters
│   ├── Character Relationships
│   └── Character Locations
│
├── Locations
│
├── Factions
│   └── Faction Locations
│
├── Events
│   └── Event Participants
│
└── Lore Entries
```

---

## Tech Stack

### Backend

- Java 21
- Spring Boot
- Spring Security (JWT)
- PostgreSQL
- Flyway
- Maven
- Docker

### Frontend

- Next.js
- TypeScript
- Tailwind CSS
- shadcn/ui
- Node.js 22

---

## Running Locally

### Prerequisites

- Java 21
- Node.js 22
- Docker
- Maven

### Database

```bash
  docker compose up -d
```

### Backend

```bash
  cd backend
  mvn spring-boot:run
```

### Frontend

```bash
  cd frontend
  npm install
  npm run dev
```

---

## Future Improvements

- [x] Worlds
- [x] Characters
- [x] Locations
- [x] Factions
- [x] Events
- [x] Lore
- [ ] Backend integration tests
- [ ] Authentication
- [ ] Frontend tests
- [ ] Search & filtering
- [ ] Image uploads
- [ ] Rich text editor

## Application Preview

![World Overview](world-builder-frontend/screenshots/murim-overview.png)
