#!/usr/bin/env bash

# Running the image as
#  - removing the container after exit,
#  - detached (-d),
#  - binding localhost:9090 to container:9090
docker run --rm -d -p 9091:9091 teamb/w4e-scheduler-service

# to stop: docker stop ID
# to start a new shell in the container: docker exec -it ID bash
# to attach to the container: docker attach ID (^P ^Q to detach)
