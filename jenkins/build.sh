set -x;

KubeRegistryHost=registry.kube-system.svc.cluster.local;
RegistryImage="registry.kube-system.svc.cluster.local:30050/jenkins:custom-v1";

export DOCKER_TLS_VERIFY="1";
export DOCKER_HOST="tcp://127.0.0.1:2376";
export DOCKER_CERT_PATH="/home/minikube/.minikube/certs";
#export MINIKUBE_ACTIVE_DOCKERD="minikube"

eval $(minikube docker-env);
docker build . -t jenkins:custom-v1;


#minikube image build . -t jenkins:custom-v1 --all=true; # --all for multinode minikube

#docker tag 	jenkins:custom-v1 	$RegistryImage;
#docker push 	$RegistryImage;
#curl http://minikube:30050/v2/_catalog;
#curl http://minikube:30050/v2/jenkins/tags/list;
