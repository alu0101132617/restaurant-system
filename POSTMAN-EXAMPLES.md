# 📮 Peticiones de Ejemplo para Postman

## Base URL
```
http://localhost:8080
```

---

## 1️⃣ GET - Información del Restaurante

### Request
**Method:** `GET`  
**URL:** `http://localhost:8080/api/info`  
**Headers:** Ninguno necesario

### Response esperada (200 OK)
```json
{
    "name": "Mi Restaurante",
    "address": "Calle Falsa 123"
}
```

---

## 2️⃣ GET - Obtener Menú Completo

### Request
**Method:** `GET`  
**URL:** `http://localhost:8080/api/menu`  
**Headers:** Ninguno necesario

### Response esperada (200 OK)
```json
{
    "mains": [
        {
            "id": 1,
            "name": "Buratta Pizza",
            "price": 54
        },
        {
            "id": 2,
            "name": "Pink Pasta",
            "price": 12
        },
        {
            "id": 3,
            "name": "Rosemary Salmon",
            "price": 30
        },
        {
            "id": 4,
            "name": "Spaghetti",
            "price": 8
        },
        {
            "id": 5,
            "name": "Crown Pizza",
            "price": 50
        }
    ],
    "appetizers": [
        {
            "id": 1,
            "name": "Truffel Fries",
            "price": 23
        },
        {
            "id": 2,
            "name": "Molten Chocolate",
            "price": 12
        },
        {
            "id": 3,
            "name": "Mac&Cheese Balls",
            "price": 12
        },
        {
            "id": 4,
            "name": "Dynamite Shrimp",
            "price": 32
        },
        {
            "id": 5,
            "name": "Kheera",
            "price": 10
        }
    ],
    "drinks": [
        {
            "id": 1,
            "name": "cola",
            "price": 6
        },
        {
            "id": 2,
            "name": "7up",
            "price": 6
        },
        {
            "id": 3,
            "name": "orange juice",
            "price": 10
        },
        {
            "id": 4,
            "name": "mojito",
            "price": 14
        },
        {
            "id": 5,
            "name": "Red Bull",
            "price": 8
        }
    ]
}
```

---

## 3️⃣ POST - Login de Cajero (Éxito)

### Request
**Method:** `POST`  
**URL:** `http://localhost:8080/api/login`  
**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
    "name": "abdualmajeed"
}
```

### Response esperada (200 OK)
```json
{
    "status": "ok",
    "welcome": "Welcome abdualmajeed"
}
```

---

## 4️⃣ POST - Login de Cajero (Cajero No Existente)

### Request
**Method:** `POST`  
**URL:** `http://localhost:8080/api/login`  
**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
    "name": "usuario_falso"
}
```

### Response esperada (200 OK)
```json
{
    "status": "error",
    "message": "unknown cashier"
}
```

---

## 5️⃣ POST - Login de Cajero (Otro cajero válido)

### Request
**Method:** `POST`  
**URL:** `http://localhost:8080/api/login`  
**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
    "name": "abdualrahman"
}
```

### Response esperada (200 OK)
```json
{
    "status": "ok",
    "welcome": "Welcome abdualrahman"
}
```

---

## 6️⃣ POST - Login sin nombre (Error de validación)

### Request
**Method:** `POST`  
**URL:** `http://localhost:8080/api/login`  
**Headers:**
```
Content-Type: application/json
```

**Body (raw JSON):**
```json
{
    "name": ""
}
```

### Response esperada (400 Bad Request)
```json
{
    "status": "error",
    "message": "missing name"
}
```

---

## 📝 Notas para Postman

### Cómo crear las peticiones:

1. **Crear una nueva Collection** llamada "Restaurant API"

2. **Para peticiones GET:**
   - Click en "New Request"
   - Selecciona método `GET`
   - Pega la URL
   - Click en "Send"

3. **Para peticiones POST:**
   - Click en "New Request"
   - Selecciona método `POST`
   - Pega la URL
   - Ve a la pestaña "Headers" y añade:
     - Key: `Content-Type`
     - Value: `application/json`
   - Ve a la pestaña "Body"
   - Selecciona "raw" y "JSON"
   - Pega el JSON del body
   - Click en "Send"

### Cashiers válidos en la BD:
- `abdualmajeed` (ID: 4231, Salary: 7000)
- `abdualrahman` (ID: 4232, Salary: 7000)

### Variables de entorno (Opcional):
Puedes crear una variable de entorno en Postman:
- Variable: `base_url`
- Value: `http://localhost:8080`

Luego usar: `{{base_url}}/api/info`

---

## 🔍 Verificación de la Base de Datos

Si quieres verificar los datos directamente en MySQL:

```sql
-- Ver todos los cashiers
SELECT * FROM Cashier;

-- Ver el menú completo
SELECT * FROM maincourse;
SELECT * FROM appetizers;
SELECT * FROM drinks;
```

Ejecuta con:
```
docker exec -it restaurant-mysql mysql -uroot -prootpass -e "USE project3; SELECT * FROM Cashier;"
```

