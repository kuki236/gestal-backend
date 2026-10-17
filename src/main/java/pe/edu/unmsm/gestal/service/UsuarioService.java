package pe.edu.unmsm.gestal.service;

import org.springframework.stereotype.Service;

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
}
