# Gestal — Backend

Backend del sistema **Gestal — Sistema de Gestión Integral de Servicios Universitarios**.

Proyecto del curso *Taller de Construcción de Software Web* — Grupo 3, Facultad de Ingeniería de Sistemas e Informática, UNMSM (2026-II).

## Stack

- Java + Spring Boot (API REST, arquitectura por capas: controladores, servicios, DTO)
- MySQL (persistencia)
- Autenticación con JWT o sesión
- Docker y CI/CD en fases posteriores

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

## Estado

Fase I del proyecto (análisis, especificación y prototipo). El scaffolding de Spring Boot se agregará al iniciar la Fase II.
