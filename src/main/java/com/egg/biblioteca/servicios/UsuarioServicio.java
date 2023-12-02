package com.egg.biblioteca.servicios;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.egg.biblioteca.entidades.Usuario;
import com.egg.biblioteca.enums.Rol;
import com.egg.biblioteca.excepciones.BibliotecaException;
import com.egg.biblioteca.repositorios.UsuarioRepositorio;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;

@Service
public class UsuarioServicio implements UserDetailsService{

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Transactional
    public void registrar(String nombre, String email, String password, String password2) throws BibliotecaException {

        validar(nombre, email, password, password2);
        Usuario usuario = new Usuario();

        usuario.setNombre(nombre);
        usuario.setEmail(email);
        usuario.setPassword(new BCryptPasswordEncoder().encode(password)); //para guardar la contraseña también la vamos a codificar.
        usuario.setRol(Rol.USER); //Para que por defecto, los usuarios registrados tengan este rol.

        usuarioRepositorio.save(usuario);
    }

    private void validar(String nombre, String email, String password, String password2) throws BibliotecaException {
        if(nombre == null || nombre.isBlank())
            throw new BibliotecaException("El nombre no puede ser nulo ni estar vacio.");
        if(email == null || email.isBlank())
            throw new BibliotecaException("El email no puede ser nulo ni estar vacio.");
        if (password == null || password.isBlank() || password.length() < 6)
            throw new BibliotecaException("La contraseña no puede ser nula ni estar vacia, y debe tener al menos 6 caracteres.");
        if (!password.equals(password2))
            throw new BibliotecaException("Las contraseñas ingresadas deben coincidir.");

    }

    //Cada vez que un user se loguee con sus credenciales, Spring Security se va a dirigir a este método y va a 
    //otorgar los permisos a los que tiene acceso este usuario.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.buscarPorEmail(email);

        if (usuario != null) {

            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority p = new SimpleGrantedAuthority("ROLE_" + usuario.getRol().toString());

            permisos.add(p);
            
            //Recuperamos los atributos de la request (solicitud http)
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession sesion = attr.getRequest().getSession(true);
            sesion.setAttribute("usuariosession", usuario);

            return new User(usuario.getEmail(), usuario.getPassword(), permisos);
        }

        return null;
    }    
}
