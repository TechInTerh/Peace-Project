#!/bin/sh

mc alias set myminio http://minio:9000 minioadmin minioadmin

if [ $(mc ls myminio | grep kafka-bucket | wc -l) -ne 1 ]; then
    mc mb -p myminio/kafka-bucket
    mc policy set public myminio/kafka-bucket
    exit 1
fi

if [ $(curl -X GET -H 'Accept: application/json' http://kafka-connect:8083/connectors | grep s3-sink-connector | wc -l) -ne 1 ]; then
    curl -X POST -H "Content-Type: application/json" --data @/srvc_connect/connector_config.json http://kafka-connect:8083/connectors
    exit 1
fi

exit 0
