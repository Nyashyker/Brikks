#!/bin/bash

CODE=src
LIBRALARY_PSTG=lib/postgresql-42.5.0.jar
RESULT=tmp_out

mkdir -p "$RESULT"
javac -cp "$LIBRALARY_PSTG" -d "$RESULT" $(find "$CODE" -name '*.java')

