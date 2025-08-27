set -e

APP_NAME="bongbaek-server"
OLD_CONTAINER="blue"

# DOCKER 로그인
echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin

# 실행중인 컨테이너 확인
if docker ps --format '{{.Names}}' | grep -q "$OLD_CONTAINER"; then
  CURRENT="blue"
  IDLE="green"
else
  CURRENT="green"
  IDLE="blue"
fi

# 이미지 가져오기
docker pull $DOCKER_USERNAME/$APP_NAME:latest

# Redis & Dozzle 실행
docker compose up -d redis dozzle

# 새 버전 컨테이너 실행
docker compose up -d "$IDLE"

# 새 컨테이너 헬스체크
timeout=120
count=0
until [ "$(docker inspect -f '{{.State.Health.Status}}' "$IDLE")" = "healthy" ] || [ $count -ge $timeout ]; do
  sleep 5
  count=$((count+5))
done

if [ "$(docker inspect -f '{{.State.Health.Status}}' "$IDLE")" != "healthy" ]; then
  echo "헬스 체크 실패"
  docker stop ${IDLE}
  docker rm ${IDLE}
  exit 1
fi

echo "헬스 체크 성공"

# Nginx 트래픽 전환
docker compose up -d nginx

# 기존 컨테이너 종료
docker stop "${CURRENT}"
docker rm "${CURRENT}"

docker image prune -f

