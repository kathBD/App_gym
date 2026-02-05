package com.sena.appspringboot.app.gym.config;

import com.sena.appspringboot.app.gym.security.CustomAuthenticationSuccessHandler;
import com.sena.appspringboot.app.gym.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          CustomAuthenticationSuccessHandler successHandler) {
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Deshabilitar CSRF para permitir pruebas en Postman (POST/PUT/DELETE)
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        // Recursos públicos
                        .requestMatchers("/", "/inicio", "/login", "/css/**", "/js/**", "/img/**", "/error/**").permitAll()

                        // Seguridad para Usuarios (Gestión)
                        .requestMatchers("/usuarios/editar/**", "/usuarios/eliminar/**", "/usuarios/guardar/**").hasAuthority("ROLE_ADMINISTRADOR")
                        .requestMatchers("/usuarios/**").hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_ENTRENADOR")

                        // Seguridad para la API REST
                        .requestMatchers("/api/usuarios/**").hasAuthority("ROLE_ADMINISTRADOR")

                        // Rutas específicas por Rol
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMINISTRADOR")
                        .requestMatchers("/entrenador/**").hasAuthority("ROLE_ENTRENADOR")
                        .requestMatchers("/cliente/**").hasAuthority("ROLE_CLIENTE")

                        .anyRequest().authenticated()
                )

                // Configuración para el Navegador (Formulario de Login)
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("correo")
                        .passwordParameter("password")
                        .successHandler(successHandler)
                        .failureUrl("/login?error=true")
                        .permitAll()
                )

                // CONFIGURACIÓN PARA POSTMAN (Autenticación Básica)
                // Esto permite que Postman envíe credenciales en el Header de la petición
                .httpBasic(Customizer.withDefaults())

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/error/403")
                );

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}


