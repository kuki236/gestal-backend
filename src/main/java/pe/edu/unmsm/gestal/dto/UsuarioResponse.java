package pe.edu.unmsm.gestal.dto;

public record UsuarioResponse(
        Long id,
        String nombre,
        String correo,
        String rol,
        String area) {
}
