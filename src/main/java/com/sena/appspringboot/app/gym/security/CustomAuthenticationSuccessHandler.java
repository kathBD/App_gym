package com.sena.appspringboot.app.gym.security;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Set;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
        System.out.println("Roles del usuario: " + roles); // depuración

        if (roles.contains("ROLE_ADMINISTRADOR")) {
            response.sendRedirect("/admin");
        } else if (roles.contains("ROLE_CLIENTE")) {
            response.sendRedirect("/cliente");
        } else if (roles.contains("ROLE_ENTRENADOR")) {
            response.sendRedirect("/entrenador");
        } else {
            response.sendRedirect("/");
        }
    }
}
