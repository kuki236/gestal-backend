# Gestal — Backend

Backend del sistema **Gestal — Sistema de Gestión Integral de Servicios Universitarios**.

Proyecto del curso *Taller de Construcción de Software Web* — Grupo 3, Facultad de Ingeniería de Sistemas e Informática, UNMSM (2026-II).

## Stack

- Java 17 + Spring Boot (API REST, arquitectura por capas: `controller`, `service`, `repository`, `dto`, `entity`)
- MySQL (persistencia con JPA/Hibernate)
- Spring Security + JWT (autenticación y autorización por rol)
- Bean Validation y manejo centralizado de errores

## Repositorios relacionados

- Frontend: https://github.com/kuki236/gestal-frontend

## Flujo de trabajo (GitFlow)

| Rama | Uso |
| --- | --- |
| `main` | Versiones estables y entregables |
| `develop` | Integración del trabajo en curso |
| `feature/*` | Nuevas funcionalidades, desde `develop` (`feature/HU-012-registro-solicitud`) |
| `release/*` | Preparación de una entrega (`release/v0.2.0`) |
| `hotfix/*` | Correcciones urgentes sobre `main` |

Commits con Conventional Commits (`feat:`, `fix:`, `docs:`, `chore:`, `test:`, `refactor:`).

## Requisitos

- JDK 17
- MySQL 8 en ejecución (base `gestal`; se crea automáticamente si usas MySQL local)

No hace falta instalar Maven: el proyecto incluye el **Maven Wrapper** (`mvnw` / `mvnw.cmd`).

## Configuración

Copia `.env.example` a `.env` y ajusta los valores. El backend carga `.env` automáticamente.

```
DB_URL=jdbc:mysql://localhost:3306/gestal?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=
JWT_SECRET=una-clave-larga-de-al-menos-32-caracteres
```

## Base de datos

Hay dos formas de preparar MySQL (cualquiera sirve):

1. **Script SQL (recomendado):** crea la base, las tablas y los datos semilla.

   ```bash
   mysql -u root -p < db/gestal.sql
   ```

2. **Automático al arrancar:** con `spring.jpa.hibernate.ddl-auto=update` y el `DataSeeder`, el backend crea las tablas y los datos semilla (roles, categorías y usuarios) si no existen.

Contraseña de los usuarios demo: `gestal123`.

## Ejecución

```bash
# Linux/macOS
./mvnw spring-boot:run

# Windows (PowerShell)
.\mvnw.cmd spring-boot:run
```

El esquema y los datos semilla (roles, categorías y usuarios) se crean al arrancar (`ddl-auto: update` + `DataSeeder`).

### Usuarios semilla

| Correo | Rol | Contraseña |
| --- | --- | --- |
| ana.torres@example.edu | ESTUDIANTE | gestal123 |
| luis.rojas@example.edu | TECNICO | gestal123 |
| maria.vega@example.edu | SUPERVISOR | gestal123 |
| admin@example.edu | ADMINISTRADOR | gestal123 |

## Endpoints

Base: `http://localhost:8080`

| Método | Ruta | Rol | Uso |
| --- | --- | --- | --- |
| POST | `/api/auth/login` | Público | Iniciar sesión; devuelve `token`, `rol` y `area` |
| GET | `/api/usuarios?rol=TECNICO` | Supervisor/Admin | Listar usuarios por rol (usado por el frontend para asignar responsables) |
| GET | `/api/categorias` | Autenticado | Listar categorías (id, nombre, área y prioridad inicial) |
| GET | `/api/prioridades` | Autenticado | Listar los valores de prioridad |
| GET | `/api/solicitudes` | Autenticado | Listar según rol |
| GET | `/api/solicitudes/{id}` | Autenticado | Ver detalle con control de acceso, historial de estados y comentarios |
| POST | `/api/solicitudes` | Estudiante | Registrar solicitud (`categoriaId`, `descripcion`, `ubicacion`) |
| POST | `/api/solicitudes/{id}/comentarios` | Autenticado (según rol) | Añadir comentario (`texto`); no permitido en estados finales |
| PUT | `/api/solicitudes/{id}` | Estudiante/Supervisor/Admin | Actualizar solicitud (`categoriaId`, `descripcion`, `ubicacion`) |
| PATCH | `/api/solicitudes/{id}/estado` | Técnico/Supervisor | Cambiar estado (`estado`, `motivo` opcional para rechazo) |
| PATCH | `/api/solicitudes/{id}/responsable` | Supervisor | Asignar responsable (`responsableId`, `prioridad` opcional); pasa a ASIGNADO |
| DELETE | `/api/solicitudes/{id}` | Administrador | Eliminación lógica de la solicitud |

Autenticación: `Authorization: Bearer <token>`.

### Reglas de negocio

- El código se genera como `GEST-YYYY-NNN` (por ejemplo, `GEST-2026-001`).
- Estado inicial `NUEVO`; la prioridad inicial se toma de la categoría.
- Transiciones válidas: `NUEVO → ASIGNADO → EN_PROCESO → RESUELTO → CERRADO`; `RECHAZADO` como salida. `CERRADO` y `RECHAZADO` son finales.
- El técnico solo mueve `ASIGNADO → EN_PROCESO → RESUELTO`; el cierre, el rechazo y la asignación son del supervisor.
- El estudiante solo ve sus propias solicitudes.
- El historial de estados se registra en cada creación, transición y asignación (`GET /solicitudes/{id}` lo devuelve).
- Comentarios: puede comentar el solicitante dueño, el técnico responsable, el supervisor y el administrador; no se permiten en estados finales.

### Ejemplo rápido

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"correo":"ana.torres@example.edu","contrasena":"gestal123"}'

# Registrar solicitud (con el token del login)
curl -X POST http://localhost:8080/api/solicitudes \
  -H "Authorization: Bearer <token>" -H "Content-Type: application/json" \
  -d '{"categoriaId":1,"descripcion":"El proyector no enciende","ubicacion":"Laboratorio 204"}'
```

También hay una colección Postman lista para importar en `api/gestal.postman_collection.json` (guarda el token del login automáticamente).

## Estructura

```
src/main/java/pe/edu/unmsm/gestal/
├── config/       # SecurityConfig, DataSeeder
├── controller/   # Auth, Categoria, Solicitud
├── dto/          # Peticiones y respuestas (records)
├── entity/       # Rol, Usuario, Categoria, Solicitud + enums
├── exception/    # Excepciones y GlobalExceptionHandler
├── repository/   # Repositorios JPA
├── security/     # JwtService, filtro JWT, UserDetailsService
└── service/      # Auth, Usuario, Categoria, Solicitud
```

## Estado

Fase II — primera versión funcional (MVP): login, registro, consulta, cambio de estado, historial y comentarios con MySQL.

## Pendientes (fuera del MVP)

- Paginación de listados (RNF-10).
- Dashboard de indicadores, SLA, auditoría y adjuntos.
- Docker, CI/CD, pruebas automatizadas y observabilidad.
