#!/usr/bin/env bash

function extract_jar()
{
    docker run \
        --rm \
        -t \
        -v "$PWD:/out" \
        --user "$(id -u):$(id -g)" \
        --entrypoint /bin/bash \
        $1 \
        -c "cp ./app.jar /out/$2.jar" || exit 1
}

function extract_zip()
{
    DIR=temp_$2
    mkdir $DIR || exit 1
    cd $DIR || exit 1

    docker run \
        --rm \
        -t \
        -v "$PWD:/out" \
        --user "$(id -u):$(id -g)" \
        --entrypoint /bin/ash \
        $1 \
        -c "cp -R ./node_modules ./dist /out/" || exit 1
    
    cd .. || exit 1
    zip -r $2.zip $DIR || exit 1
    rm -rf $DIR || exit 1
}

echo "** Generating artifacts"

TEMP_DIR=./artifacts-temp
rm -rf $TEMP_DIR || exit 1
mkdir $TEMP_DIR || exit 1
cd $TEMP_DIR || exit 1

extract_jar teamb/w4e-spring-backend backend
extract_jar teamb/w4e-spring-cli cli
extract_zip teamb/w4e-bank-service-builder bank
extract_zip teamb/w4e-scheduler-service-builder scheduler
extract_zip teamb/w4e-skipass-service-builder skipass

ls -Ali || exit 1
cd .. || exit 1

echo "** Done all"
