<h1> Black Plate: Restaurant Management System using Maven Java </h1>

<img width="468" alt="Picture2" src="https://github.com/user-attachments/assets/31e73cdd-dd44-498a-bb92-486e502745be">  

[![Quality gate](http://localhost:9000/api/project_badges/quality_gate?project=RestaurantSystem&token=sqb_d4b8129844e80ce55ab6731a3a7e958499353208)](http://localhost:9000/dashboard?id=RestaurantSystem)

<h2> About </h2> 

The profession of managing a restaurant. It includes the principles of OOP and using Java function of planning, organizing, staffing, directing, developing an attitude in food and beverage control systems, and efficiently and effectively planning menus at profitable prices, taking into consideration constraints and others.

<h2>More info on the wiki</h2>
https://github.com/alu0101132617/restaurant-system/wiki

<h2> Tools used: </h2>

- Java Development Kit (JDK)
- Maven
- JUnit (for testing)

<h2> Database: </h2>

- MySql

<h2>  Functionality: </h2>

- Increases operational efficiency.

- Helps the restaurant manager to manage the restaurant more effectively and efficiently by computerizing Meal Ordering, Cart, and Restaurant Management Accounting.
  
- Avoids paperwork.
  
- Simple to learn and easy to use.


<h2> Clarification of important information: </h2>

The system is between the customer and waiter in the restaurant and it's not a virtual system. The waiter takes customer orders. Our system will facilitate the process of taking orders.


<h2> Application's GUI: </h2>
<img width="922" alt="Screen Shot 2024-08-19 at 1 52 45 AM" src="https://github.com/user-attachments/assets/5710f1e5-2de4-4c66-83e5-042630acbe26">
<br>
<h3> 1. Logged in as an Admin: (Username: admin, Password: admin) </h3>
<img width="922" alt="Screen Shot 2024-08-19 at 1 53 22 AM" src="https://github.com/user-attachments/assets/587ec9d4-050f-4687-86ab-3f217948c09f">
<br>
<h3> Update Prices choice: </h3>
<img width="1243" alt="Screen Shot 2024-08-19 at 1 54 13 AM" src="https://github.com/user-attachments/assets/49bb9ba4-899c-4e66-afcc-a116a98d95d9">
<br>
<h3> Menu choice to see the previous updates: </h3>
<img width="1243" alt="Screen Shot 2024-08-19 at 1 53 57 AM" src="https://github.com/user-attachments/assets/6671a0e2-da0e-4d92-9703-7d0925795261">
<br>
<h3> 2. Logged in as a Cashier: (Any username & password) </h3>
<img width="925" alt="Screen Shot 2024-08-19 at 3 10 27 AM" src="https://github.com/user-attachments/assets/6984412b-8897-488e-a898-0c0fbdbf01ef">
<br>
<h3> Menu choice: </h3>
<img width="1243" alt="Screen Shot 2024-08-19 at 1 53 57 AM" src="https://github.com/user-attachments/assets/6671a0e2-da0e-4d92-9703-7d0925795261">
<br>
<h3> Save Receipt: </h3>
<img width="1235" alt="Screen Shot 2024-08-19 at 1 56 06 AM" src="https://github.com/user-attachments/assets/8be21f86-1e68-4605-8606-c14e63853696">
<br>
<h3> All Receipt that has been saved, will be shown in the JavaApplication2 UPDATED file: </h3>
<img width="391" alt="Screen Shot 2024-08-19 at 3 09 02 AM" src="https://github.com/user-attachments/assets/22b8a887-f432-4cc2-95df-cd15ceeb0767">
<br>
<h3> About us choice: </h3>
<img width="1235" alt="Screen Shot 2024-08-19 at 1 55 38 AM" src="https://github.com/user-attachments/assets/bdf9d36f-739e-49d7-b9ce-5655d3943a0a">


<h2> Database Schema: </h2>


<img width="468" alt="Picture2" src="https://github.com/user-attachments/assets/3d2ef6c9-9aed-42c0-8d1e-0f35134fe680">
<img width="468" alt="Picture3" src="https://github.com/user-attachments/assets/9ebc7997-7673-42eb-9141-d3669a6790b2">
 
 <h2> Classes UML: </h2>
 <img width="468" alt="Picture4" src="https://github.com/user-attachments/assets/829b58be-f702-4479-9cd4-cf9be6285803">

## Configuración de la BBDD (MySQL / MariaDB)
Antes de ejecutar la aplicación, se debe tener instalado MySQL ó MariaDB y haber creado la base de datos correspondiente.
0. Asegurarse de que el gestor de base de datos está instalado y ejecutándose:
```bash
sudo systemctl status mysql/mariadb
sudo systemctl start mysql/mariadb
sudo systemctl enable mysql/mariadb
```
1. Iniciar sesión en el gestor de base de datos:
```bash
sudo mysql -u root -p
```
2. Crear la base de datos del sistema:
```bash
CREATE DATABASE project3;
USE project3;
```
3. Importar el script de creación de tablas y datos iniciales (archivo database.sql incluido en el proyecto):
```bash
SOURCE ruta/al/proyecto/database.sql;
```
4. Verificar que las tablas se hayan creado correctamente:
```bash
SHOW TABLES;
```
5. Ejecutar el proyecto:
```bash
mvn exec:java 
```
