set -x;

kubectl logs jenkins-operator-75fd5974b-6ht9h -n jenkins;
kubectl -n jenkins logs pod/jenkins-instance1; # --previous;
#kubectl -n jenkins logs pod/jenkins-example -f;