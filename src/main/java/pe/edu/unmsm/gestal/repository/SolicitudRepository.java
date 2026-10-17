package pe.edu.unmsm.gestal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.unmsm.gestal.entity.Solicitud;

public interface SolicitudRepository extends JpaRepository<Solicitud, Long> {

    List<Solicitud> findBySolicitante_IdAndActivoTrueOrderByFechaCreacionDesc(Long solicitanteId);

    List<Solicitud> findByResponsable_IdAndActivoTrueOrderByFechaCreacionDesc(Long responsableId);

    List<Solicitud> findByCategoria_AreaResponsableAndActivoTrueOrderByFechaCreacionDesc(String areaResponsable);

    List<Solicitud> findAllByActivoTrueOrderByFechaCreacionDesc();
}
