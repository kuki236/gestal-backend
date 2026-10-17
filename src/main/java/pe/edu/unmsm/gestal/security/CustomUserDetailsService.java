package pe.edu.unmsm.gestal.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import pe.edu.unmsm.gestal.entity.Usuario;
import pe.edu.unmsm.gestal.repository.UsuarioRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarios;

    public CustomUserDetailsService(UsuarioRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException {
        Usuario usuario = usuarios.findByCorreo(correo)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + correo));
        return User.withUsername(usuario.getCorreo())
                .password(usuario.getPasswordHash())
                .authorities(new SimpleGrantedAuthority("ROLE_" + usuario.getRol().getNombre().name()))
                .disabled(!usuario.isActivo())
                .build();
    }
}
