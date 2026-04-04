package com.sena.appspringboot.app.gym.service;

import com.sena.appspringboot.app.gym.model.Usuario;
import com.sena.appspringboot.app.gym.model.Rol;
import com.sena.appspringboot.app.gym.repository.IUsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Evidencia de conocimiento: Prueba unitaria para el módulo de Usuarios.
 * Se utiliza el framework Mockito para simular el comportamiento del Repositorio.
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private IUsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuarioPrueba;

    /**
     * Fase de Preparación (Arrange):
     * Se inicializan los objetos necesarios con los métodos manuales definidos en el modelo.
     */
    @BeforeEach
    void setUp() {
        usuarioPrueba = new Usuario();

        // Usamos los nombres exactos de tus métodos manuales en Usuario.java
        usuarioPrueba.setUsuarioId(1L);
        usuarioPrueba.setNombre("Katherine Duarte");
        usuarioPrueba.setCorreo("katherine@vibrafit.com");

        Rol rolTest = new Rol();
        rolTest.setNombre("ROLE_CLIENTE");
        usuarioPrueba.setRol(rolTest);
    }

    /**
     * Fase de Ejecución y Validación (Act & Assert):
     * Se verifica que el servicio encuentre correctamente al usuario.
     */
    @Test
    @DisplayName("Prueba Exitosa: Buscar usuario por correo")
    void testFindUserByEmailSuccess() {
        // 1. GIVEN: Definimos que el repo entregue el usuario directo (sin Optional)
        when(usuarioRepository.findByCorreo("katherine@vibrafit.com"))
                .thenReturn(usuarioPrueba);

        // 2. WHEN: Llamamos al método EXACTO de tu servicio: findByCorreo
        Usuario resultado = usuarioService.findByCorreo("katherine@vibrafit.com");

        // 3. THEN: Comprobamos que la lógica de negocio no altere los datos
        assertNotNull(resultado, "El objeto recuperado no debe ser nulo");
        assertEquals("Katherine Duarte", resultado.getNombre(), "El nombre debe ser igual al esperado");
        assertEquals(1L, resultado.getUsuarioId(), "El ID debe coincidir");

        // Verificación de integridad: asegurar que se llamó al repositorio 1 vez
        verify(usuarioRepository, times(1)).findByCorreo("katherine@vibrafit.com");
    }
}