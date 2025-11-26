#!/usr/bin/env bash
set -euo pipefail

#############################################
# launch-system.sh (versión mejorada)
#############################################

REPO_ROOT="$(cd "$(dirname "$0")/.." && pwd)"
echo "[launch] Repo root: $REPO_ROOT"

check_cmd() { command -v "$1" >/dev/null 2>&1; }

ask_yes_no() {
  while true; do
    read -rp "$1 [y/N]: " yn
    case $yn in
      [Yy]*) return 0 ;;
      [Nn]*|"") return 1 ;;
    esac
  done
}

#############################################
# DETECTAR docker-compose O docker compose
#############################################

detect_compose() {
  if docker compose version >/dev/null 2>&1; then
    echo "[check] Usando plugin integrado: docker compose"
    DOCKER_COMPOSE="docker compose"
  elif docker-compose version >/dev/null 2>&1; then
    echo "[check] Usando binario clásico: docker-compose"
    DOCKER_COMPOSE="docker-compose"
  else
    echo "[error] No se encontró docker compose ni docker-compose."
    echo "        Instala uno con:"
    echo "        sudo apt install docker-compose-plugin"
    exit 1
  fi
}

#############################################
# DETECTAR / INSTALAR DOCKER
#############################################

install_docker_apt() {
  echo "[install] Installing Docker (apt)..."
  sudo apt-get update
  sudo apt-get install -y ca-certificates curl gnupg lsb-release
  sudo mkdir -p /etc/apt/keyrings
  curl -fsSL https://download.docker.com/linux/$(. /etc/os-release; echo "$ID")/gpg | \
    sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
  echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
    https://download.docker.com/linux/$(. /etc/os-release; echo "$ID") \
    $(lsb_release -cs) stable" | sudo tee /etc/apt/sources.list.d/docker.list >/dev/null
  sudo apt-get update
  sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin
  sudo systemctl enable --now docker || true
}

install_docker_dnf() {
  echo "[install] Installing Docker (dnf)..."
  sudo dnf -y install dnf-plugins-core
  sudo dnf config-manager --add-repo https://download.docker.com/linux/fedora/docker-ce.repo
  sudo dnf -y install docker-ce docker-ce-cli containerd.io docker-compose-plugin
  sudo systemctl enable --now docker || true
}

install_docker_pacman() {
  echo "[install] Installing Docker (pacman)..."
  sudo pacman -Sy --noconfirm docker
  sudo systemctl enable --now docker || true
}

install_docker() {
  if check_cmd apt-get; then install_docker_apt
  elif check_cmd dnf; then install_docker_dnf
  elif check_cmd pacman; then install_docker_pacman
  else
    echo "[error] Tu distro no soporta instalación automática"
    exit 1
  fi
}

ensure_docker() {
  if check_cmd docker; then
    echo "[check] Docker detected: $(docker --version)"
  else
    echo "[check] Docker no encontrado."
    if ask_yes_no "¿Quieres instalar Docker ahora?"; then
      install_docker
    else
      echo "[abort] Debes instalar Docker antes de continuar."
      exit 1
    fi
  fi
}

#############################################
# FLUJO PRINCIPAL
#############################################

echo "[launch] Comprobando Docker..."
ensure_docker
detect_compose

echo "[launch] Levantando contenedores con $DOCKER_COMPOSE up -d..."
cd "$REPO_ROOT"
$DOCKER_COMPOSE up -d

#############################################
# ESPERAR A MYSQL
#############################################

echo "[launch] Esperando a que MySQL responda..."

MAX_ATTEMPTS=60
for ATTEMPT in $(seq 1 $MAX_ATTEMPTS); do
  if docker ps --format '{{.Names}}' | grep -q '^restaurant-mysql$'; then
    if docker exec restaurant-mysql mysqladmin ping -h localhost \
      -u root -prootpass --silent >/dev/null 2>&1; then

      echo "[launch] MySQL está listo."
      break
    fi
  fi
  echo "[launch] MySQL no responde todavía ($ATTEMPT/$MAX_ATTEMPTS)..."
  sleep 2
done

if [ "$ATTEMPT" -eq "$MAX_ATTEMPTS" ]; then
  echo "[error] MySQL no arrancó. Revisa: docker logs restaurant-mysql"
  exit 1
fi

#############################################
# COMPILAR SERVER
#############################################

if check_cmd mvn; then
  echo "[launch] Compilando server con Maven..."
  (cd server && mvn clean package -DskipTests)
else
  echo "[warn] Maven no está instalado. Debes compilar el server manualmente."
fi

#############################################
# ARRANCAR SERVER BACKGROUND
#############################################

echo "[launch] Arrancando servidor Spring Boot..."

if check_cmd mvn; then
  nohup mvn -f server spring-boot:run > "$REPO_ROOT/server/server.log" 2>&1 &
  SERVER_PID=$!
  echo "[launch] Servidor arrancado (PID=$SERVER_PID). Log: server/server.log"
else
  JAR_FILE=$(ls -t server/target/restaurant-server*.jar | head -n1)
  nohup java -jar "$JAR_FILE" > "$REPO_ROOT/server/server.log" 2>&1 &
  SERVER_PID=$!
  echo "[launch] JAR arrancado (PID=$SERVER_PID)."
fi

#############################################
# ESPERAR ENDPOINT /health
#############################################

echo "[launch] Esperando http://localhost:8080/api/health..."

for i in $(seq 1 60); do
  if curl -s -o /dev/null -w "%{http_code}" http://localhost:8080/api/health | grep -q "200"; then
    echo "[launch] Servidor OK."
    break
  fi
  echo "[launch] Aún no responde ($i/60)..."
  sleep 2
done

#############################################
# LANZAR FRONT
#############################################

echo "[launch] Preparado para lanzar front."

if check_cmd mvn; then
  echo ""
  echo "[info] Para arrancar el front manualmente:"
  echo "       mvn -Dexec.mainClass=es.ull.esit.app.JavaApplication2 exec:java"
  echo ""

  if ask_yes_no "¿Quieres lanzar el front ahora (en background)?"; then
    nohup mvn -Dexec.mainClass=es.ull.esit.app.JavaApplication2 exec:java \
        > "$REPO_ROOT/front.log" 2>&1 &
    echo "[launch] Front arrancado. Log: front.log"
  else
    echo "[launch] No se lanzó el front automáticamente."
  fi
else
  echo "[warn] Maven no disponible, no se puede lanzar el front automáticamente."
fi

echo "[launch] Todo listo"
