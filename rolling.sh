set -e

APP_NAME="bongbaek-server"
BLUE="bongbaek1-container"
GREEN="bongbaek2-container"

# rolling 사용시 bongbaek-server 이미지 태그를 버전별로 관리해야함.
PREV_VERSION="1.0.0"
DEPLOY_VERSION="1.0.1"

# DOCKER 로그인
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin

# 이미지 가져오기
docker pull $DOCKER_USERNAME/$APP_NAME:"$DEPLOY_VERSION"

# Redis & Dozzle 실행
docker compose up -d redis dozzle

# 새 버전 컨테이너 실행
for CONTAINER in "$GREEN" "$BLUE"; do

  docker compose stop "$CONTAINER" || true
  docker compose rm -f "$CONTAINER" || true

  APP_VERSION="$DEPLOY_VERSION" docker compose up -d "$CONTAINER"

  timeout=120
  count=0
  until [ "$(docker inspect -f '{{.State.Health.Status}}' "$CONTAINER")" = "healthy" ] || [ $count -ge $timeout ]; do
    sleep 5
    count=$((count+5))
  done

  if [ "$(docker inspect -f '{{.State.Health.Status}}' "$CONTAINER")" != "healthy" ]; then
    echo "헬스 체크 실패 → 롤백"

    docker stop "$CONTAINER" || true
    docker rm "$CONTAINER" || true

    APP_VERSION="$PREV_VERSION" docker compose up -d "$CONTAINER"
    exit 1
  fi

  echo "헬스 체크 성공"
done

# Nginx 트래픽 전환
docker compose up -d nginx

docker image prune -f
