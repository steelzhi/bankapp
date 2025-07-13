Service=$1;

docker-compose down --remove-orphans $Service;

docker rmi -f $(docker images | grep bank | awk '{print $3}');
docker system prune -f;
docker volume prune -f;

docker compose build --no-cache $Service;
docker-compose up --force-recreate -d $Service;