# Black Plate: Restaurant Management System

![Picture2](https://github.com/user-attachments/assets/31e73cdd-dd44-498a-bb92-486e502745be)

[![Quality gate](https://sonarcloud.io/api/project_badges/quality_gate?project=alu0101132617_restaurant-system)](https://sonarcloud.io/summary/new_code?id=alu0101132617_restaurant-system)
[![SonarQube Cloud](https://sonarcloud.io/images/project_badges/sonarcloud-highlight.svg)](https://sonarcloud.io/summary/new_code?id=alu0101132617_restaurant-system)

[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=alu0101132617_restaurant-system&metric=coverage)](https://sonarcloud.io/summary/new_code?id=alu0101132617_restaurant-system)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=alu0101132617_restaurant-system&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=alu0101132617_restaurant-system)

[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=alu0101132617_restaurant-system&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=alu0101132617_restaurant-system)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=alu0101132617_restaurant-system&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=alu0101132617_restaurant-system)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=alu0101132617_restaurant-system&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=alu0101132617_restaurant-system)

[![Vulnerabilities](https://sonarcloud.io/api/project_badges/measure?project=alu0101132617_restaurant-system&metric=vulnerabilities)](https://sonarcloud.io/summary/new_code?id=alu0101132617_restaurant-system)
[![Bugs](https://sonarcloud.io/api/project_badges/measure?project=alu0101132617_restaurant-system&metric=bugs)](https://sonarcloud.io/summary/new_code?id=alu0101132617_restaurant-system)
[![Code Smells](https://sonarcloud.io/api/project_badges/measure?project=alu0101132617_restaurant-system&metric=code_smells)](https://sonarcloud.io/summary/new_code?id=alu0101132617_restaurant-system)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=alu0101132617_restaurant-system&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=alu0101132617_restaurant-system)

## 1. Resumen del proyecto
**Restaurant System** es una aplicación diseñada para gestionar las operaciones esenciales de un restaurante, centrándose principalmente en:
- Gestión de la carta: actualización de productos (bebidas, aperitivos y platos principales) y precios.
- Autenticación básica del personal (cajeros / administradores).
- Toma de pedidos y facturación básica (generación de cuentas y recibos).

El proyecto sigue una **arquitectura de tres capas**, lo que favorece la escalabilidad, el mantenimiento y la separación de responsabilidades:

- **Middleware REST (Spring Boot)**  
  Gestiona la lógica de negocio, la persistencia y el acceso seguro a la base de datos.

- **Cliente de escritorio (Java Swing)**  
  Proporciona una interfaz gráfica destinada al personal del restaurante para la gestión de productos y la operación diaria.

- **Comunicación JSON (Jackson)**  
  Permite la serialización y deserialización de objetos entre cliente y servidor mediante una API RESTful.


## 2. Stack tecnológico
El proyecto se apoya en una arquitectura orientada a servicios con el siguiente stack:
### Backend (Middleware)
- Lenguaje: Java
- Framework: Spring Boot
- API: RESTful (JSON)
- ORM: Spring Data JPA (Hibernate)
- Base de datos: MySQL
- Serialización: Jackson
- Build: Maven
- Tests: JUnit 5

### Frontend
- Tipo: Aplicación de escritorio
- Tecnología: Java Swing

### Infraestructura
- Contenedores: Docker
- Orquestación local: Docker Compose


## 3. Estructura del repositorio
La organización del repositorio refleja la separación entre cliente y servidor.

| Directorio | Contenido                  | Función                  |
| ---------- | -------------------------- | ------------------------ |
| `server`   | Backend REST (Spring Boot) | Lógica y persistencia    |
| `src`      | Cliente Swing              | UI y cliente HTTP        |
| `db`       | Scripts SQL (00–05)        | Esquema, triggers, datos |
| `scripts`  | `launch-system.sh`         | Arranque asistido        |

Subcarpetas clave backend:
- [server/src/main/java/es/ull/esit/server/controller](server/src/main/java/es/ull/esit/server/controller)
- [server/src/main/java/es/ull/esit/server/middleware/model](server/src/main/java/es/ull/esit/server/middleware/model)
- [server/src/main/java/es/ull/esit/server/repo](server/src/main/java/es/ull/esit/server/repo)
- [server/src/main/java/es/ull/esit/server/config/SecurityConfig.java](server/src/main/java/es/ull/esit/server/config/SecurityConfig.java)


## 4. Instalación y ejecución local

Prerequisitos:
- JDK 17
- Maven 3.9+
- Docker y Docker Compose

1) Clonar el repositorio
```bash
git clone https://github.com/alu0101132617/restaurant-system.git
cd restaurant-system
```

2) Levantar la base de datos (MySQL)
```bash
docker compose up -d
# Esperar a que finalice la inicialización (db/*.sql)
docker logs restaurant-mysql
```

3) Arrancar el backend (Spring Boot)
```bash
mvn -f server clean spring-boot:run
```

4) Verificar salud de la API
```bash
curl http://localhost:8080/api/health
```

5) Lanzar el frontend (Java Swing)

Ejecutar desde IDE (IntelliJ/NetBeans/Eclipse) la clase principal en `src/main/java` con la API disponible en `http://localhost:8080`.


### Alternativa para arrancar BD, backend and fronted automáticamente:

`scripts/launch-system.sh` para arrancar DB y backend automáticamente.

### Configuración por defecto (server/src/main/resources/application.properties):
- DB_URL: `jdbc:mysql://localhost:3306/project3`
- DB_USER: `restaurant`
- DB_PASS: `secret`
- API base: `http://localhost:8080`


## 5. Base de datos: modelo y migraciones
La persistencia de datos se gestiona mediante **Spring Data JPA** (Hibernate) sobre una base de datos **MySQL**, evitando la necesidad de escribir consultas SQL directamente en el código del backend.

### Modelo de entidades
| Entidad | Campos principales | Descripción |
|-------|------------------|-------------|
| `Drink` | `drinksId`, `itemDrinks`, `drinksPrice` | Representa las bebidas disponibles en la carta |
| `Appetizer` | `appetizersId`, `itemAppetizers`, `appetizersPrice` | Representa los aperitivos |
| `MainCourse` | `mainCourseId`, `itemMainCourse`, `mainCoursePrice` | Representa los platos principales |
| `Cashier` | `id`, `name` | Representa al personal (cajeros / administradores) |

### Migraciones y esquema

- La inicialización de la base de datos se realiza de manera automática con Docker Compose montando:
- [db/00-create-db.sql](db/00-create-db.sql)
- [db/01-tables.sql](db/01-tables.sql)
- [db/02-procedures.sql](db/02-procedures.sql)
- [db/03-triggers.sql](db/03-triggers.sql)
- [db/04-privileges.sql](db/04-privileges.sql)
- [db/05-data.sql](db/05-data.sql)

---

## 6. API — Endpoints principales

La API REST está implementada con **Spring Boot** y se expone en la siguiente URL base: http://localhost:8080/api

### Autenticación y personal
- `POST /api/login`
- `GET /api/cashiers`
- `GET /api/cashiers/{id}`
- `GET /api/cashiers/name/{name}`
- `PUT /api/cashiers/{id}`

### Gestión de productos (menú)

CRUD completo para cada tipo de producto:
- Bebidas: `/drinks`
- Aperitivos: `/appetizers`
- Platos principales: `/maincourses`

Cada recurso admite los métodos:
- `GET` (listar u obtener por ID)
- `POST` (crear)
- `PUT` (actualizar)
- `DELETE` (eliminar)

### Otros:
- `GET /api/menu`
- `GET /api/health`  |  `GET /api/db-check`


## 7. Frontend (Cliente)
El frontend es una aplicación de escritorio desarrollada en **Java Swing**, diseñada para ser ligera y fácil de usar por el personal del restaurante.

### Pantallas principales
- `Login`: inicio de sesión del usuario (admin o cashier).
- `AdminLogin`: muestra opciones del usuario con rol 'admi': _Update Prices_ o _Menu_.
- `CashierLogin`: muestra opciones del usuario con rol 'cashier': _About us_ o _Menu_.
- `AdminProducts`: se accede desde el botón _Update Prices_. Permite gestionar productos y precios de la carta.
- `Order`: se accede desde el botón _Menu_. Permite gacturar pedidos y generar recibos.

### Comunicación con el backend
- Protocolo: HTTP/REST.
- Cliente HTTP: `java.net.http.HttpClient`.
- Serialización/deserialización de datos: Jackson.
- La lógica de comunicación está centralizada en la clase `ApiClient`.

## 8. Autenticación y autorización
- Autenticación básica: inicio de seción vía POST `/api/login` (valida contra `users`).
- Roles simples:
  - **admin**
  - **cashier**
- La autorización se gestiona a nivel de interfaz. La API no mantiene sesión ni emite tokens, responde con datos mínimos del usuario y su rol.
- Seguridad avanzada (JWT, OAuth, cifrado de contraseñas) pendiente de implementación

### Flujo de autenticación
  1) El cliente envía credenciales.
  2) El backend comprueba usuario/contraseña en `users`.
  3) Respuestas:
     - 200 OK: credenciales válidas.
     - 401 Unauthorized: credenciales inválidas.
---

### 9. Tests y calidad
- Pruebas (JUnit 5)
  - Backend: `mvn -f server clean test`
  - Cliente: `mvn -DskipTests=false test` (o desde el IDE)

- Análisis estático (informes en server/target/site)
  - Checkstyle: `mvn -f server checkstyle:check`
  - PMD: `mvn -f server pmd:check`
  - SpotBugs: `mvn -f server com.github.spotbugs:spotbugs-maven-plugin:check`
  - Dependency-Check: `mvn -f server org.owasp:dependency-check-maven:check`

- Cobertura (JaCoCo)
  - `mvn -f server clean test jacoco:report`
  - Informe: `server/target/site/jacoco/index.html`

- SonarCloud (requiere SONAR_TOKEN)
  - `export SONAR_TOKEN=<token>`
  - `mvn -f server -DskipTests=false verify sonar:sonar -Dsonar.host.url=https://sonarcloud.io -Dsonar.projectKey=alu0101132617_restaurant-system -Dsonar.organization=<org> -Dsonar.token="$SONAR_TOKEN"`
---

## 10. Docker y despliegue
- MySQL se ejecuta dentro de un contenedor Docker (ver [docker-compose.yml](docker-compose.yml)).
- Scripts SQL montados como `docker-entrypoint-*.d`.
- El backend y el frontend se ejecutan mediante Maven.
- El despliegue se automatiza en el script `launch-system.sh`.




