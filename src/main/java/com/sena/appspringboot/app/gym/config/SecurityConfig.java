package com.sena.appspringboot.app.gym.config;

import com.sena.appspringboot.app.gym.security.CustomAuthenticationSuccessHandler;
import com.sena.appspringboot.app.gym.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Reemplaza a @EnableGlobalMethodSecurity (que está obsoleta)
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
                .csrf(csrf -> csrf.disable()) // Deshabilitado para facilitar pruebas en API y formularios
                .authorizeHttpRequests(auth -> auth
                        // 1. Recursos estáticos y rutas públicas
                        .requestMatchers("/", "/inicio", "/login", "/css/**", "/js/**", "/img/**", "/error/**").permitAll()

                        // 2. Seguridad del CRUD de Usuarios (Web y API)
                        // Usamos hasAuthority porque coincide exactamente con lo que devuelve tu UserDetailsService
                        .requestMatchers("/usuarios/editar/**", "/usuarios/eliminar/**", "/usuarios/guardar/**").hasAuthority("ROLE_ADMINISTRADOR")
                        .requestMatchers("/usuarios/**").hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_ENTRENADOR")

                        // 3. Rutas de la API Rest
                        .requestMatchers("/api/usuarios/**").hasAuthority("ROLE_ADMINISTRADOR")

                        // 4. Rutas específicas por Rol
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMINISTRADOR")
                        .requestMatchers("/entrenador/**").hasAuthority("ROLE_ENTRENADOR")
                        .requestMatchers("/cliente/**").hasAuthority("ROLE_CLIENTE")

                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("correo")
                        .passwordParameter("password")
                        .successHandler(successHandler) // Tu manejador personalizado para redirigir según rol
                        .failureUrl("/login?error=true")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/error/403") // Asegúrate de tener esta vista o controlador
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


