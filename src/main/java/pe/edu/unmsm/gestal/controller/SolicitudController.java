package pe.edu.unmsm.gestal.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import pe.edu.unmsm.gestal.dto.AsignarResponsableRequest;
import pe.edu.unmsm.gestal.dto.CambioEstadoRequest;
import pe.edu.unmsm.gestal.dto.ComentarioRequest;
import pe.edu.unmsm.gestal.dto.ComentarioResponse;
import pe.edu.unmsm.gestal.dto.SolicitudDetalleResponse;
import pe.edu.unmsm.gestal.dto.SolicitudRequest;
import pe.edu.unmsm.gestal.dto.SolicitudResponse;
import pe.edu.unmsm.gestal.entity.Usuario;
import pe.edu.unmsm.gestal.service.SolicitudService;
import pe.edu.unmsm.gestal.service.UsuarioService;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    private final SolicitudService solicitudService;
    private final UsuarioService usuarioService;

    public SolicitudController(SolicitudService solicitudService, UsuarioService usuarioService) {
        this.solicitudService = solicitudService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<SolicitudResponse> listar(Authentication authentication) {
        return solicitudService.listar(usuarioActual(authentication));
    }

    @GetMapping("/{id}")
    public SolicitudDetalleResponse obtener(@PathVariable Long id, Authentication authentication) {
        return solicitudService.obtener(id, usuarioActual(authentication));
    }

    @PostMapping("/{id}/comentarios")
    @ResponseStatus(HttpStatus.CREATED)
    public ComentarioResponse comentar(@PathVariable Long id,
            @Valid @RequestBody ComentarioRequest request,
            Authentication authentication) {
        return solicitudService.comentar(id, request, usuarioActual(authentication));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public SolicitudResponse crear(@Valid @RequestBody SolicitudRequest request, Authentication authentication) {
        return solicitudService.crear(request, authentication.getName());
    }

    @PatchMapping("/{id}/estado")
    public SolicitudResponse cambiarEstado(@PathVariable Long id,
            @Valid @RequestBody CambioEstadoRequest request,
            Authentication authentication) {
        return solicitudService.cambiarEstado(id, request, usuarioActual(authentication));
    }

    @PatchMapping("/{id}/responsable")
    public SolicitudResponse asignarResponsable(@PathVariable Long id,
            @Valid @RequestBody AsignarResponsableRequest request,
            Authentication authentication) {
        return solicitudService.asignarResponsable(id, request, usuarioActual(authentication));
    }

    @PutMapping("/{id}")
    public SolicitudResponse actualizar(@PathVariable Long id,
            @Valid @RequestBody SolicitudRequest request,
            Authentication authentication) {
        return solicitudService.actualizar(id, request, usuarioActual(authentication));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id, Authentication authentication) {
        solicitudService.eliminar(id, usuarioActual(authentication));
    }

    private Usuario usuarioActual(Authentication authentication) {
        return usuarioService.porCorreo(authentication.getName());
    }
}
