package pe.edu.unmsm.gestal.dto;

public record LoginResponse(
        String token,
        String correo,
        String nombre,
        String rol,
        String area) {
}
