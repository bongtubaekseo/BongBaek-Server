set -e

APP_NAME="bongbaek-server"
BLUE="bongbaek1-container"
GREEN="bongbaek2-container"

# DOCKER 로그인
docker login -u "$DOCKER_USERNAME" -p "$DOCKER_PASSWORD"

# 이미지 가져오기
docker pull $DOCKER_USERNAME/$APP_NAME:latest

# 현재 실행 중인 이미지 저장
CURRENT_CONTAINER=$(docker inspect -f '{{.Image}}' $GREEN 2>/dev/null)

# Redis & Dozzle 실행
docker compose up -d redis dozzle

# 새 버전 컨테이너 GREEN에서 실행
docker compose up -d $GREEN

timeout=120
count=0
until [ "$(docker inspect -f '{{.State.Health.Status}}' $GREEN)" = "healthy" ] || [ $count -ge $timeout ]; do
  sleep 5
  count=$((count+5))
done

if [ "$(docker inspect -f '{{.State.Health.Status}}' $GREEN)" != "healthy" ]; then
  echo "헬스 체크 실패 → 롤백"
  if [ -n "$CURRENT_CONTAINER" ]; then
    docker run -d --name $GREEN $CURRENT_CONTAINER || true
  fi
  exit 1
fi

echo "헬스 체크 성공"

# 새 버전 컨테이너 BLUE에서 실행
docker compose up -d $BLUE

timeout=120
count=0
until [ "$(docker inspect -f '{{.State.Health.Status}}' $BLUE)" = "healthy" ] || [ $count -ge $timeout ]; do
  sleep 5
  count=$((count+5))
done

if [ "$(docker inspect -f '{{.State.Health.Status}}' $BLUE)" != "healthy" ]; then
  echo "헬스 체크 실패 → 롤백"
  if [ -n "$CURRENT_CONTAINER" ]; then
    docker run -d --name $BLUE $CURRENT_CONTAINER || true
  fi
  exit 1
fi

# Nginx 트래픽 전환
docker compose up -d nginx

docker image prune -f
