package pe.edu.unmsm.gestal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ComentarioRequest(
        @NotBlank(message = "El comentario no puede estar vacío")
        @Size(max = 1000, message = "El comentario no puede superar los 1000 caracteres")
        String texto) {
}
