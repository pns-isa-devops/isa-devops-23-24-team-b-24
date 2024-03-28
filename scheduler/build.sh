#!/usr/bin/env bash

echo "Compiling the NestJS scheduler system within a multi-stage docker build"

docker build -f ./Dockerfile.builder -t teamb/w4e-scheduler-service-builder .
docker build -f ./Dockerfile -t teamb/w4e-scheduler-service .
