@echo off
cd /d C:\Users\Hecto\Desktop\Restaurant-System\restaurant-system
echo Levantando Docker Compose...
docker compose up -d > docker-up.log 2>&1
echo Resultado guardado en docker-up.log
echo.
echo Verificando contenedores...
docker ps -a >> docker-up.log 2>&1
type docker-up.log

