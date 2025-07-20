set -x;

kubectl get all;

AccountsPod=$(kubectl get all | grep pod/myapp-accounts | grep -i -v terminating | awk '{print $1}');

kubectl exec -ti $AccountsPod -- env | sort;

kubectl logs $AccountsPod;