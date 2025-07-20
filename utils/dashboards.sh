set -x;

minikube service headlamp -n headlamp;
kubectl create token headlamp --duration 24h -n headlamp;


minikube service yakd-dashboard  -n yakd-dashboard;

#minikube service kubernetes-dashboard -n kubernetes-dashboard;

minikube service portainer -n portainer;

