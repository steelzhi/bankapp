#set -x;
echo  "Username: $(kubectl -n keycloak get secret bank-initial-admin -o jsonpath='{.data.username}' | base64 --decode)"
echo "Password: $(kubectl -n keycloak get secret bank-initial-admin -o jsonpath='{.data.password}' | base64 --decode)"