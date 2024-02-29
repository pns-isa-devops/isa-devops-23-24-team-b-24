#!/usr/bin/env bash

echo "Compiling the NestJS Ski Pass system within a multi-stage docker build"

docker build -t teamb/w4e-skipass-service .
