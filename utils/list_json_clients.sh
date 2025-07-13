#!/usr/bin/env bash

#set -x;

list_clients()
{
 	docker exec  keycloak bash -lc "/opt/keycloak/bin/kcadm.sh get clients -r master --fields clientId --server http://keycloak:8080 --realm master --user admin --password admin 2>/dev/null" | \
  	jq '.[] | select(.clientId)';
}

#[ $(list_clients | grep clientId | grep -E 'accounts|cash|exchange|front-ui|transfer' | wc -l) -eq 5 ]; exit $?;
list_clients;

