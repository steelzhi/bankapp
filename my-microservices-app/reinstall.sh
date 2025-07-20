set -x;

helm uninstall myapp; 

rm charts_used/*/templates/*~;

helm dependency update .;
helm dependency build;

helm install myapp . -f values.yaml;
#helm uninstall myapp; helm spray  . -f values.yaml;

sleep 5s;

./status.sh;