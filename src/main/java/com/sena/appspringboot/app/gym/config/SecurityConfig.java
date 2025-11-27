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
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final CustomAuthenticationSuccessHandler successHandler;

    @Autowired
    public SecurityConfig(CustomUserDetailsService userDetailsService,
                          CustomAuthenticationSuccessHandler successHandler) {
        this.userDetailsService = userDetailsService;
        this.successHandler = successHandler;
    }

    // Definir PasswordEncoder como un bean
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Utiliza el algoritmo BCrypt
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/inicio", "/perfil", "/login", "/css/**", "/js/**", "/img/**").permitAll()  // Rutas públicas
                        // Aquí es donde aseguramos que el nombre de la autoridad tiene el prefijo ROLE_
                        .requestMatchers("/admin/**").hasAuthority("ROLE_ADMINISTRADOR")  // Solo ADMIN puede acceder a /admin/**
                        .requestMatchers("/usuarios/**").hasAuthority("ROLE_ADMINISTRADOR")  // Solo ADMIN puede acceder a /usuarios/**
                        .requestMatchers("/entrenador/**").hasAuthority("ROLE_ENTRENADOR")  // Solo ENTRENADOR puede acceder a /entrenador/**
                        .requestMatchers("/cliente/**").hasAuthority("ROLE_CLIENTE")  // Solo CLIENTE puede acceder a /cliente/**
                        .anyRequest().authenticated()  // El resto de las rutas requiere autenticación
                )
                .authenticationProvider(authenticationProvider())
                .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("correo")
                        .passwordParameter("password")
                        .failureUrl("/login?error=true")
                        .successHandler(successHandler)
                        .permitAll()  // Permitir acceso sin autenticación a la página de login
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout=true")
                        .permitAll()  // Permitir acceso sin autenticación al logout
                )
                .exceptionHandling(exception -> exception
                        .accessDeniedPage("/error/403")  // Página cuando se deniega el acceso
                )
                .csrf(csrf -> csrf.disable());  // Deshabilitar CSRF (para simplificar el ejemplo)

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());  // Asegúrate de que el PasswordEncoder esté correctamente inyectado
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}


