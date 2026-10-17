package pe.edu.unmsm.gestal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.unmsm.gestal.entity.NombreRol;
import pe.edu.unmsm.gestal.entity.Rol;

public interface RolRepository extends JpaRepository<Rol, Long> {

    Optional<Rol> findByNombre(NombreRol nombre);
}
