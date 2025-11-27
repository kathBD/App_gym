package com.sena.appspringboot.app.gym.service;


import com.sena.appspringboot.app.gym.model.Usuario;
import com.sena.appspringboot.app.gym.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Intentando cargar usuario con correo: [" + username + "]");

        Usuario usuario = usuarioRepository.findByCorreoIgnoreCase(username.trim())
                .orElseThrow(() -> {
                    System.out.println("Usuario no encontrado con correo: " + username);
                    return new UsernameNotFoundException("Usuario no encontrado con correo: " + username);
                });

        System.out.println("Usuario encontrado: " + usuario.getCorreo());
        System.out.println("Contraseña hash: " + usuario.getPassword());

        String rolName = usuario.getRol() != null ? usuario.getRol().getNombre().toUpperCase() : "USER";
        System.out.println("Rol del usuario: " + rolName);

        return new User(
                usuario.getCorreo(),
                usuario.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + rolName))
        );
    }
}