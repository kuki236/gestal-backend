package pe.edu.unmsm.gestal.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import pe.edu.unmsm.gestal.dto.LoginRequest;
import pe.edu.unmsm.gestal.dto.LoginResponse;
import pe.edu.unmsm.gestal.entity.Usuario;
import pe.edu.unmsm.gestal.exception.ResourceNotFoundException;
import pe.edu.unmsm.gestal.repository.UsuarioRepository;
import pe.edu.unmsm.gestal.security.JwtService;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarios;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, UsuarioRepository usuarios) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.usuarios = usuarios;
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.correo(), request.contrasena()));
        Usuario usuario = usuarios.findByCorreo(request.correo())
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + request.correo()));
        String rol = usuario.getRol().getNombre().name();
        String token = jwtService.generarToken(usuario.getCorreo(), rol);
        return new LoginResponse(token, usuario.getCorreo(), usuario.getNombre(), rol, usuario.getArea());
    }
}
