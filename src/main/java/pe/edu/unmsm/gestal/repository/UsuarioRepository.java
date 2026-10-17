package pe.edu.unmsm.gestal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import pe.edu.unmsm.gestal.entity.NombreRol;
import pe.edu.unmsm.gestal.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByCorreo(String correo);

    boolean existsByCorreo(String correo);

    List<Usuario> findByRol_Nombre(NombreRol nombre);
}
