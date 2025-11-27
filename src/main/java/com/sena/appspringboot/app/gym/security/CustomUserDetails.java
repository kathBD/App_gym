package com.sena.appspringboot.app.gym.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUserDetails extends User {

    private Long id;  // El campo id del usuario

    // Constructor que toma el id, nombre de usuario, contraseña y roles
    public CustomUserDetails(Long id, String username, String password,
                             Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);  // Llamamos al constructor de la clase padre (User)
        this.id = id;  // Asignamos el id
    }

    // Getter para el id
    public Long getId() {
        return id;
    }
}
