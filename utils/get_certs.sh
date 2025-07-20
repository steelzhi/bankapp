#!/usr/bin/env bash

rsync -av twc-build:/home/minikube/.minikube /home/minikube/ --exclude=cache;
