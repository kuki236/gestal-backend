package pe.edu.unmsm.gestal.controller;

import java.util.List;
import java.util.Locale;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pe.edu.unmsm.gestal.dto.UsuarioResponse;
import pe.edu.unmsm.gestal.entity.NombreRol;
import pe.edu.unmsm.gestal.exception.BusinessException;
import pe.edu.unmsm.gestal.service.UsuarioService;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<UsuarioResponse> listar(@RequestParam(required = false) String rol) {
        if (rol == null || rol.isBlank()) {
            throw new BusinessException("El parámetro rol es obligatorio");
        }
        return usuarioService.listarPorRol(rolDe(rol));
    }

    private NombreRol rolDe(String rol) {
        try {
            return NombreRol.valueOf(rol.trim().toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new BusinessException("Rol no válido: " + rol);
        }
    }
}
