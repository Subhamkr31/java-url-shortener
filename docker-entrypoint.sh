#!/bin/sh
set -e
if [ -n "${MONGO_URI_FILE:-}" ] && [ -f "${MONGO_URI_FILE}" ]; then
  export MONGO_URI=$(tr -d '\n\r' < "${MONGO_URI_FILE}")
fi
exec java -jar /app/app.jar
