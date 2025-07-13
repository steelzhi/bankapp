rm ~/.kube/config || mkdir ~/.kube;
ln -s ~/project/utils/minikube.yml ~/.kube/config;
./get_certs.sh;

