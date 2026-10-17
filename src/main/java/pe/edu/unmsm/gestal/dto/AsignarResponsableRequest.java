package pe.edu.unmsm.gestal.dto;

import jakarta.validation.constraints.NotNull;
import pe.edu.unmsm.gestal.entity.Prioridad;

public record AsignarResponsableRequest(
        @NotNull(message = "El responsable es obligatorio")
        Long responsableId,

        Prioridad prioridad) {
}
