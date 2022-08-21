#!/bin/sh


DIRNAME="sftp_folder"

ORDER_FILE="orders.csv"

mkdir -p -- "$DIRNAME"
cp "$ORDER_FILE" "$DIRNAME"

docker build -f Dockerfile -t return-service:0.0.1-SNAPSHOT .

docker compose -f docker-compose-dev.yml up -d
