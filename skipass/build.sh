#!/usr/bin/env bash

echo "Compiling the NestJS Ski Pass system within a multi-stage docker build"

docker build -f ./Dockerfile.builder -t teamb/w4e-skipass-service-builder .
docker build -f ./Dockerfile -t teamb/w4e-skipass-service .
