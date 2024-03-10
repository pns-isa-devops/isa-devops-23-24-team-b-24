#!/usr/bin/env bash

# Running the image as
#  - removing the container after exit,
#  - detached (-d),
#  - binding localhost:9092 to container:9092
docker run --rm -d -p 9092:9092 teamb/w4e-skipass-service

# to stop: docker stop ID
# to start a new shell in the container: docker exec -it ID bash
# to attach to the container: docker attach ID (^P ^Q to detach)
