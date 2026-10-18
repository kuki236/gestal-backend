package pe.edu.unmsm.gestal.dto;

import java.time.LocalDateTime;

public record ComentarioResponse(
        Long id,
        String autor,
        String rol,
        LocalDateTime fecha,
        String texto) {
}
