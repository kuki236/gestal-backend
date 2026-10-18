-- ============================================================
-- Gestal - esquema y datos semilla (MySQL 8)
-- Ejecutar:  mysql -u root -p < db/gestal.sql
-- Contraseña de todos los usuarios demo: gestal123
-- ============================================================

CREATE DATABASE IF NOT EXISTS gestal
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE gestal;

CREATE TABLE IF NOT EXISTS rol (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_rol_nombre (nombre)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS usuario (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  correo VARCHAR(255) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  rol_id BIGINT NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  area VARCHAR(255) NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_usuario_correo (correo),
  KEY fk_usuario_rol (rol_id),
  CONSTRAINT fk_usuario_rol FOREIGN KEY (rol_id) REFERENCES rol (id)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS categoria (
  id BIGINT NOT NULL AUTO_INCREMENT,
  nombre VARCHAR(255) NOT NULL,
  area_responsable VARCHAR(255) NOT NULL,
  prioridad_inicial VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_categoria_nombre (nombre)
) ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS solicitud (
  id BIGINT NOT NULL AUTO_INCREMENT,
  codigo VARCHAR(255) NOT NULL,
  solicitante_id BIGINT NOT NULL,
  categoria_id BIGINT NOT NULL,
  descripcion VARCHAR(1000) NOT NULL,
  ubicacion VARCHAR(255) NOT NULL,
  prioridad VARCHAR(255) NOT NULL,
  estado VARCHAR(255) NOT NULL,
  responsable_id BIGINT NULL,
  motivo_rechazo VARCHAR(500) NULL,
  fecha_creacion DATETIME NOT NULL,
  fecha_actualizacion DATETIME NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  PRIMARY KEY (id),
  UNIQUE KEY uk_solicitud_codigo (codigo),
  KEY fk_solicitud_solicitante (solicitante_id),
  KEY fk_solicitud_categoria (categoria_id),
  KEY fk_solicitud_responsable (responsable_id),
  CONSTRAINT fk_solicitud_solicitante FOREIGN KEY (solicitante_id) REFERENCES usuario (id),
  CONSTRAINT fk_solicitud_categoria FOREIGN KEY (categoria_id) REFERENCES categoria (id),
  CONSTRAINT fk_solicitud_responsable FOREIGN KEY (responsable_id) REFERENCES usuario (id)
) ENGINE = InnoDB;

-- ------------------------------------------------------------
-- Datos semilla
-- ------------------------------------------------------------

INSERT IGNORE INTO rol (id, nombre) VALUES
  (1, 'ESTUDIANTE'),
  (2, 'TECNICO'),
  (3, 'SUPERVISOR'),
  (4, 'ADMINISTRADOR');

INSERT IGNORE INTO categoria (id, nombre, area_responsable, prioridad_inicial) VALUES
  (1, 'Soporte informático', 'Soporte informático', 'MEDIA'),
  (2, 'Mantenimiento', 'Mantenimiento', 'MEDIA'),
  (3, 'Infraestructura', 'Infraestructura', 'ALTA'),
  (4, 'Administrativo', 'Administración', 'BAJA');

-- password_hash corresponde a "gestal123" (BCrypt)
INSERT IGNORE INTO usuario (id, nombre, correo, password_hash, rol_id, activo, area) VALUES
  (1, 'Ana Torres', 'ana.torres@example.edu', '$2a$10$nkxXcMOhL7HzS6rsXeD4beBBInoP21ZJKWjXjF4t4D6filPDXJd7y', 1, TRUE, NULL),
  (2, 'Luis Rojas', 'luis.rojas@example.edu', '$2a$10$nkxXcMOhL7HzS6rsXeD4beBBInoP21ZJKWjXjF4t4D6filPDXJd7y', 2, TRUE, 'Soporte informático'),
  (3, 'María Vega', 'maria.vega@example.edu', '$2a$10$nkxXcMOhL7HzS6rsXeD4beBBInoP21ZJKWjXjF4t4D6filPDXJd7y', 3, TRUE, 'Soporte informático'),
  (4, 'Administrador', 'admin@example.edu', '$2a$10$nkxXcMOhL7HzS6rsXeD4beBBInoP21ZJKWjXjF4t4D6filPDXJd7y', 4, TRUE, NULL),
  (5, 'Carla Ramos', 'carla.ramos@example.edu', '$2a$10$nkxXcMOhL7HzS6rsXeD4beBBInoP21ZJKWjXjF4t4D6filPDXJd7y', 2, TRUE, 'Soporte informático');

-- Solicitud de ejemplo (coincide con el prototipo Figma)
INSERT IGNORE INTO solicitud
  (id, codigo, solicitante_id, categoria_id, descripcion, ubicacion, prioridad, estado, responsable_id, motivo_rechazo, fecha_creacion, fecha_actualizacion, activo)
VALUES
  (1, 'GEST-2026-001', 1, 1, 'El proyector del laboratorio no enciende.', 'Laboratorio 204 · Pabellón nuevo', 'MEDIA', 'NUEVO', NULL, NULL, '2026-10-16 09:00:00', '2026-10-16 09:00:00', TRUE);
