set -x;

minikube status;

minikube profile list;

minikube kubectl -- get all -A;

minikube kubectl config view;
