#!/bin/bash

CODE=src
LIBRALARY_PSTG=lib/postgresql-42.5.0.jar
RESULT=tmp_out

java -cp "$RESULT":"$LIBRALARY_PSTG" Main $@
