package fullstack.backend.service;

import fullstack.backend.model.Usuario;
import fullstack.backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    final private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    public List<Usuario> obtenerUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Optional<Usuario> obtenerUsuarioPorRut(Integer rut) {
        return usuarioRepository.findByRut(rut);
    }

    public Usuario crearUsuario(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Correo ya registrado: " + usuario.getEmail());
        }
        if (usuarioRepository.existsByRut(usuario.getRut())) {
            throw new RuntimeException("RUT ya registrado: " + usuario.getRut());
        }
        usuario.setHashContrasena(encoder.encode(usuario.getHashContrasena()));
        return usuarioRepository.save(usuario);
    }

    public Usuario actualizarUsuario(Integer rut, Usuario usuarioActualizado) {
        Optional<Usuario> usuario = usuarioRepository.findByRut(rut);
        if (usuario.isPresent()) {
            Usuario usuarioExistente = usuario.get();

            if (!usuarioExistente.getEmail().equals(usuarioActualizado.getEmail()) &&
                    usuarioRepository.existsByEmail(usuarioActualizado.getEmail())) {
                throw new RuntimeException("Correo ya registrado: " + usuarioActualizado.getEmail());
            }

            if (!usuarioExistente.getRut().equals(usuarioActualizado.getRut()) &&
                    usuarioRepository.existsByRut(usuarioActualizado.getRut())) {
                throw new RuntimeException("RUT ya registrado: " + usuarioActualizado.getRut());
            }

            usuarioExistente.setNombre(usuarioActualizado.getNombre());
            usuarioExistente.setApellido(usuarioActualizado.getApellido());
            usuarioExistente.setEmail(usuarioActualizado.getEmail());
            usuarioExistente.setRut(usuarioActualizado.getRut());
            usuarioExistente.setTelefono(usuarioActualizado.getTelefono());
            usuarioExistente.setEdad(usuarioActualizado.getEdad());
            usuarioExistente.setHashContrasena(encoder.encode(usuarioActualizado.getHashContrasena()));
            usuarioExistente.setRol(usuarioActualizado.getRol());
            usuarioExistente.setActivo(usuarioActualizado.getActivo());

            return usuarioRepository.save(usuarioExistente);
        }
        throw new RuntimeException("No se encontró usuario con rut: " + rut);
    }

    public void borrarUsuario(Integer rut) {
        Optional<Usuario> usuario = usuarioRepository.findByRut(rut);
        if (usuario.isPresent()) {
            usuarioRepository.delete(usuario.get());
        } else {
            throw new RuntimeException("No se encontró usuario con rut: " + rut);
        }
    }
}