#!/usr/bin/env bash

#set -x;

User=admin;
Password=admin;

TmpDir=/tmp/json_clients;


load_client()
{
	local Module=$1;
	local FileName=$Module.json;
	local File=~/project/$Module/src/main/resources/$FileName;
	if [ -f $File ]; then
#		docker exec keycloak /opt/keycloak/bin/kcadm.sh create clients -f $File  --server http://localhost:8080 --realm master --user $User --password $Password;
#		kubectl -n keycloak cp $File bank-0:/tmp/; # tar missing in the Keycloak official container image
		kubectl -n keycloak exec -i bank-0 -- bash -c "cat > /$TmpDir/$FileName" < /$File;
		kubectl -n keycloak exec -ti bank-0 -- /opt/keycloak/bin/kcadm.sh create clients -f /$TmpDir/$FileName  --server http://localhost:8080 --realm master --user $User --password $Password;
	else
		echo "===> Missing $File";
		exit 1;
	fi;

}

kubectl -n keycloak exec -i bank-0 -- bash -c "mkdir $TmpDir";

for ClientName in accounts cash exchange front-ui transfer; do #accounts blocker cash exchange exchange-generator front-ui notifications transfer
	load_client $ClientName;
done;

exit 0;
