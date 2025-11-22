# 🚀 SERVIDOR LANZADO - Guía Rápida de Testing

## ✅ Estado del Servidor
El servidor Spring Boot debería estar arrancándose en: **http://localhost:8080**

Espera a ver este mensaje en la consola:
```
Started RestaurantApplication in X.XXX seconds
```

---

## 📮 PETICIONES RÁPIDAS PARA POSTMAN

### 🔹 Opción 1: Importar Colección (MÁS FÁCIL)

1. Abre Postman
2. Click en "Import"
3. Selecciona el archivo: `Restaurant-API.postman_collection.json`
4. ¡Listo! Tendrás 6 peticiones pre-configuradas

---

### 🔹 Opción 2: Crear Manualmente

#### 1️⃣ GET - Info del Restaurante
```
GET http://localhost:8080/api/info
```

#### 2️⃣ GET - Menú Completo
```
GET http://localhost:8080/api/menu
```

#### 3️⃣ POST - Login Exitoso
```
POST http://localhost:8080/api/login
Content-Type: application/json

Body:
{
    "name": "abdualmajeed"
}
```

#### 4️⃣ POST - Login con Otro Cajero
```
POST http://localhost:8080/api/login
Content-Type: application/json

Body:
{
    "name": "abdualrahman"
}
```

#### 5️⃣ POST - Login Fallido (Usuario No Existe)
```
POST http://localhost:8080/api/login
Content-Type: application/json

Body:
{
    "name": "usuario_falso"
}
```

#### 6️⃣ POST - Error de Validación (Nombre Vacío)
```
POST http://localhost:8080/api/login
Content-Type: application/json

Body:
{
    "name": ""
}
```

---

## 📊 Respuestas Esperadas

### ✅ GET /api/info
```json
{
    "name": "Mi Restaurante",
    "address": "Calle Falsa 123"
}
```

### ✅ GET /api/menu
```json
{
    "mains": [
        {"id": 1, "name": "Buratta Pizza", "price": 54},
        {"id": 2, "name": "Pink Pasta", "price": 12},
        ...más items
    ],
    "appetizers": [...],
    "drinks": [...]
}
```

### ✅ POST /api/login (Éxito)
```json
{
    "status": "ok",
    "welcome": "Welcome abdualmajeed"
}
```

### ❌ POST /api/login (Error - Usuario No Existe)
```json
{
    "status": "error",
    "message": "unknown cashier"
}
```

### ❌ POST /api/login (Error - Validación)
```json
{
    "status": "error",
    "message": "missing name"
}
```

---

## 🎯 Cajeros Válidos en la BD

Los únicos cajeros que existen en la tabla `Cashier`:
- ✅ `abdualmajeed` (ID: 4231)
- ✅ `abdualrahman` (ID: 4232)

Cualquier otro nombre retornará error.

---

## 🔧 Troubleshooting

### ❌ Error "Connection refused"
- Verifica que el servidor esté corriendo
- Busca el mensaje "Started RestaurantApplication" en la consola

### ❌ Error "Unknown database 'project3'"
```
docker compose down -v
docker compose up -d
```
Espera 15 segundos y vuelve a arrancar el servidor.

### ❌ Error de compilación
```
mvn -f server\pom.xml clean package -DskipTests
```

---

## 📁 Archivos de Referencia

- `POSTMAN-EXAMPLES.md` - Documentación detallada de cada endpoint
- `Restaurant-API.postman_collection.json` - Colección importable de Postman
- `TEST-INSTRUCTIONS.md` - Guía completa de testing

---

## 🎉 ¡Todo Listo!

El middleware está funcionando con:
- ✅ Spring Boot
- ✅ Hibernate/JPA
- ✅ MySQL en Docker
- ✅ Conexión segura a la BD
- ✅ Repositorios y Entidades JPA
- ✅ Endpoints REST funcionales

**¡Empieza a probar en Postman!** 🚀

