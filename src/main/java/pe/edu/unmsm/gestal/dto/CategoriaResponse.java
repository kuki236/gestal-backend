package pe.edu.unmsm.gestal.dto;

import pe.edu.unmsm.gestal.entity.Prioridad;

public record CategoriaResponse(
        Long id,
        String nombre,
        String areaResponsable,
        Prioridad prioridadInicial) {
}
