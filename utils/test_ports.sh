#!/usr/bin/env bash
set -x;

TestHost=localhost;
if [ "$HOSTNAME" = "keycloak-hc1" ]; then
	TestHost="keycloak";
fi;

curl -I http://$TestHost:8080/realms/master && curl --head -fsS http://$TestHost:9000/health/ready;
Result=$?;
echo Result: $Result;

Result2=1;
if [ $Result -eq 0 ]; then
	Result2=0;
fi;

echo Result2: $Result2;
exit $Result2;
#exit $Result;


#docker exec -ti keycloak-hc1 curl -I http://keycloak:8080/realms/master
