set -x;

helm uninstall myapp; helm install myapp .;

sleep 10s;

./status.sh;