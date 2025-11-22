@echo off
echo ===================================
echo Compilando modulo server...
echo ===================================
echo.

mvn -f server\pom.xml clean package -DskipTests

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo ERROR: La compilacion fallo
    pause
    exit /b 1
)

echo.
echo ===================================
echo Compilacion exitosa!
echo ===================================
echo.
echo Arrancando el servidor Spring Boot...
echo El servidor estara disponible en http://localhost:8080
echo.
echo Presiona Ctrl+C para detener el servidor
echo.

mvn -f server\pom.xml spring-boot:run

