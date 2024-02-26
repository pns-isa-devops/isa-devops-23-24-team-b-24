#!/usr/bin/env bash

echo "Compiling the NestJS scheduler system within a multi-stage docker build"

docker build -t teamb/w4e-scheduler-service .
