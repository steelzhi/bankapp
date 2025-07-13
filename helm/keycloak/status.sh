set -x;

status()
{
        kubectl -n olm get all;
#        kubectl -n olm describe subscription keycloak-operator;

		echo -e "\n\n\n";
       
        kubectl -n keycloak get all;
#        kubectl -n keycloak describe keycloak bank;

#        kubectl get installplan,csv,pods -A;
} 

status;