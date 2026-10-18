package pe.edu.unmsm.gestal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.unmsm.gestal.entity.HistorialEstado;

public interface HistorialEstadoRepository extends JpaRepository<HistorialEstado, Long> {

    List<HistorialEstado> findBySolicitud_IdOrderByFechaAscIdAsc(Long solicitudId);
}
