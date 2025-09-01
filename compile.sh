#!/bin/bash

CODE=src
LIBRARY_PG=lib/postgresql-42.5.0.jar
RESULT=out/production/Brikks

mkdir -p "$RESULT"
javac -cp "$LIBRARY_PG" -d "$RESULT" $(find "$CODE" -name '*.java')
