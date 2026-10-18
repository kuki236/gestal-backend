package pe.edu.unmsm.gestal.service;

import java.util.List;

import org.springframework.stereotype.Service;

import pe.edu.unmsm.gestal.dto.UsuarioResponse;
import pe.edu.unmsm.gestal.entity.NombreRol;
import pe.edu.unmsm.gestal.entity.Usuario;
import pe.edu.unmsm.gestal.exception.ResourceNotFoundException;
import pe.edu.unmsm.gestal.repository.UsuarioRepository;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarios;

    public UsuarioService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    public Usuario porCorreo(String correo) {
        return usuarios.findByCorreo(correo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + correo));
    }

    public Usuario porId(Long id) {
        return usuarios.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + id));
    }

    public List<UsuarioResponse> listarPorRol(NombreRol nombre) {
        return usuarios.findByRol_Nombre(nombre).stream()
                .map(this::toResponse)
                .toList();
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getRol().getNombre().name(),
                usuario.getArea());
    }
}
