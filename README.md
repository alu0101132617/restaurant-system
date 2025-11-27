# 🍽️ Black Plate — Restaurant Management System (Java + Maven + MySQL)

Sistema de gestión de restaurantes basado en Java, Maven, MySQL y arquitectura de 3 capas (Cliente – Middleware – Base de Datos).  
Incluye un middleware REST (Spring Boot) y una aplicación de escritorio en Java Swing para Cajeros y Administradores.

---

# 📌 Resumen del Proyecto

Black Plate permite gestionar las operaciones esenciales de un restaurante:

- Gestión del menú (bebidas, aperitivos, platos principales)
- Actualización de precios por el Administrador
- Toma de pedidos por el Cajero
- Generación y almacenamiento de recibos
- Control básico de usuarios (Cajeros)

El proyecto está dividido en dos módulos principales:

- Cliente (Java Swing)
- Servidor (Spring Boot API REST)

Se comunican mediante solicitudes HTTP y JSON (Jackson).

---

# 🎯 Objetivos del Sistema

- Facilitar la gestión operativa del restaurante
- Permitir CRUD del menú
- Controlar usuarios y roles básicos (Admin/Cajero)
- Reducir tareas manuales y errores humanos
- Sentar bases para una arquitectura escalable

Extensiones futuras sugeridas:
- Estados de pedidos (pendiente, servido…)
- Analítica de ventas
- Integración con POS
- Notificaciones en tiempo real

---

# 🧱 Arquitectura General

Cliente Java Swing <—> Middleware Spring Boot (API REST) <—> MySQL

- Cliente: interfaz de usuario para Admin y Cajero
- Middleware: maneja la lógica de negocio y persistencia
- MySQL: almacén principal de datos

---

# 🗂️ Estructura del Repositorio

restaurant-system/
│
├── server/                → Middleware REST (Spring Boot)
│   ├── controller/        → Endpoints HTTP /api/*
│   ├── model/             → Entidades JPA
│   └── repo/              → Repositorios (Spring Data JPA)
│
├── src/                   → Cliente Java Swing
│   ├── app/               → Ventanas (Login, Frame1, adminProducts)
│   └── middleware/        → ApiClient.java (HTTP client)
│
├── db/
│   └── init.sql           → Esquema inicial + datos base
│
└── docs/                  → Diagramas (UML, BBDD) y documentación

---

# 🖥️ Interfaz Gráfica (GUI)

Pantallas principales:

- Login (Admin/Cajero)
- Panel de Admin (gestión de precios)
- Panel de Cajero (toma de pedidos)
- Generación de recibos
- Vista “Acerca de”

Las imágenes del proyecto se encuentran en GitHub (user-attachments).

---

# 📦 Instalación y Ejecución

## ✔ Requisitos
- JDK 17 o superior
- Maven 3+
- Docker + Docker Compose
- Git

## 1. Clonar repositorio
```
git clone https://github.com/alu0101132617/restaurant-system.git
cd restaurant-system
```

## 2. Método recomendado: script automático

```
chmod +x scripts/launch-system.sh
bash scripts/launch-system.sh
```

Este script:
- Levanta MySQL en Docker
- Ejecuta `db/init.sql`
- Inicia el servidor Spring Boot
- Comprueba el endpoint `/api/health`
- Lanza el cliente Java Swing

## 3. Método manual

### A) Levantar MySQL
```
docker compose up -d mysql
```

### B) Iniciar Middleware (Spring Boot)
```
cd server
mvn clean spring-boot:run
```

Disponible en: http://localhost:8080/api/

### C) Iniciar Cliente Swing
```
cd ..
mvn -Dexec.mainClass=es.ull.esit.app.Main exec:java
```

---

# 🗄️ Base de Datos

## Tablas principales
- Drink (bebidas)
- Appetizer (aperitivos)
- MainCourse (platos principales)
- Cashier (usuarios)

## Script de inicialización
El esquema está en:

```
db/init.sql
```

---

# 🔌 API REST (Middleware)

Base URL:
```
http://localhost:8080/api/
```

## Autenticación

### POST /api/login
Verifica la existencia del cajero por nombre.

## Gestión de Cajeros
```
GET    /api/cashiers
POST   /api/cashiers
PUT    /api/cashiers/{id}
DELETE /api/cashiers/{id}
```

## Gestión del Menú

### Bebidas:
```
GET    /api/drinks
POST   /api/drinks
GET    /api/drinks/{id}
PUT    /api/drinks/{id}
DELETE /api/drinks/{id}
```

### Aperitivos:
```
/api/appetizers   (mismos métodos)
```

### Platos principales:
```
/api/maincourses  (mismos métodos)
```

---

# 📬 Contacto
Para soporte o mejoras, abrir un Issue en el repositorio.

---

# 📚 Wiki completa
https://github.com/alu0101132617/restaurant-system/wiki

