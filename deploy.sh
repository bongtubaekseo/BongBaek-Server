set -e

APP_NAME="bongbaek-server"
OLD_CONTAINER="bongbaek1-container"

# DOCKER 로그인
docker login -u "$DOCKER_USERNAME" -p "$DOCKER_PASSWORD"

# 실행중인 컨테이너 확인
if docker ps --format '{{.Names}}' | grep -q "$OLD_CONTAINER"; then
  CURRENT="bongbaek1"
  IDLE="bongbaek2"
else
  CURRENT="bongbaek2"
  IDLE="bongbaek1"
fi

# 이미지 가져오기
docker pull $DOCKER_USERNAME/$APP_NAME:latest

# Redis & Dozzle 실행
docker compose up -d redis dozzle

# 새 버전 컨테이너 실행
docker compose up -d $IDLE

# 새 컨테이너 헬스체크
timeout=120
count=0
until [ "$(docker inspect -f '{{.State.Health.Status}}' ${IDLE}-container)" = "healthy" ] || [ $count -ge $timeout ]; do
  sleep 5
  count=$((count+5))
done

if [ "$(docker inspect -f '{{.State.Health.Status}}' ${IDLE}-container)" != "healthy" ]; then
  echo "헬스 체크 실패"
  docker stop ${IDLE}-container
  docker rm ${IDLE}-container
  exit 1
fi

echo "헬스 체크 성공"

# Nginx 트래픽 전환
docker compose up -d nginx

# 기존 컨테이너 종료
docker stop ${CURRENT}-container
docker rm ${CURRENT}-container

docker image prune -f

