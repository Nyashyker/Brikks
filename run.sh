#!/bin/bash

LIBRARY_PG=lib/postgresql-42.5.0.jar
RESULT=out/production/Brikks

java -cp "$RESULT":"$LIBRARY_PG" Main "$@"
