set -x;

kubectl delete -f bank-instance.yml && sleep 3s;

kubectl apply -f bank-instance.yml && sleep 75s && \
./status.sh && \
~/project/utils/load_json_clients.sh;

#./password.sh;
#bash -lc "kubectl -n keycloak logs pod/bank-0 -f";
