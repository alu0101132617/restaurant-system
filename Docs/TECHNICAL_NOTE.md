**Technical Note — Restaurant Server**

Resumen rápido
- **Proyecto**: REST backend para la aplicación Restaurant-System.
- **Ubicación**: `server/` (Spring Boot application).

**Tech Stack**
- **Language**: Java 17
- **Framework**: Spring Boot 3.0.6 (starter parent in `server/pom.xml`)
- **Web**: `spring-boot-starter-web` (REST controllers, Jackson JSON)
- **Security**: `spring-boot-starter-security` (password handling, auth endpoints)
- **Persistence**: Spring Data JPA (`spring-boot-starter-data-jpa`) + Hibernate
- **Connection pool**: HikariCP (bundled with Spring Boot)
- **Database**: MySQL (image in `docker-compose.yml`, connector `mysql-connector-java`)
- **JDBC**: `spring-boot-starter-jdbc` used for DataSource wiring
- **Build**: Maven (`pom.xml`)
- **Containerization**: `docker-compose.yml` provides a `mysql` service and a `server` service for deployment

**Decisiones de diseño**
- API REST organizada por recursos bajo el prefijo `/api`. Cada recurso tiene su propio controlador (separación de responsabilidades).
- Persistencia por **Spring Data JPA** y repositorios: repositorios simples (`findAll`, `findById`, `save`) para mantener el código de acceso a datos minimalista.
- Las entidades `MainCourse`, `Appetizer`, `Drink` usan la misma tabla `Item` con diferentes columnas de id (mapeadas mediante `@IdClass`). Esto facilita que la base de datos centralice ítems del menú, pero obliga a lógica de mapeo específica en Java.
- El esquema de BD se crea por el script `db/init.sql` (no usar `spring.jpa.hibernate.ddl-auto=update` en producción). En `application.properties` se usa `ddl-auto=none` para evitar cambios automáticos.
- Comprobación de salud: hay endpoints de salud y `db-check` que usan el `DataSource` para validar la conexión a la base.
- Seguridad: existe un endpoint `/api/login` que verifica credenciales contra la tabla `users`. Los `PasswordEncoder` se usan para comparar hashes.
- Docker: `docker-compose.yml` define un servicio `mysql` con el script de inicialización y el servicio `server` que depende de él. Las variables de entorno pueden sobreescribir `spring.datasource.*` cuando se ejecuta en Docker.

**Configuración relevante**
- `server/src/main/resources/application.properties` (parcial):
```
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/project3?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=rootpass
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.jpa.hibernate.ddl-auto=none
spring.jpa.show-sql=true
```
- `docker-compose.yml` expone el servicio MySQL en el puerto `3306` y monta `db/init.sql` para inicializar la base.

**Endpoints (documentación rápida)**

Base URL: `http://<host>:8080` (o `http://localhost:8080` en desarrollo)

- Auth
  - POST `/api/login`
    - Request: `{ "username": "user", "password": "secret" }`
    - Response: `200 OK` with `User` object when credentials valid, `401` otherwise.

- Health
  - GET `/api/health` — devuelve estado simple del servicio `{status: "UP", ...}`
  - GET `/api/db-check` — intenta abrir una conexión JDBC y devuelve estado `UP` o `DOWN` con metadatos de la conexión.

- Menu
  - GET `/api/menu` — devuelve un objeto con `mainCourses`, `appetizers`, `drinks` y `totalItems`.

- MainCourse (resource: `/api/maincourse`)
  - GET `/api/maincourse` — lista todos los platos principales. Response: JSON array of `MainCourse`.
  - GET `/api/maincourse/{id}` — obtener plato por id. Response: `200` con objeto o `404`.
  - POST `/api/maincourse` — crear nuevo plato. Body: `MainCourse` JSON. Response: `201 Created` con entidad.
  - PUT `/api/maincourse/{id}` — actualizar plato; `404` si no existe.
  - DELETE `/api/maincourse/{id}` — eliminar plato; `204` si eliminado.

- Appetizer (resource: `/api/appetizers`)
  - GET `/api/appetizers`
  - GET `/api/appetizers/{id}`
  - POST `/api/appetizers`
  - PUT `/api/appetizers/{id}`
  - DELETE `/api/appetizers/{id}`

- Drink (resource: `/api/drinks`)
  - GET `/api/drinks`
  - GET `/api/drinks/{id}`
  - POST `/api/drinks`
  - PUT `/api/drinks/{id}`
  - DELETE `/api/drinks/{id}`

- Cashiers (resource: `/api/cashiers`)
  - GET `/api/cashiers` — lista cajeros
  - GET `/api/cashiers/{id}` — obtener por id
  - GET `/api/cashiers/name/{name}` — obtener por nombre
  - POST `/api/cashiers` — crear nuevo cajero
  - PUT `/api/cashiers/{id}` — actualizar
  - DELETE `/api/cashiers/{id}` — eliminar

Nota: Los esquemas de entidades (nombres de columnas) están mapeados en `server/src/main/java/es/ull/esit/server/middleware/model/*.java`.

**Flujo de usuario (alto nivel)**
- Flujo típico visitante/cliente:
  1. El cliente web/app solicita `GET /api/menu` para mostrar menú completo.
  2. Usuario añade ítems al carrito en el frontend (no hay endpoint explícito de pedidos en este backend; el manejo del carrito/pedido puede estar en frontend o pendiente de implementación).
  3. Si se requiere autenticación, el cliente hace `POST /api/login` con credenciales y mantiene una sesión/token (actualmente la API devuelve el objeto `User` tras validar, la gestión de sesiones/token debe implementarse en el cliente o ampliarse en el servidor).

- Flujo administrador / gestión:
  1. Admin se autentica (`/api/login`).
  2. Admin usa endpoints `POST/PUT/DELETE` en `/api/maincourse`, `/api/appetizers`, `/api/drinks` para mantener el menú.
  3. Gestión de cajeros por `/api/cashiers`.

**Cómo levantar localmente (desarrollo)**
1. Requisitos: Java 17, Maven, Docker (opcional para MySQL).
2. Opciones de base de datos:
   - Usar Docker (recomendado): `docker compose up -d mysql` (levanta MySQL y ejecuta `db/init.sql`).
   - O instalar MySQL localmente y ejecutar `db/init.sql` para crear la base `project3`.
3. Ejecutar servidor:
```powershell
cd server
mvn spring-boot:run
```
4. Alternativa con Docker: `docker compose up -d` levantará `mysql` y (si has construido la imagen) el servicio `server`.

**Notas y recomendaciones**
- Actualmente la conexión en `application.properties` apunta a `localhost:3306`. Bajo Docker Compose el servicio `server` usa la variable `SPRING_DATASOURCE_URL` apuntando a `jdbc:mysql://mysql:3306/project3...`.
- Si necesita soporte para pedidos (orders), se sugiere añadir un recurso `Order` con su propia tabla y endpoints para crear y consultar pedidos.
- Añadir autenticación basada en tokens (JWT) si la aplicación va a exponer APIs públicas o tener un frontend desacoplado.
- Añadir test de integración que arranque una base en memoria (H2) o use Testcontainers para validar endpoints y repositorios.

---
Document generated by developer request. Para ampliar: dime si quieres que incluya ejemplos de request/response concretos (JSON), o que agregue un diagrama del flujo.
