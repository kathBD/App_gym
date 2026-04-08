package com.sena.appspringboot.app.gym.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // No autenticar peticiones de login
        if (path.contains("/api/auth/login")) {
            chain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        System.out.println("🔍 JWT Filter - URL: " + path);
        System.out.println("🔍 JWT Filter - Header: " + (header != null ? "Presente" : "No presente"));

        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println("⚠️ No hay token Bearer, continuando...");
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(7);
        System.out.println("🔍 Token recibido: " + token.substring(0, Math.min(50, token.length())) + "...");

        try {
            if (jwtUtil.isTokenValid(token)) {
                String correo = jwtUtil.extractCorreo(token);
                String rol = jwtUtil.extractRol(token);

                System.out.println("✅ Token válido - Usuario: " + correo + ", Rol: " + rol);

                List<SimpleGrantedAuthority> authorities =
                        List.of(new SimpleGrantedAuthority("ROLE_" + rol.toUpperCase()));

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(correo, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
                System.out.println("✅ Autenticación establecida en contexto");
            } else {
                System.out.println("❌ Token inválido");
                response.setStatus(401);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token inválido\"}");
                return;
            }
        } catch (Exception e) {
            System.out.println("❌ Error procesando token: " + e.getMessage());
            e.printStackTrace();
            response.setStatus(401);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"" + e.getMessage() + "\"}");
            return;
        }

        chain.doFilter(request, response);
    }
}