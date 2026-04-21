# 🏋️ VibraFit - Backend API

Backend del sistema de gestión de gimnasio **VibraFit**, desarrollado con Spring Boot 3 y Java 21. Soporta múltiples clientes: aplicación web (Thymeleaf), app Angular y app Android.

---

<img width="600" height="300" alt="image" src="https://github.com/user-attachments/assets/356b1de0-7ea5-4ffe-ad74-be7dfecc120d" />
<img width="600" height="300" alt="image" src="https://github.com/user-attachments/assets/0e23cbb7-d439-4545-a123-7dd5a7405b95" />
<img width="600" height="300" alt="image" src="https://github.com/user-attachments/assets/3ead93d0-99d7-4804-9ec0-7f56f4a2c2ba" />
<img width="600" height="300" alt="image" src="https://github.com/user-attachments/assets/1c98fb77-6bc3-4b4d-8e05-bbf6718da556" />



## 🚀 Tecnologías

| Tecnología | Versión |
|---|---|
| Java | 21 |
| Spring Boot | 3.5.6 |
| Spring Security | 6.x |
| Spring Data JPA | 3.x |
| MySQL | 8.x |
| JWT (jjwt) | 0.12.3 |
| Thymeleaf | 3.x |
| Maven | 3.x |

---





## 📁 Estructura del Proyecto
```
src/main/java/com/sena/appspringboot/app/gym/
├── config/
│   └── SecurityConfig.java          # Configuración de seguridad multiclient
├── controller/
│   ├── AuthController.java          # API REST: login JWT
│   ├── UsuarioController.java       # API REST: CRUD usuarios
│   ├── UsuarioWebController.java    # Web: vistas Thymeleaf
│   └── LoginController.java        # Web: login Thymeleaf
├── model/
│   ├── Usuario.java
│   ├── Rol.java
│   ├── Ejercicio.java
│   ├── Rutina.java
│   └── EjercicioRutina.java
├── repository/
│   ├── IUsuarioRepository.java
│   └── IRolRepository.java
├── security/
│   ├── JwtUtil.java                 # Generación y validación JWT
│   ├── JwtAuthFilter.java           # Filtro JWT para API
│   ├── CustomUserDetailsService.java
│   └── CustomAuthenticationSuccessHandler.java
└── service/
    ├── UsuarioService.java
    └── RolService.java
```

---

## ⚙️ Configuración

### `application.properties`
```properties
spring.application.name=app-gym
spring.datasource.url=jdbc:mysql://localhost:3306/vibrafitapp
spring.datasource.username=root
spring.datasource.password=TU_PASSWORD
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=vibrafit2024_clave_super_secreta_segura
jwt.expiration=86400000
```

---

## 🔐 Seguridad - Arquitectura Multiclient

El sistema maneja dos tipos de autenticación en una sola cadena de seguridad:

| Cliente | Autenticación | Rutas |
|---|---|---|
| Web (Thymeleaf) | Sesión HTTP | `/usuarios/**`, `/admin/**` |
| Angular / Android | JWT Bearer Token | `/api/**` |

### Flujo JWT
```
POST /api/auth/login
→ Retorna token JWT + datos del usuario
→ Token válido por 24 horas
→ Incluir en headers: Authorization: Bearer <token>
```

---

## 📡 Endpoints API REST

### Autenticación
```
POST /api/auth/login
Body: { "correo": "string", "password": "string" }
Response: { "token": "jwt", "usuario": { ... } }
```

### Usuarios
```
GET    /api/usuarios          → Listar todos
GET    /api/usuarios/{id}     → Obtener por ID
GET    /api/usuarios/rol/{rol}→ Filtrar por rol
POST   /api/usuarios          → Crear usuario
PUT    /api/usuarios/{id}     → Editar usuario
DELETE /api/usuarios/{id}     → Eliminar usuario
```

---

## 👥 Roles del Sistema

| Rol | Descripción |
|---|---|
| `ADMINISTRADOR` | Acceso total al sistema |
| `ENTRENADOR` | Gestión de clientes y rutinas |
| `CLIENTE` | Acceso a sus rutinas y progreso |

---

## 🗄️ Base de Datos
```sql
-- Crear base de datos
CREATE DATABASE vibrafitapp;
```

Las tablas se crean automáticamente con `ddl-auto=update`.

### Tablas principales
- `usuarios` — usuarios del sistema
- `roles` — roles (ADMINISTRADOR, ENTRENADOR, CLIENTE)
- `rutinas` — rutinas de entrenamiento
- `ejercicios` — catálogo de ejercicios
- `ejercicio_rutina` — relación ejercicio-rutina

---

## ▶️ Cómo Ejecutar

### Prerrequisitos
- Java 21
- MySQL 8
- Maven 3.x

### Pasos
```bash
# 1. Clonar repositorio
git clone https://github.com/kathBD/App_gym.git

# 2. Crear base de datos MySQL
CREATE DATABASE vibrafitapp;

# 3. Configurar application.properties con tus credenciales

# 4. Ejecutar
mvn spring-boot:run
```

El servidor inicia en `http://localhost:8080/inicio`


## 👩‍💻 Desarrollado por

**Katherine** 
