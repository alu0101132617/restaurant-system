# 🧪 Cómo Testear el Middleware

## Prerequisitos
✅ Docker Desktop corriendo
✅ Maven instalado
✅ Java 11+ instalado

## Paso 1: Levantar la base de datos MySQL

Abre PowerShell o CMD en la carpeta del proyecto y ejecuta:

```
docker compose up -d
```

Espera 10-15 segundos para que MySQL se inicialice.

Verifica que está corriendo:
```
docker ps
```

## Paso 2: Compilar y arrancar el servidor

### Opción A: Usando el script automático (Recomendado)
```
start-server.bat
```

### Opción B: Manual
```
mvn -f server\pom.xml clean package -DskipTests
mvn -f server\pom.xml spring-boot:run
```

Espera a ver este mensaje:
```
Started RestaurantApplication in X.XXX seconds
```

## Paso 3: Testear los endpoints

Abre OTRA terminal (PowerShell o CMD) y ejecuta:

### Opción A: Script automático de tests (Recomendado)

**Para PowerShell:**
```powershell
.\test-middleware.ps1
```

**Para CMD:**
```
test-middleware.bat
```

### Opción B: Tests manuales con curl

```
curl http://localhost:8080/api/info
curl http://localhost:8080/api/menu
curl -X POST http://localhost:8080/api/login -H "Content-Type: application/json" -d "{\"name\":\"abdualmajeed\"}"
```

### Opción C: Tests manuales con PowerShell

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/info" -Method GET
Invoke-RestMethod -Uri "http://localhost:8080/api/menu" -Method GET
$body = @{name="abdualmajeed"} | ConvertTo-Json
Invoke-RestMethod -Uri "http://localhost:8080/api/login" -Method POST -Body $body -ContentType "application/json"
```

## Respuestas esperadas

### GET /api/info
```json
{
  "name": "Mi Restaurante",
  "address": "Calle Falsa 123"
}
```

### GET /api/menu
```json
{
  "mains": [
    {"id": 1, "name": "Buratta Pizza", "price": 54},
    {"id": 2, "name": "Pink Pasta", "price": 12},
    ...
  ],
  "appetizers": [...],
  "drinks": [...]
}
```

### POST /api/login (cashier existente)
```json
{
  "status": "ok",
  "welcome": "Welcome abdualmajeed"
}
```

### POST /api/login (cashier no existente)
```json
{
  "status": "error",
  "message": "unknown cashier"
}
```

## Cashiers válidos en la base de datos

- `abdualmajeed` (ID: 4231)
- `abdualrahman` (ID: 4232)

## Detener todo

1. Detener el servidor: `Ctrl + C` en la terminal del servidor
2. Detener MySQL: `docker compose down`

## Troubleshooting

**Error: Connection refused**
- Verifica que MySQL está corriendo: `docker ps`
- Verifica que el servidor Spring Boot arrancó correctamente (busca "Started RestaurantApplication")

**Error: Unknown database 'project3'**
- El script init.sql no se ejecutó. Elimina el volumen y vuelve a levantar:
  ```
  docker compose down -v
  docker compose up -d
  ```

**Error: Cannot find symbol (compilación)**
- Asegúrate de tener Java 11 o superior: `java -version`
- Limpia el proyecto: `mvn clean`

