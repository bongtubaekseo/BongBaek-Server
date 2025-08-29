set -e

APP_NAME="bongbaek-server"
FIRST="bongbaek1"
SECOND="bongbaek2"
CONTAINER="-container"

# 버전 정보 가져오기
DEPLOY_VERSION=$(tr -d '\r' < version)

PREV_VERSION=$(docker inspect --format='{{index .Config.Image}}' "$FIRST$CONTAINER" 2>/dev/null | awk -F: '{print $2}')

export APP_VERSION="$DEPLOY_VERSION"

# DOCKER 로그인
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin

# 이미지 가져오기
docker pull "$DOCKER_USERNAME/$APP_NAME:$DEPLOY_VERSION"

# 새 버전 컨테이너 실행
for SERVICE in "$FIRST" "$SECOND"; do

  docker stop "$SERVICE$CONTAINER" || true
  docker rm -f "$SERVICE$CONTAINER" || true
  docker compose up -d "$SERVICE"

  timeout=120
  count=0
  until [ "$(docker inspect -f '{{.State.Health.Status}}' "$SERVICE$CONTAINER")" = "healthy" ] || [ $count -ge $timeout ]; do
    sleep 5
    count=$((count+5))
  done

  if [ "$(docker inspect -f '{{.State.Health.Status}}' "$SERVICE$CONTAINER")" != "healthy" ]; then
    echo "헬스 체크 실패 → 롤백"

    docker stop "$SERVICE$CONTAINER" || true
    docker rm "$SERVICE$CONTAINER" || true

    APP_VERSION="$PREV_VERSION" docker compose up -d "$SERVICE"
    exit 1
  fi

  echo "헬스 체크 성공"
done

# Nginx 트래픽 전환
docker compose up -d nginx

docker image prune -f
