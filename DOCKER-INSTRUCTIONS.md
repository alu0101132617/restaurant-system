# Instrucciones para levantar la base de datos MySQL con Docker

## Pasos para ejecutar:

### 1. Abrir PowerShell o CMD en el directorio del proyecto
```bash
cd C:\Users\Hecto\Desktop\Restaurant-System\restaurant-system
```

### 2. Levantar el contenedor MySQL con Docker Compose
```bash
docker compose up -d
```

### 3. Verificar que el contenedor está corriendo
```bash
docker ps
```

Deberías ver un contenedor llamado `restaurant-mysql` en la lista.

### 4. Ver los logs de inicialización (opcional)
```bash
docker compose logs mysql
```

### 5. Verificar que la base de datos `project3` fue creada
```bash
docker exec -it restaurant-mysql mysql -uroot -prootpass -e "SHOW DATABASES;"
```

Deberías ver `project3` en la lista de bases de datos.

### 6. Verificar las tablas creadas
```bash
docker exec -it restaurant-mysql mysql -uroot -prootpass -e "USE project3; SHOW TABLES;"
```

Deberías ver tablas como: `maincourse`, `appetizers`, `drinks`, `Cashier`, `Chef`, `Manager`, etc.

### 7. Consultar datos de ejemplo
```bash
docker exec -it restaurant-mysql mysql -uroot -prootpass -e "USE project3; SELECT * FROM maincourse;"
docker exec -it restaurant-mysql mysql -uroot -prootpass -e "USE project3; SELECT * FROM appetizers;"
docker exec -it restaurant-mysql mysql -uroot -prootpass -e "USE project3; SELECT * FROM drinks;"
docker exec -it restaurant-mysql mysql -uroot -prootpass -e "USE project3; SELECT * FROM Cashier;"
```

## Detener el contenedor
```bash
docker compose down
```

## Eliminar el contenedor y volúmenes (datos completos)
```bash
docker compose down -v
```

## Credenciales de la base de datos

- **Host:** localhost
- **Puerto:** 3306
- **Usuario root:** root
- **Password root:** rootpass
- **Usuario app:** restaurant
- **Password app:** secret
- **Base de datos:** project3

## Compilar y ejecutar el servidor Spring Boot

Una vez que MySQL esté corriendo:

```bash
# Compilar el módulo server
mvn -f server/pom.xml clean package -DskipTests

# Ejecutar el servidor
mvn -f server/pom.xml spring-boot:run
```

El servidor estará disponible en http://localhost:8080

## Endpoints disponibles

- `GET http://localhost:8080/api/info` - Información del restaurante
- `POST http://localhost:8080/api/login` - Login de cajero (body: {"name":"abdualmajeed"})
- `GET http://localhost:8080/api/menu` - Menú completo (mains, appetizers, drinks)

## Ejecutar el ApiClient independiente

```bash
java -cp target\RestaurantSystem-1.0-SNAPSHOT.jar es.ull.esit.app.middleware.ApiClient http://localhost:8080
```

