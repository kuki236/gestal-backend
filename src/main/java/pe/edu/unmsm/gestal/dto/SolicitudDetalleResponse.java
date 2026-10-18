package pe.edu.unmsm.gestal.dto;

import java.util.List;

public record SolicitudDetalleResponse(
        SolicitudResponse solicitud,
        List<HistorialEstadoResponse> historial,
        List<ComentarioResponse> comentarios) {
}
