@echo off
echo === Test del Middleware REST del Restaurante ===
echo.

set BASE_URL=http://localhost:8080

echo [1/3] Testeando GET /api/info...
curl -X GET %BASE_URL%/api/info
echo.
echo.

echo [2/3] Testeando GET /api/menu...
curl -X GET %BASE_URL%/api/menu
echo.
echo.

echo [3/3] Testeando POST /api/login...
curl -X POST %BASE_URL%/api/login -H "Content-Type: application/json" -d "{\"name\":\"abdualmajeed\"}"
echo.
echo.

echo === Tests completados ===
pause

