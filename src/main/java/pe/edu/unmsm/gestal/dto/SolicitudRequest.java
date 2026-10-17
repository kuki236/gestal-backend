package pe.edu.unmsm.gestal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record SolicitudRequest(
        @NotNull(message = "La categoría es obligatoria")
        Long categoriaId,

        @NotBlank(message = "La descripción es obligatoria")
        @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
        String descripcion,

        @NotBlank(message = "La ubicación es obligatoria")
        String ubicacion) {
}
