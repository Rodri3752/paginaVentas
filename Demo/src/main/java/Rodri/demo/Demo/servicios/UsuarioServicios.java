package Rodri.demo.Demo.servicios;

import Rodri.demo.Demo.entidades.Usuario;
import Rodri.demo.Demo.errores.ErrorServicio;
import Rodri.demo.Demo.repositorios.UsuarioRepositorio;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.stereotype.Service;

@Service
public class UsuarioServicios implements UserDetailsService {

    @Autowired
    private NotificacionServicios notificacionServicio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void registrar(String nombre, String apellido, String mail, String clave, String clave1) throws ErrorServicio {
        validar(nombre, apellido, mail, clave, clave1);
        Usuario usuario = new Usuario();
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setMail(mail);
        String encriptada = new BCryptPasswordEncoder().encode(clave);
        usuario.setClave(encriptada);
        usuario.setAlta(new Date());

        usuarioRepositorio.save(usuario);
       // notificacionServicio.enviar("Bienvenido a la mejor pagina de compras", "Pagina de compras", usuario.getMail());

    }

    @Transactional
    public void modificar(String id, String nombre, String apellido, String mail, String clave, String clave1) throws ErrorServicio {
        validar(nombre, apellido, mail, clave, clave1);
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {

            Usuario usuario = respuesta.get();

            usuario.setApellido(apellido);
            usuario.setNombre(nombre);
            usuario.setMail(mail);
            String encriptada = new BCryptPasswordEncoder().encode(clave);
            usuario.setClave(encriptada);

            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado.");
        }
    }

    @Transactional
    public void deshabilitar(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setBaja(new Date());
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado.");
        }
    }

    @Transactional
    public void habilitar(String id) throws ErrorServicio {
        Optional<Usuario> respuesta = usuarioRepositorio.findById(id);
        if (respuesta.isPresent()) {
            Usuario usuario = respuesta.get();
            usuario.setBaja(null);
            usuarioRepositorio.save(usuario);
        } else {
            throw new ErrorServicio("No se encontro el usuario solicitado.");
        }
    }

    private void validar(String nombre, String apellido, String mail, String clave, String clave1) throws ErrorServicio {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new ErrorServicio("El nombre del usuario no puede ser nulo");
        }
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new ErrorServicio("El apellido del usuario no puede ser nulo");
        }
        if (mail == null || mail.trim().isEmpty()) {
            throw new ErrorServicio("El mail del usuario no puede ser nulo");
        }
        if (clave == null || clave.trim().isEmpty() || clave.length() < 6 ) {
            throw new ErrorServicio("El clave del usuario no puede ser nulo, ni tener menos de 6 digitos");
        }
       // if (clave != clave1) {
       //     throw new ErrorServicio("La clave y la verificacion de la clave deben ser iguales");
       // }
    }

    @Override
    public UserDetails loadUserByUsername(String mail) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorMail(mail);
        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();

            GrantedAuthority p1 = new SimpleGrantedAuthority("MODULO_FOTOS");
            permisos.add(p1);
            GrantedAuthority p2 = new SimpleGrantedAuthority("MODULO_PRODUCTO");
            permisos.add(p2);
            GrantedAuthority p3 = new SimpleGrantedAuthority("MODULO_COMPRA");
            permisos.add(p3);
            User user = new User(usuario.getNombre(), usuario.getClave(), permisos);
            return user;
        } else {
            return null;
        }
    }
}
