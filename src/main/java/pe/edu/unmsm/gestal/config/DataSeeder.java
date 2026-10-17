package pe.edu.unmsm.gestal.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import pe.edu.unmsm.gestal.entity.Categoria;
import pe.edu.unmsm.gestal.entity.NombreRol;
import pe.edu.unmsm.gestal.entity.Prioridad;
import pe.edu.unmsm.gestal.entity.Rol;
import pe.edu.unmsm.gestal.entity.Usuario;
import pe.edu.unmsm.gestal.repository.CategoriaRepository;
import pe.edu.unmsm.gestal.repository.RolRepository;
import pe.edu.unmsm.gestal.repository.UsuarioRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final String PASSWORD_DEMO = "gestal123";

    private final RolRepository roles;
    private final CategoriaRepository categorias;
    private final UsuarioRepository usuarios;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(RolRepository roles, CategoriaRepository categorias, UsuarioRepository usuarios,
            PasswordEncoder passwordEncoder) {
        this.roles = roles;
        this.categorias = categorias;
        this.usuarios = usuarios;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (roles.count() == 0) {
            roles.save(new Rol(NombreRol.ESTUDIANTE));
            roles.save(new Rol(NombreRol.TECNICO));
            roles.save(new Rol(NombreRol.SUPERVISOR));
            roles.save(new Rol(NombreRol.ADMINISTRADOR));
        }

        if (categorias.count() == 0) {
            categorias.save(new Categoria("Soporte informático", "Soporte informático", Prioridad.MEDIA));
            categorias.save(new Categoria("Mantenimiento", "Mantenimiento", Prioridad.MEDIA));
            categorias.save(new Categoria("Infraestructura", "Infraestructura", Prioridad.ALTA));
            categorias.save(new Categoria("Administrativo", "Administración", Prioridad.BAJA));
        }

        crearUsuario("Ana Torres", "ana.torres@example.edu", NombreRol.ESTUDIANTE, null);
        crearUsuario("Luis Rojas", "luis.rojas@example.edu", NombreRol.TECNICO, "Soporte informático");
        crearUsuario("María Vega", "maria.vega@example.edu", NombreRol.SUPERVISOR, "Soporte informático");
        crearUsuario("Administrador", "admin@example.edu", NombreRol.ADMINISTRADOR, null);
    }

    private void crearUsuario(String nombre, String correo, NombreRol nombreRol, String area) {
        if (usuarios.existsByCorreo(correo)) {
            return;
        }
        Rol rol = roles.findByNombre(nombreRol)
                .orElseThrow(() -> new IllegalStateException("Rol no encontrado: " + nombreRol));
        usuarios.save(new Usuario(nombre, correo, passwordEncoder.encode(PASSWORD_DEMO), rol, area));
    }
}
