package pe.edu.unmsm.gestal.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.unmsm.gestal.entity.Comentario;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    List<Comentario> findBySolicitud_IdOrderByFechaAscIdAsc(Long solicitudId);
}
