#helm repo add jenkins-repo https://raw.githubusercontent.com/jenkinsci/kubernetes-operator/master/chart;
#helm repo update jenkins-repo;

#helm uninstall --namespace jenkins jenkins-operator-r1;
#helm install jenkins-operator-r1 jenkins-repo/jenkins-operator --namespace jenkins --create-namespace -f values.yml;
#kubectl delete jenkins jenkins -n default;


#Install by kubectl

kubectl delete ns jenkins; kubectl create ns jenkins;

#Jenkins CRD & operator:
kubectl -n jenkins apply -f https://raw.githubusercontent.com/jenkinsci/kubernetes-operator/master/config/crd/bases/jenkins.io_jenkins.yaml && \
kubectl -n jenkins apply -f https://raw.githubusercontent.com/jenkinsci/kubernetes-operator/master/deploy/all-in-one-v1alpha2.yaml && sleep 25s && \
./status.sh && ./recreate.sh;
