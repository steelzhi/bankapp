#!/usr/bin/env bash
set -x;

[ $( /utils/list_json_clients.sh | grep clientId | grep -E 'accounts|cash|exchange|front-ui|transfer' | wc -l) -eq 5 ];
exit $?;

