#!/bin/sh


DIRNAME="sftp_folder"

ORDER_FILE="orders.csv"

mkdir -p -- "$DIRNAME"
cp "$ORDER_FILE" "$DIRNAME"

docker compose -f docker-compose-dev.yml up -d
