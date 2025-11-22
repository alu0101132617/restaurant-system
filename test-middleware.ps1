# Script de PowerShell para testear el middleware REST
# Ejecutar: .\test-middleware.ps1

Write-Host "=== Test del Middleware REST del Restaurante ===" -ForegroundColor Cyan
Write-Host ""

$baseUrl = "http://localhost:8080"

# Test 1: Endpoint /api/info
Write-Host "[1/3] Testeando GET /api/info..." -ForegroundColor Yellow
try {
    $info = Invoke-RestMethod -Uri "$baseUrl/api/info" -Method GET
    Write-Host "✓ Éxito!" -ForegroundColor Green
    $info | ConvertTo-Json
    Write-Host ""
} catch {
    Write-Host "✗ Error: $_" -ForegroundColor Red
    Write-Host ""
}

# Test 2: Endpoint /api/menu
Write-Host "[2/3] Testeando GET /api/menu..." -ForegroundColor Yellow
try {
    $menu = Invoke-RestMethod -Uri "$baseUrl/api/menu" -Method GET
    Write-Host "✓ Éxito!" -ForegroundColor Green
    Write-Host "Platos principales encontrados: $($menu.mains.Count)"
    Write-Host "Aperitivos encontrados: $($menu.appetizers.Count)"
    Write-Host "Bebidas encontradas: $($menu.drinks.Count)"
    Write-Host ""
    Write-Host "Detalle del menú:"
    $menu | ConvertTo-Json -Depth 3
    Write-Host ""
} catch {
    Write-Host "✗ Error: $_" -ForegroundColor Red
    Write-Host ""
}

# Test 3: Endpoint /api/login
Write-Host "[3/3] Testeando POST /api/login..." -ForegroundColor Yellow
try {
    $body = @{
        name = "abdualmajeed"
    } | ConvertTo-Json

    $loginResponse = Invoke-RestMethod -Uri "$baseUrl/api/login" -Method POST -Body $body -ContentType "application/json"
    Write-Host "✓ Éxito!" -ForegroundColor Green
    $loginResponse | ConvertTo-Json
    Write-Host ""
} catch {
    Write-Host "✗ Error: $_" -ForegroundColor Red
    Write-Host ""
}

Write-Host "=== Tests completados ===" -ForegroundColor Cyan

