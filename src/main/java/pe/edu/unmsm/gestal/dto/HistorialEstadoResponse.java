package pe.edu.unmsm.gestal.dto;

import java.time.LocalDateTime;

import pe.edu.unmsm.gestal.entity.EstadoSolicitud;

public record HistorialEstadoResponse(
        EstadoSolicitud estado,
        String titulo,
        String detalle,
        LocalDateTime fecha,
        String autor) {
}
