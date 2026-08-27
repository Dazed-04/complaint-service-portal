#!/bin/bash
# scripts/copy-schema-to-container.sh
# Copies all schema/reset SQL files into the Oracle container's /tmp,
# so sqlplus (running inside the container) can find them via @/tmp/<file>.

CONTAINER=complaint-portal-db

docker cp src/main/resources/schema/. "$CONTAINER":/tmp/
docker cp scripts/reset-all-data.sql "$CONTAINER":/tmp/
docker cp scripts/reset-schema-and-data.sql "$CONTAINER":/tmp/
