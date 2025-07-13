set -x;

if ! [ -f olm/install.sh ]; then
	./download.sh
fi;

( kubectl get -n keycloak keycloak bank || ( kubectl create ns keycloak; olm/install.sh v0.32.0; echo "Result: $?"; sleep 35s ) ) && \
kubectl apply -f keycloak-operator.yml && sleep 35s && ./recreate.sh;

