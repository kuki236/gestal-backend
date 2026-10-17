package pe.edu.unmsm.gestal.dto;

import java.time.LocalDateTime;

import pe.edu.unmsm.gestal.entity.EstadoSolicitud;
import pe.edu.unmsm.gestal.entity.Prioridad;

public record SolicitudResponse(
        Long id,
        String codigo,
        String categoria,
        String areaResponsable,
        String descripcion,
        String ubicacion,
        Prioridad prioridad,
        EstadoSolicitud estado,
        String solicitante,
        String responsable,
        String motivoRechazo,
        LocalDateTime fechaCreacion,
        LocalDateTime fechaActualizacion) {
}
