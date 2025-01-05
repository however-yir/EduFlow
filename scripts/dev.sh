#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
ENV_FILE="${ROOT_DIR}/.env"
ENV_EXAMPLE="${ROOT_DIR}/.env.example"
BACKEND_DIR="${ROOT_DIR}/teaching-manager-bk"

usage() {
  cat <<'USAGE'
Usage: ./scripts/dev.sh <command>

Commands:
  check-env    Validate local .env exists and no placeholder password remains
  infra-up     Start MySQL via docker compose
  infra-down   Stop MySQL via docker compose
  backend      Run Spring Boot backend (teaching-manager-bk)
  all          check-env + infra-up + backend
USAGE
}

ensure_env() {
  if [[ ! -f "${ENV_FILE}" ]]; then
    echo "Missing ${ENV_FILE}. Copy ${ENV_EXAMPLE} and fill your local values first."
    exit 1
  fi

  if grep -q 'change_me_' "${ENV_FILE}"; then
    echo "Detected placeholder secrets in ${ENV_FILE}. Please replace all change_me_* values."
    exit 1
  fi
}

compose() {
  docker compose --env-file "${ENV_FILE}" -f "${ROOT_DIR}/docker-compose.dev.yml" "$@"
}

cmd="${1:-}"
case "${cmd}" in
  check-env)
    ensure_env
    echo "Environment check passed."
    ;;
  infra-up)
    ensure_env
    compose up -d
    ;;
  infra-down)
    ensure_env
    compose down
    ;;
  backend)
    ensure_env
    cd "${BACKEND_DIR}"
    mvn spring-boot:run
    ;;
  all)
    ensure_env
    compose up -d
    cd "${BACKEND_DIR}"
    mvn spring-boot:run
    ;;
  *)
    usage
    exit 1
    ;;
esac
