package pe.edu.unmsm.gestal.service;

import java.time.LocalDateTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import pe.edu.unmsm.gestal.dto.AsignarResponsableRequest;
import pe.edu.unmsm.gestal.dto.CambioEstadoRequest;
import pe.edu.unmsm.gestal.dto.SolicitudRequest;
import pe.edu.unmsm.gestal.dto.SolicitudResponse;
import pe.edu.unmsm.gestal.entity.Categoria;
import pe.edu.unmsm.gestal.entity.EstadoSolicitud;
import pe.edu.unmsm.gestal.entity.NombreRol;
import pe.edu.unmsm.gestal.entity.Solicitud;
import pe.edu.unmsm.gestal.entity.Usuario;
import pe.edu.unmsm.gestal.exception.BusinessException;
import pe.edu.unmsm.gestal.exception.ResourceNotFoundException;
import pe.edu.unmsm.gestal.repository.SolicitudRepository;

@Service
public class SolicitudService {

    private static final Map<EstadoSolicitud, Set<EstadoSolicitud>> TRANSICIONES = Map.of(
            EstadoSolicitud.NUEVO, EnumSet.of(EstadoSolicitud.ASIGNADO, EstadoSolicitud.RECHAZADO),
            EstadoSolicitud.ASIGNADO, EnumSet.of(EstadoSolicitud.EN_PROCESO, EstadoSolicitud.RECHAZADO),
            EstadoSolicitud.EN_PROCESO, EnumSet.of(EstadoSolicitud.RESUELTO, EstadoSolicitud.RECHAZADO),
            EstadoSolicitud.RESUELTO, EnumSet.of(EstadoSolicitud.CERRADO, EstadoSolicitud.RECHAZADO));

    private final SolicitudRepository solicitudes;
    private final CategoriaService categoriaService;
    private final UsuarioService usuarioService;

    public SolicitudService(SolicitudRepository solicitudes, CategoriaService categoriaService,
            UsuarioService usuarioService) {
        this.solicitudes = solicitudes;
        this.categoriaService = categoriaService;
        this.usuarioService = usuarioService;
    }

    @Transactional
    public SolicitudResponse crear(SolicitudRequest request, String correoSolicitante) {
        Usuario solicitante = usuarioService.porCorreo(correoSolicitante);
        Categoria categoria = categoriaService.porId(request.categoriaId());

        Solicitud solicitud = new Solicitud();
        solicitud.setCodigo(generarCodigo());
        solicitud.setSolicitante(solicitante);
        solicitud.setCategoria(categoria);
        solicitud.setDescripcion(request.descripcion());
        solicitud.setUbicacion(request.ubicacion());
        solicitud.setPrioridad(categoria.getPrioridadInicial());
        solicitud.setEstado(EstadoSolicitud.NUEVO);
        solicitud.setFechaCreacion(LocalDateTime.now());
        solicitud.setFechaActualizacion(LocalDateTime.now());
        return toResponse(solicitudes.save(solicitud));
    }

    @Transactional(readOnly = true)
    public List<SolicitudResponse> listar(Usuario usuario) {
        List<Solicitud> resultado = switch (usuario.getRol().getNombre()) {
            case ESTUDIANTE -> solicitudes.findBySolicitante_IdAndActivoTrueOrderByFechaCreacionDesc(usuario.getId());
            case TECNICO -> solicitudes.findByResponsable_IdAndActivoTrueOrderByFechaCreacionDesc(usuario.getId());
            case SUPERVISOR -> usuario.getArea() != null
                    ? solicitudes.findByCategoria_AreaResponsableAndActivoTrueOrderByFechaCreacionDesc(usuario.getArea())
                    : solicitudes.findAllByActivoTrueOrderByFechaCreacionDesc();
            case ADMINISTRADOR -> solicitudes.findAllByActivoTrueOrderByFechaCreacionDesc();
        };
        return resultado.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public SolicitudResponse obtener(Long id, Usuario usuario) {
        Solicitud solicitud = buscar(id);
        if (usuario.getRol().getNombre() == NombreRol.ESTUDIANTE
                && !solicitud.getSolicitante().getId().equals(usuario.getId())) {
            throw new AccessDeniedException("No puede ver una solicitud que no es suya");
        }
        return toResponse(solicitud);
    }

    @Transactional
    public SolicitudResponse cambiarEstado(Long id, CambioEstadoRequest request, Usuario usuario) {
        Solicitud solicitud = buscar(id);
        NombreRol rol = usuario.getRol().getNombre();
        if (rol != NombreRol.TECNICO && rol != NombreRol.SUPERVISOR) {
            throw new BusinessException("Solo técnicos y supervisores pueden cambiar el estado");
        }
        validarTransicion(solicitud, request.estado(), rol, request.motivo());
        if (rol == NombreRol.TECNICO
                && (solicitud.getResponsable() == null
                        || !solicitud.getResponsable().getId().equals(usuario.getId()))) {
            throw new BusinessException("Solo el técnico responsable puede actualizar esta solicitud");
        }
        solicitud.setEstado(request.estado());
        if (request.estado() == EstadoSolicitud.RECHAZADO) {
            solicitud.setMotivoRechazo(request.motivo());
        }
        solicitud.setFechaActualizacion(LocalDateTime.now());
        return toResponse(solicitudes.save(solicitud));
    }

    @Transactional
    public SolicitudResponse asignarResponsable(Long id, AsignarResponsableRequest request, Usuario usuario) {
        NombreRol rol = usuario.getRol().getNombre();
        if (rol != NombreRol.SUPERVISOR && rol != NombreRol.ADMINISTRADOR) {
            throw new BusinessException("Solo el supervisor puede asignar responsables");
        }
        Solicitud solicitud = buscar(id);
        if (solicitud.getEstado() == EstadoSolicitud.CERRADO || solicitud.getEstado() == EstadoSolicitud.RECHAZADO) {
            throw new BusinessException("Una solicitud cerrada o rechazada no admite reasignaciones");
        }
        Usuario responsable = usuarioService.porId(request.responsableId());
        if (responsable.getRol().getNombre() != NombreRol.TECNICO) {
            throw new BusinessException("El responsable debe tener rol TECNICO");
        }
        solicitud.setResponsable(responsable);
        if (request.prioridad() != null) {
            solicitud.setPrioridad(request.prioridad());
        }
        if (solicitud.getEstado() == EstadoSolicitud.NUEVO) {
            solicitud.setEstado(EstadoSolicitud.ASIGNADO);
        }
        solicitud.setFechaActualizacion(LocalDateTime.now());
        return toResponse(solicitudes.save(solicitud));
    }

    @Transactional
    public void eliminar(Long id, Usuario usuario) {
        if (usuario.getRol().getNombre() != NombreRol.ADMINISTRADOR) {
            throw new BusinessException("Solo el administrador puede eliminar solicitudes");
        }
        Solicitud solicitud = buscar(id);
        solicitud.setActivo(false);
        solicitud.setFechaActualizacion(LocalDateTime.now());
        solicitudes.save(solicitud);
    }

    @Transactional
    public SolicitudResponse actualizar(Long id, SolicitudRequest request, Usuario usuario) {
        Solicitud solicitud = buscar(id);
        NombreRol rol = usuario.getRol().getNombre();
        if (solicitud.getEstado() == EstadoSolicitud.CERRADO || solicitud.getEstado() == EstadoSolicitud.RECHAZADO) {
            throw new BusinessException("Una solicitud cerrada o rechazada no se puede editar");
        }
        switch (rol) {
            case ESTUDIANTE -> {
                if (!solicitud.getSolicitante().getId().equals(usuario.getId())) {
                    throw new AccessDeniedException("No puede editar una solicitud que no es suya");
                }
                if (solicitud.getEstado() != EstadoSolicitud.NUEVO) {
                    throw new BusinessException("Solo puede editar una solicitud en estado NUEVO");
                }
            }
            case SUPERVISOR, ADMINISTRADOR -> {
            }
            default -> throw new BusinessException("Rol no autorizado para editar solicitudes");
        }
        Categoria categoria = categoriaService.porId(request.categoriaId());
        solicitud.setCategoria(categoria);
        solicitud.setDescripcion(request.descripcion());
        solicitud.setUbicacion(request.ubicacion());
        solicitud.setPrioridad(categoria.getPrioridadInicial());
        solicitud.setFechaActualizacion(LocalDateTime.now());
        return toResponse(solicitudes.save(solicitud));
    }

    private void validarTransicion(Solicitud solicitud, EstadoSolicitud nuevo, NombreRol rol, String motivo) {
        Set<EstadoSolicitud> permitidas = TRANSICIONES.getOrDefault(solicitud.getEstado(), Set.of());
        if (!permitidas.contains(nuevo)) {
            throw new BusinessException("Transición no permitida: " + solicitud.getEstado() + " -> " + nuevo);
        }
        switch (rol) {
            case TECNICO -> {
                if (nuevo != EstadoSolicitud.EN_PROCESO && nuevo != EstadoSolicitud.RESUELTO) {
                    throw new BusinessException("El técnico solo puede mover la solicitud a EN_PROCESO o RESUELTO");
                }
            }
            case SUPERVISOR -> {
                if (nuevo == EstadoSolicitud.EN_PROCESO || nuevo == EstadoSolicitud.RESUELTO) {
                    throw new BusinessException("Iniciar o resolver la atención corresponde al técnico responsable");
                }
            }
            default -> throw new BusinessException("Rol no autorizado para cambiar el estado");
        }
        if (nuevo == EstadoSolicitud.RECHAZADO && (motivo == null || motivo.isBlank())) {
            throw new BusinessException("El motivo del rechazo es obligatorio");
        }
    }

    private Solicitud buscar(Long id) {
        return solicitudes.findById(id)
                .filter(Solicitud::isActivo)
                .orElseThrow(() -> new ResourceNotFoundException("Solicitud no encontrada: " + id));
    }

    private String generarCodigo() {
        int anio = LocalDateTime.now().getYear();
        long correlativo = solicitudes.count() + 1;
        return String.format("GEST-%d-%03d", anio, correlativo);
    }

    private SolicitudResponse toResponse(Solicitud s) {
        return new SolicitudResponse(
                s.getId(),
                s.getCodigo(),
                s.getCategoria().getNombre(),
                s.getCategoria().getAreaResponsable(),
                s.getDescripcion(),
                s.getUbicacion(),
                s.getPrioridad(),
                s.getEstado(),
                s.getSolicitante().getNombre(),
                s.getResponsable() != null ? s.getResponsable().getNombre() : null,
                s.getMotivoRechazo(),
                s.getFechaCreacion(),
                s.getFechaActualizacion());
    }
}
