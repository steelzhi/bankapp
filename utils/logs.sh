#docker logs keycloak-hc1

docker compose logs keycloak | head -n 20; echo "---";

docker compose logs keycloak-hc1 | head -n 20; echo "---";
docker compose logs keycloak-hc2 | head -n 30; echo "---";

docker compose logs blocker | head -n 20;

