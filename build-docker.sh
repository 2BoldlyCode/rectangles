#!/bin/bash
set -x
artifacts=( cli rest )
for artifact in "${artifacts[@]/#/rectangles-}"
do
  ./mvnw install -pl $artifact -am
  ./mvnw install -Drectangles-docker=true -pl $artifact
done
