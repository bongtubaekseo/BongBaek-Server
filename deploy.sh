# 서비스가 실행 중이면 그대로 두기, 아니면 재시작
docker compose up -d --no-recreate nginx nginx-exporter redis prometheus grafana dozzle

# bongbaek 서비스 롤링 배포
for service in bongbaek1 bongbaek2
do
    echo "===== Deploying $service ====="

    # 새 이미지 Pull
    docker compose pull $service

    # 현재 컨테이너를 stop & remove
    docker compose stop $service
    docker compose rm -f $service

    # 새 이미지로 컨테이너 재시작
    docker compose up -d $service

    # Health check
    echo "Waiting for $service to be healthy..."
    until [ "$(docker inspect --format='{{.State.Health.Status}}' $service)" = "healthy" ]; do
            echo "  $service is not ready yet. Waiting 5s..."
            sleep 5
        done
    echo "  $service is healthy!"
done

# 불필요한 이미지 정리
docker image prune -f

echo "===== Deployment Completed ====="