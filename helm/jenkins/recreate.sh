set -x;

kubectl -n jenkins delete jenkins instance1; 
kubectl apply -f instance1.yml && sleep 10s;

kubectl get all -n jenkins;