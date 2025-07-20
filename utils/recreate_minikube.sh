#!/usr/bin/env bash

#set -x;

Version=v1.32.6;
#Version=v1.31.10;

if [ "$USER" != "minikube" ]; then
	mkdir ~/.kube;
	rm -Rf ~/.kube/config;
	ln -s /home/minikube/.kube/config  ~/.kube/;
	echo "Created symlink to /home/minikube/.kube/config";
	echo "Minikube instance recreation shall be run under minikube user!";
	kubectl get nodes;
	exit 0;
fi;

start_mk()
{
	minikube start \
	  --insecure-registry="10.0.0.0/8" \
	  --kubernetes-version=$Version --driver=docker \
	  --cni=flannel --apiserver-ips=0.0.0.0 --apiserver-port=8444 \
	  --extra-config=kubeadm.ignore-preflight-errors=Swap  --feature-gates=NodeSwap=true  --extra-config=kubelet.fail-swap-on=false;
#	  --extra-config=kubelet.memory-swap.swap-behavior=LimitedSwap \
}

enable_addons()
{
	local Addons=$@;
	for A in $Addons; do
		minikube addons enable $A;
	done;
	minikube addons list;
}	

minikube stop; minikube delete; start_mk; sleep 10s;

enable_addons csi-hostpath-driver dashboard headlamp logviewer metrics-server portainer registry ingress storage-provisioner-rancher volumesnapshots storage-provisioner-rancher yakd;
# volcano # batch scheduler

#minikube node add;

minikube kubectl -- create token headlamp --duration 24h -n headlamp;
minikube profile list; minikube kubectl -- get all -A; minikube kubectl config view;
chmod 750 -R ~/.minikube;
chmod 666 ~/.kube/config;
kubectl get nodes;
