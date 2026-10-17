package pe.edu.unmsm.gestal.dto;

import jakarta.validation.constraints.NotNull;
import pe.edu.unmsm.gestal.entity.EstadoSolicitud;

public record CambioEstadoRequest(
        @NotNull(message = "El nuevo estado es obligatorio")
        EstadoSolicitud estado,

        String motivo) {
}
