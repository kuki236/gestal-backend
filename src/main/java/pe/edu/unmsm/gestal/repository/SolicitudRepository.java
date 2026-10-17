package pe.edu.unmsm.gestal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.unmsm.gestal.entity.Solicitud;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    List<Solicitud> findBySolicitante_IdOrderByFechaCreacionDesc(Long solicitanteId);

    List<Solicitud> findByResponsable_IdOrderByFechaCreacionDesc(Long responsableId);

    List<Solicitud> findByCategoria_AreaResponsableOrderByFechaCreacionDesc(String areaResponsable);

    List<Solicitud> findAllByOrderByFechaCreacionDesc();

    long countByFechaCreacionBetween(java.time.LocalDateTime inicio, java.time.LocalDateTime fin);
}
