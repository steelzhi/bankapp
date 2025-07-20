#!/usr/bin/env bash

set -x;

MinikubeIP=192.168.58.2

./kube_config.sh;

screen -S twc-build -X quit && sleep 1s;
screen -S twc-build -md bash -lc "ssh -L 8444:$MinikubeIP:8444 -D 0.0.0.0:12345 twc-build" && sleep 3s;
# -L 8080:localhost:8080
kubectl get nodes;

